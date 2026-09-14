-- 20260908175028_create_stamp_tables.sql
-- Base stamp location and stamp model for the Japan stamp app.
-- user_stamp_info stores per-user mutable values that are unique to the auth user.

create extension if not exists pgcrypto;

create type public.holiday_mode as enum ('closed', 'open', 'limited');
create type public.vote_direction as enum ('up', 'down');

create table if not exists public.stamp_set (
  id uuid primary key default gen_random_uuid(),
  created_by uuid references auth.users(id) on delete set null,
  name text not null,
  address text not null default '',
  location text not null default '',
  latitude double precision not null default 0,
  longitude double precision not null default 0,
  hours jsonb not null default '[]'::jsonb,
  holiday_mode public.holiday_mode not null default 'open',
  holiday_details text not null default '',
  has_fee boolean not null default false,
  fee_amount numeric(8,2) not null default 0,
  fee_currency text not null default 'JPY',
  publicly_viewable boolean not null default false,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.stamp (
  id uuid primary key default gen_random_uuid(),
  stamp_location_id uuid not null references public.stamp_set(id) on delete cascade,
  name text not null,
  image_url text not null default '',
  available boolean not null default true,
  thumbs_up integer not null default 0,
  thumbs_down integer not null default 0,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create table if not exists public.user_stamp_info (
  id uuid primary key default gen_random_uuid(),
  user_id uuid not null references auth.users(id) on delete cascade,
  stamp_id uuid not null references public.stamp(id) on delete cascade,
  obtained boolean not null default false,
  wishlisted boolean not null default false,
  notes text not null default '',
  vote public.vote_direction,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),
  unique (user_id, stamp_id)
);

create index if not exists idx_stamps_location_id on public.stamp(stamp_location_id);
create index if not exists idx_user_stamp_info_user_id on public.user_stamp_info(user_id);
create index if not exists idx_user_stamp_info_stamp_id on public.user_stamp_info(stamp_id);

alter table public.stamp_set enable row level security;
alter table public.stamp enable row level security;
alter table public.user_stamp_info enable row level security;

create policy "stamp_set_select_public_or_owned"
on public.stamp_set
for select
using (
  publicly_viewable = true
  or created_by = auth.uid()
  or public.current_user_has_role('admin')
);

create policy "stamp_select_public"
on public.stamp
for select
using (true);

create policy "stamp_set_insert_registered_or_above"
on public.stamp_set
for insert
with check (
  created_by = auth.uid()
  and public.current_user_role() in ('registered', 'trusted', 'admin')
);

create policy "stamp_set_update_owner_or_admin"
on public.stamp_set
for update
using (
  created_by = auth.uid()
  or public.current_user_has_role('admin')
)
with check (
  (
    created_by = auth.uid()
    and public.current_user_role() in ('registered', 'trusted', 'admin')
  )
  or public.current_user_has_role('admin')
);

create policy "stamp_set_delete_owner_or_admin"
on public.stamp_set
for delete
using (
  created_by = auth.uid()
  or public.current_user_has_role('admin')
);

create policy "stamp_admin_write"
on public.stamp
for all
using (public.current_user_has_role('admin'))
with check (public.current_user_has_role('admin'));

create policy "user_stamp_info_select_owner_or_admin"
on public.user_stamp_info
for select
using (
  user_id = auth.uid()
  or public.current_user_has_role('admin')
);

create policy "user_stamp_info_insert_owner_any_role_but_vote_is_registered_only"
on public.user_stamp_info
for insert
with check (
  user_id = auth.uid()
  and (
    (
      public.current_user_role() = 'anonymous'
      and vote is null
    )
    or (
      public.current_user_role() in ('registered', 'trusted', 'admin')
    )
  )
);

create policy "user_stamp_info_update_owner_any_role_but_vote_is_registered_only"
on public.user_stamp_info
for update
using (
  user_id = auth.uid()
  or public.current_user_has_role('admin')
)
with check (
  (
    user_id = auth.uid()
    and (
      (
        public.current_user_role() = 'anonymous'
        and vote is null
      )
      or (
        public.current_user_role() in ('registered', 'trusted', 'admin')
      )
    )
  )
  or public.current_user_has_role('admin')
);

create policy "user_stamp_info_delete_owner_or_admin"
on public.user_stamp_info
for delete
using (
  user_id = auth.uid()
  or public.current_user_has_role('admin')
);

create or replace function public.stamp_vote_count(p_stamp_id uuid)
returns table (thumbs_up bigint, thumbs_down bigint)
language sql
stable
security definer
set search_path = public
as $$
  select
    count(*) filter (where vote = 'up')::bigint as thumbs_up,
    count(*) filter (where vote = 'down')::bigint as thumbs_down
  from public.user_stamp_info
  where stamp_id = p_stamp_id;
$$;

grant usage on schema public to anon, authenticated, service_role;
grant select on public.stamp_set to anon, authenticated, service_role;
grant select on public.stamp to anon, authenticated, service_role;
grant select, insert, update, delete on public.user_stamp_info to authenticated, service_role;

grant insert, update, delete on public.stamp_set to authenticated, service_role;
grant insert, update, delete on public.stamp to authenticated, service_role;
