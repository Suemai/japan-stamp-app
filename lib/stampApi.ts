import type {
    FetchLightOptions,
    MapCoordinate,
    StampLocationRow,
    StampRole,
    StampRow,
    StampRowLight,
    StampRowMinimal,
    StampSetHours,
    StampSetMarker,
    StampSetRow,
    StampUserInfoChanges,
    StampUserRow,
    UserStampFilter,
} from '@/interfaces/stampModels';
import { supabase } from './supabase';
import {
    fetchLocationByIdTemp,
    fetchLocationsTemp,
    fetchStampByIdTemp,
    fetchStampsAroundLocationTemp,
    fetchStampsTemp,
    fetchUserStampInfoTemp,
    fetchUserStampsTemp,
    isTempDataEnabled,
    updateUserStampInfoTemp,
} from './tempStampApi';

export type {
    FetchLightOptions,
    MapCoordinate,
    StampRole,
    StampRow,
    StampRowLight,
    StampRowMinimal,
    StampSetHours,
    StampSetMarker,
    StampSetRow,
    StampUserInfoChanges,
    StampUserRow,
    UserStampFilter
};

export async function fetchLocationMarkers(): Promise<StampSetMarker[]> {
  const { data, error } = await supabase
    .from('stamp_set')
    .select('id, latitude, longitude')
    .eq('publicly_viewable', true);

  if (error) {
    logSupabaseError('fetchLocationMarkers', error);
    throw error;
  }

  return (data ?? []) as StampSetMarker[];
}

export async function fetchLocationById(locationId: string): Promise<Array<StampSetRow & { stamps: StampRow[] }> | null> {
  if (isTempDataEnabled()) {
    return fetchLocationByIdTemp(locationId);
  }

  const { data, error } = await supabase
    .from('stamp_set')
    .select(`
      id,
      created_by,
      name,
      address,
      location,
      latitude,
      longitude,
      hours,
      holiday_mode,
      holiday_details,
      has_fee,
      fee_amount,
      fee_currency,
      publicly_viewable,
      created_at,
      updated_at,
      stamps:stamp (
        id,
        stamp_location_id,
        name,
        image_url,
        available,
        thumbs_up,
        thumbs_down,
        created_at,
        updated_at
      )
    `)
    .eq('id', locationId)
    .maybeSingle();

  if (error) {
    logSupabaseError('fetchLocationById', error);
    throw error;
  }

  if (!data) {
    return null;
  }

  const row = data as StampLocationRow & { stamps?: StampRow[] | StampRow };
  return [{
    ...row,
    stamps: Array.isArray(row.stamps) ? row.stamps : (row.stamps ? [row.stamps as StampRow] : []),
  }] as Array<StampLocationRow & { stamps: StampRow[] }>;
}

function logSupabaseError(operation: string, error: { message: string; code?: string; details?: string; hint?: string }) {
  console.error(`${operation} error:`, {
    message: error.message,
    code: error.code,
    details: error.details,
    hint: error.hint,
  });
}

export async function updateUserStampInfo(
  stampId: string,
  changes: StampUserInfoChanges,
): Promise<StampUserRow | null> {
  if (isTempDataEnabled()) {
    return updateUserStampInfoTemp(stampId, changes);
  }

  const { data: { session } } = await supabase.auth.getSession();
  const user = session?.user;
  if (!user) {
    throw new Error('No active user session.');
  }

  if (user.is_anonymous && changes.vote !== undefined && changes.vote !== null) {
    throw new Error('Anonymous users cannot vote on stamps.');
  }

  const { data, error } = await supabase
    .from('user_stamp_info')
    .upsert(
      {
        user_id: user.id,
        stamp_id: stampId,
        ...changes,
        updated_at: new Date().toISOString(),
      },
      { onConflict: 'user_id,stamp_id' },
    )
    .select('*')
    .single();

  if (error) {
    logSupabaseError('updateUserStampInfo', error);
    throw error;
  }

  return data as StampUserRow;
}

export async function fetchLocations(options: FetchLightOptions = {}): Promise<Array<StampLocationRow & { stamps: StampRow[] }>> {
  if (isTempDataEnabled()) {
    return fetchLocationsTemp();
  }

  if (options.light) {
    const { data, error } = await supabase
      .from('stamp_set')
      .select('id, latitude, longitude, name, address, location, created_at, updated_at')
      .eq('publicly_viewable', true);

    if (error) {
      logSupabaseError('fetchLocations(light)', error);
      throw error;
    }

    return (data ?? []).map(row => ({
      ...(row as StampSetRow),
      stamps: [],
    })) as Array<StampSetRow & { stamps: StampRow[] }>;
  }

  const { data, error } = await supabase
    .from('stamp_set')
    .select(`
      id,
      created_by,
      name,
      address,
      location,
      latitude,
      longitude,
      hours,
      holiday_mode,
      holiday_details,
      has_fee,
      fee_amount,
      fee_currency,
      publicly_viewable,
      created_at,
      updated_at,
      stamps:stamp (
        id,
        stamp_location_id,
        name,
        image_url,
        available,
        thumbs_up,
        thumbs_down,
        created_at,
        updated_at
      )
    `);

  if (error) {
    logSupabaseError('fetchLocations', error);
    throw error;
  }

  const rows = (data ?? []) as Array<StampLocationRow & { stamps?: StampRow[] | StampRow }>;
  return rows.map(row => ({
    ...row,
    stamps: Array.isArray(row.stamps) ? row.stamps : (row.stamps ? [row.stamps as StampRow] : []),
  })) as Array<StampLocationRow & { stamps: StampRow[] }>;
}

export async function fetchStamps(options: FetchLightOptions = {}): Promise<StampRow[] | StampRowMinimal[]> {
  if (isTempDataEnabled()) {
    return fetchStampsTemp();
  }

  if (options.minimal) {
    console.log("starting to fetch");
    const { data, error } = await supabase
      .from('stamp')
      .select('id, name, image_url');

    if (error) {
      logSupabaseError('fetchStamps(minimal)', error);
      throw error;
    }
    console.log("fetched");
    return (data ?? []) as StampRowMinimal[];
  }

  if (options.light) {
    const { data, error } = await supabase
      .from('stamp')
      .select('id, stamp_location_id, name, image_url, available, thumbs_up, thumbs_down, created_at, updated_at, stamp_locations:stamp_location_id(id, latitude, longitude)');
    if (error) {
      logSupabaseError('fetchStamps(light)', error);
      throw error;
    }

    return (data ?? []).map(row => ({
      id: row.id,
      stamp_location_id: row.stamp_location_id,
      name: row.name,
      image_url: row.image_url,
      available: row.available,
      thumbs_up: row.thumbs_up,
      thumbs_down: row.thumbs_down,
      created_at: row.created_at,
      updated_at: row.updated_at,
      stamp_locations: row.stamp_locations ?? null,
    })) as unknown as StampRow[];
  }

  const pageSize = 10000;
  const rows: Array<StampRow & { stamp_locations: StampLocationRow | StampLocationRow[] | null }> = [];

  for (let pageStart = 0; ; pageStart += pageSize) {
    const { data, error } = await supabase
      .from('stamp')
      .select(`
        id,
        stamp_location_id,
        name,
        image_url,
        available,
        thumbs_up,
        thumbs_down,
        created_at,
        updated_at,
        stamp_locations (
          id,
          created_by,
          name,
          address,
          location,
          latitude,
          longitude,
          hours,
          holiday_mode,
          holiday_details,
          has_fee,
          fee_amount,
          fee_currency,
          publicly_viewable,
          created_at,
          updated_at
        )
      `)
      .range(pageStart, pageStart + pageSize - 1);

    if (error) {
      logSupabaseError(`fetchStamps (rows ${pageStart}-${pageStart + pageSize - 1})`, error);
      throw error;
    }

    const page = (data ?? []) as Array<StampRow & { stamp_locations: StampLocationRow | StampLocationRow[] | null }>;
    rows.push(...page);

    if (page.length < pageSize) break;
  }

  console.log('Fetched stamps:', rows.length);
  return rows.map(row => ({
    ...row,
    stamp_locations: Array.isArray(row.stamp_locations) ? row.stamp_locations[0] ?? null : row.stamp_locations,
  })) as unknown as StampRow[];
}

export async function fetchStampsAroundLocation(
  centre: MapCoordinate,
  distanceKm: number,
): Promise<StampRow[]> {
  if (isTempDataEnabled()) {
    return fetchStampsAroundLocationTemp(centre, distanceKm);
  }

  console.log(`Fetching stamps around location: centre=${JSON.stringify(centre)}, distanceKm=${distanceKm}`);
  const radiusKm = Math.max(0, distanceKm);
  const latitudeDelta = radiusKm / 111.32;
  const longitudeScale = Math.max(Math.cos((centre.latitude * Math.PI) / 180), 0.01);
  const longitudeDelta = radiusKm / (111.32 * longitudeScale);

  console.log(`Calculated bounding box: latitudeDelta=${latitudeDelta}, longitudeDelta=${longitudeDelta}`);

  const { data, error } = await supabase
    .from('stamp')
    .select(`
      id,
      stamp_location_id,
      name,
      image_url,
      available,
      thumbs_up,
      thumbs_down,
      created_at,
      updated_at,
      stamp_locations:stamp_set!inner (
        id,
        created_by,
        name,
        address,
        location,
        latitude,
        longitude,
        hours,
        holiday_mode,
        holiday_details,
        has_fee,
        fee_amount,
        fee_currency,
        publicly_viewable,
        created_at,
        updated_at
      )
    `)
    .gte('stamp_locations.latitude', centre.latitude - latitudeDelta)
    .lte('stamp_locations.latitude', centre.latitude + latitudeDelta)
    .gte('stamp_locations.longitude', centre.longitude - longitudeDelta)
    .lte('stamp_locations.longitude', centre.longitude + longitudeDelta);

  if (error) {
    logSupabaseError('fetchStampsAroundLocation', error);
    throw error;
  }

  const rows = (data ?? []) as Array<StampRow & {
    stamp_locations: StampLocationRow | StampLocationRow[] | null;
  }>;
  console.log(`Fetched ${rows.length} stamps from database within bounding box.`);
  const earthRadiusKm = 6371;
  const centreLatitudeRadians = (centre.latitude * Math.PI) / 180;
  const centreLongitudeRadians = (centre.longitude * Math.PI) / 180;

  return rows
    .map(row => ({
      ...row,
      stamp_locations: Array.isArray(row.stamp_locations)
        ? row.stamp_locations[0] ?? null
        : row.stamp_locations,
    }))
    .filter(row => {
      const location = row.stamp_locations;
      if (!location || !location.publicly_viewable) return false;

      const latitudeRadians = (location.latitude * Math.PI) / 180;
      const longitudeRadians = (location.longitude * Math.PI) / 180;
      const latitudeDifference = latitudeRadians - centreLatitudeRadians;
      const longitudeDifference = longitudeRadians - centreLongitudeRadians;
      const haversine = Math.sin(latitudeDifference / 2) ** 2
        + Math.cos(centreLatitudeRadians)
        * Math.cos(latitudeRadians)
        * Math.sin(longitudeDifference / 2) ** 2;
      const distance = 2 * earthRadiusKm * Math.asin(Math.sqrt(haversine));

      return distance <= radiusKm;
    }) as StampRow[];
}

export async function fetchStampById(stampId: string): Promise<StampRow | null> {
  if (isTempDataEnabled()) {
    return fetchStampByIdTemp(stampId);
  }

  const { data, error } = await supabase
    .from('stamp')
    .select(`
      id,
      stamp_location_id,
      name,
      image_url,
      available,
      thumbs_up,
      thumbs_down,
      created_at,
      updated_at,
      stamp_locations:stamp_set (
        id,
        created_by,
        name,
        address,
        location,
        latitude,
        longitude,
        hours,
        holiday_mode,
        holiday_details,
        has_fee,
        fee_amount,
        fee_currency,
        publicly_viewable,
        created_at,
        updated_at
      )
    `)
    .eq('id', stampId)
    .maybeSingle();

  if (error) {
    logSupabaseError('fetchStampById', error);
    throw error;
  }

  const row = data as (StampRow & { stamp_locations: StampLocationRow | StampLocationRow[] | null }) | null;
  if (!row) {
    return null;
  }

  if (Array.isArray(row.stamp_locations)) {
    row.stamp_locations = row.stamp_locations[0] ?? null;
  }

  return row as unknown as StampRow | null;
}

export async function fetchUserStampInfo(stampId: string, userId: string): Promise<StampUserRow | null> {
  if (isTempDataEnabled()) {
    return fetchUserStampInfoTemp(stampId, userId);
  }

  const { data, error } = await supabase
    .from('user_stamp_info')
    .select('*')
    .eq('stamp_id', stampId)
    .eq('user_id', userId)
    .maybeSingle();

  if (error) {
    logSupabaseError('fetchUserStampInfo', error);
    throw error;
  }

  return (data ?? null) as StampUserRow | null;
}

export async function fetchUserStamps(filter: UserStampFilter): Promise<StampRow[]> {
  if (isTempDataEnabled()) {
    return fetchUserStampsTemp(filter);
  }

  const { data: { session } } = await supabase.auth.getSession();
  const userId = session?.user?.id;
  if (!userId) return [];

  const { data, error } = await supabase
    .from('user_stamp_info')
    .select(`
      stamp_id,
      stamps:stamp!user_stamp_info_stamp_id_fkey (
        id,
        stamp_location_id,
        name,
        image_url,
        available,
        thumbs_up,
        thumbs_down,
        created_at,
        updated_at
      )
    `)
    .eq('user_id', userId)
    .eq(filter, true);

  if (error) {
    logSupabaseError(`fetchUserStamps(${filter})`, error);
    throw error;
  }

  const rows = (data ?? []) as unknown as Array<{
    stamps: Omit<StampRow, 'stamp_locations'> | Array<Omit<StampRow, 'stamp_locations'>> | null;
  }>;

  return rows
    .map(row => Array.isArray(row.stamps) ? row.stamps[0] ?? null : row.stamps)
    .filter((stamp): stamp is Omit<StampRow, 'stamp_locations'> => Boolean(stamp))
    .map(stamp => ({ ...stamp, stamp_locations: null }));
}
