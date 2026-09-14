-- 20260908174006_create_user_profiles.sql
-- Role model for Supabase auth-backed user identities.
-- Roles:
--   anonymous   -> default state for first-time or anonymous app visitors
--   registered  -> user has linked an email/password identity
--   trusted     -> can be assigned only by admins
--   admin       -> can manage all role assignments and data access

create type public.user_role as enum ('anonymous', 'registered', 'trusted', 'admin');

create table if not exists public.user_profiles (
  id uuid primary key references auth.users(id) on delete cascade,
  role public.user_role not null default 'anonymous',
  display_name text not null default 'Anonymous Stamp Collector',
  avatar_url text,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_user_profiles_role on public.user_profiles(role);

create or replace function public.current_user_role()
returns public.user_role
language sql
stable
security definer
set search_path = public
as $$
  select role
  from public.user_profiles
  where id = auth.uid();
$$;

create or replace function public.current_user_has_role(required_role public.user_role)
returns boolean
language sql
stable
security definer
set search_path = public
as $$
  select exists (
    select 1
    from public.user_profiles
    where id = auth.uid()
      and role = required_role
  );
$$;

create or replace function public.handle_new_auth_user()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  insert into public.user_profiles (id, role, display_name, avatar_url, created_at, updated_at)
  values (
    new.id,
    case
      when new.email is null then 'anonymous'::public.user_role
      else 'registered'::public.user_role
    end,
    coalesce(new.raw_user_meta_data ->> 'display_name', 'Anonymous Stamp Collector'),
    new.raw_user_meta_data ->> 'avatar_url',
    now(),
    now()
  )
  on conflict (id) do nothing;

  return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
after insert on auth.users
for each row execute procedure public.handle_new_auth_user();

create or replace function public.sync_profile_after_email_link()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  if new.email is distinct from old.email and new.email is not null then
    update public.user_profiles
    set role = 'registered'::public.user_role,
        updated_at = now()
    where id = new.id;
  end if;

  return new;
end;
$$;

drop trigger if exists on_auth_email_updated on auth.users;
create trigger on_auth_email_updated
after update of email on auth.users
for each row execute procedure public.sync_profile_after_email_link();

create or replace function public.enforce_user_role_assignment()
returns trigger
language plpgsql
security definer
set search_path = public
as $$
begin
  if new.role is distinct from old.role then
    if not public.current_user_has_role('admin') then
      raise exception 'Only an admin may assign or remove a role.';
    end if;
  end if;

  new.updated_at = now();
  return new;
end;
$$;

drop trigger if exists enforce_user_role_assignment on public.user_profiles;
create trigger enforce_user_role_assignment
before update on public.user_profiles
for each row execute procedure public.enforce_user_role_assignment();

alter table public.user_profiles enable row level security;

create policy "user_profiles_select_own_or_admin"
on public.user_profiles
for select
using (
  id = auth.uid()
  or public.current_user_has_role('admin')
);

create policy "user_profiles_insert_own_row"
on public.user_profiles
for insert
with check (id = auth.uid());

create policy "user_profiles_self_update_non_role_fields"
on public.user_profiles
for update
using (id = auth.uid())
with check (
  id = auth.uid()
  and role = (select role from public.user_profiles where id = auth.uid())
);

create policy "user_profiles_admin_update_any_row"
on public.user_profiles
for update
using (public.current_user_has_role('admin'))
with check (public.current_user_has_role('admin'));

create policy "user_profiles_delete_own_row_or_admin"
on public.user_profiles
for delete
using (
  id = auth.uid()
  or public.current_user_has_role('admin')
);

-- Optional example RLS policy for other app tables.
-- Every table that stores protected data should use a role gate such as:
-- using (
--   auth.uid() is not null and (
--     public.current_user_has_role('registered')
--     or public.current_user_has_role('trusted')
--     or public.current_user_has_role('admin')
--   )
-- )

-- Grant access for app and service role to keep the feature working from Expo clients:
grant usage on schema public to anon, authenticated, service_role;
grant select, insert, update, delete on public.user_profiles to authenticated, service_role;
