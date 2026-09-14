import type {
    MapCoordinate,
    StampRow,
    StampSetHours,
    StampSetRow,
    StampUserInfoChanges,
    StampUserRow,
    UserStampFilter,
} from '@/interfaces/stampModels';
import { PLACEHOLDER_LOCATIONS, StampLocation as TempLocation, Stamp as TempStamp } from '../data/tempData';

export type {
    MapCoordinate,
    StampRow,
    StampSetHours,
    StampSetRow,
    StampUserInfoChanges,
    StampUserRow,
    UserStampFilter
};

export function isTempDataEnabled(): boolean {
  const raw = String(process.env.EXPO_PUBLIC_USE_TEMP_DATA ?? 'false');
  return raw.toLowerCase() === 'true';
}

function mapHolidayMode(mode: string): 'closed' | 'open' | 'limited' {
  if (mode === 'known') return 'limited';
  return 'open';
}

function mapStampLocation(tempLocation: TempLocation): StampSetRow {
  return {
    id: String(tempLocation.id),
    created_by: null,
    name: tempLocation.name,
    address: tempLocation.address,
    location: tempLocation.location,
    latitude: tempLocation.latitude,
    longitude: tempLocation.longitude,
    hours: tempLocation.hours,
    holiday_mode: mapHolidayMode(tempLocation.holidayMode),
    holiday_details: tempLocation.holidayDetails,
    has_fee: tempLocation.hasFee,
    fee_amount: tempLocation.feeAmount ?? 0,
    fee_currency: tempLocation.feeCurrency,
    publicly_viewable: true,
    created_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
    updated_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
  };
}

function mapStamp(tempLocation: TempLocation, stamp: TempStamp): StampRow {
  return {
    id: String(stamp.id),
    stamp_location_id: String(tempLocation.id),
    name: stamp.name,
    image_url: stamp.image,
    available: stamp.available,
    thumbs_up: stamp.thumbsUp,
    thumbs_down: stamp.thumbsDown,
    created_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
    updated_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
    stamp_locations: mapStampLocation(tempLocation),
  };
}

export async function fetchLocationByIdTemp(locationId: string): Promise<Array<StampSetRow & { stamps: StampRow[] }> | null> {
  const location = PLACEHOLDER_LOCATIONS.find(item => String(item.id) === String(locationId));
  if (!location) {
    return Promise.resolve(null);
  }

  return Promise.resolve([{
    ...mapStampLocation(location),
    stamps: location.stamps.map(stamp => mapStamp(location, stamp)),
  }]);
}

export async function fetchLocationsTemp(): Promise<Array<StampSetRow & { stamps: StampRow[] }>> {
  return Promise.resolve(PLACEHOLDER_LOCATIONS.map(location => ({
    ...mapStampLocation(location),
    stamps: location.stamps.map(stamp => mapStamp(location, stamp)),
  })));
}

export async function fetchStampsTemp(): Promise<StampRow[]> {
  return Promise.resolve(PLACEHOLDER_LOCATIONS.flatMap(location =>
    location.stamps.map(stamp => ({
      ...mapStamp(location, stamp),
      stamp_locations: mapStampLocation(location),
    })),
  ));
}

export async function fetchStampsAroundLocationTemp(
  centre: MapCoordinate,
  distanceKm: number,
): Promise<StampRow[]> {
  const radiusKm = Math.max(0, distanceKm);
  const earthRadiusKm = 6371;
  const latitudeDelta = radiusKm / 111.32;
  const longitudeScale = Math.max(Math.cos((centre.latitude * Math.PI) / 180), 0.01);
  const longitudeDelta = radiusKm / (111.32 * longitudeScale);

  const rows: StampRow[] = [];
  for (const location of PLACEHOLDER_LOCATIONS) {
    const latitudeDifference = Math.abs(location.latitude - centre.latitude);
    const longitudeDifference = Math.abs(location.longitude - centre.longitude);
    if (latitudeDifference > latitudeDelta || longitudeDifference > longitudeDelta) {
      continue;
    }

    const latitudeRadians = (location.latitude * Math.PI) / 180;
    const longitudeRadians = (location.longitude * Math.PI) / 180;
    const centreLatitudeRadians = (centre.latitude * Math.PI) / 180;
    const centreLongitudeRadians = (centre.longitude * Math.PI) / 180;
    const latitudeDiff = latitudeRadians - centreLatitudeRadians;
    const longitudeDiff = longitudeRadians - centreLongitudeRadians;

    const haversine = Math.sin(latitudeDiff / 2) ** 2
      + Math.cos(centreLatitudeRadians)
      * Math.cos(latitudeRadians)
      * Math.sin(longitudeDiff / 2) ** 2;

    const distance = 2 * earthRadiusKm * Math.asin(Math.sqrt(haversine));
    if (distance <= radiusKm) {
      const stamp = location.stamps[0] ?? null;
      if (stamp) {
        rows.push({
          ...mapStamp(location, stamp),
          stamp_locations: mapStampLocation(location),
        });
      }
    }
  }

  return Promise.resolve(rows);
}

export async function fetchStampByIdTemp(stampId: string): Promise<StampRow | null> {
  for (const location of PLACEHOLDER_LOCATIONS) {
    for (const stamp of location.stamps) {
      if (String(stamp.id) === stampId) {
        return Promise.resolve(mapStamp(location, stamp));
      }
    }
  }

  return Promise.resolve(null);
}

export async function fetchUserStampInfoTemp(stampId: string, userId: string): Promise<StampUserRow | null> {
  for (const location of PLACEHOLDER_LOCATIONS) {
    for (const stamp of location.stamps) {
      if (String(stamp.id) === stampId) {
        return Promise.resolve({
          id: `temp-${stampId}`,
          user_id: userId,
          stamp_id: stampId,
          obtained: stamp.obtained,
          wishlisted: stamp.wishlisted,
          notes: stamp.notes,
          vote: stamp.userVote,
          created_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
          updated_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
        });
      }
    }
  }

  return Promise.resolve(null);
}

export async function fetchUserStampsTemp(filter: UserStampFilter): Promise<StampRow[]> {
  const rows: StampRow[] = [];
  for (const location of PLACEHOLDER_LOCATIONS) {
    for (const stamp of location.stamps) {
      const include = filter === 'obtained' ? stamp.obtained : stamp.wishlisted;
      if (include) {
        rows.push({
          ...mapStamp(location, stamp),
          stamp_locations: mapStampLocation(location),
        });
      }
    }
  }

  return Promise.resolve(rows);
}

const tempUserMap = new Map<string, StampUserRow>();

export async function updateUserStampInfoTemp(
  stampId: string,
  changes: StampUserInfoChanges,
): Promise<StampUserRow | null> {
  const key = `temp-user:${stampId}`;
  const existing = tempUserMap.get(key) ?? {
    id: `temp-${stampId}`,
    user_id: 'temp-user',
    stamp_id: stampId,
    obtained: false,
    wishlisted: false,
    notes: '',
    vote: null,
    created_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
    updated_at: new Date('2020-01-01T00:00:00.000Z').toISOString(),
  };

  const next = {
    ...existing,
    ...changes,
    updated_at: new Date().toISOString(),
  };

  tempUserMap.set(key, next);
  return Promise.resolve(next);
}
