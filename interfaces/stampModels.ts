export type StampRole = 'anonymous' | 'registered' | 'trusted' | 'admin';

export interface StampSetHours {
  mon: { open: boolean; openTime: string; closeTime: string };
  tue: { open: boolean; openTime: string; closeTime: string };
  wed: { open: boolean; openTime: string; closeTime: string };
  thu: { open: boolean; openTime: string; closeTime: string };
  fri: { open: boolean; openTime: string; closeTime: string };
  sat: { open: boolean; openTime: string; closeTime: string };
  sun: { open: boolean; openTime: string; closeTime: string };
}

export type StampLocationHours = StampSetHours;

export interface StampSetRow {
  id: string;
  created_by: string | null;
  name: string;
  address: string;
  location: string;
  latitude: number;
  longitude: number;
  hours: StampSetHours | null;
  holiday_mode: 'closed' | 'open' | 'limited';
  holiday_details: string;
  has_fee: boolean;
  fee_amount: number;
  fee_currency: string;
  publicly_viewable: boolean;
  created_at: string;
  updated_at: string;
  stamps?: StampRow[];
}

export type StampLocationRow = StampSetRow;

export interface StampRow {
  id: string;
  stamp_location_id: string;
  name: string;
  image_url: string;
  available: boolean;
  thumbs_up: number;
  thumbs_down: number;
  created_at: string;
  updated_at: string;
  stamp_locations: StampSetRow | null;
}

export interface StampUserRow {
  id: string;
  user_id: string;
  stamp_id: string;
  obtained: boolean;
  wishlisted: boolean;
  notes: string;
  vote: 'up' | 'down' | null;
  created_at: string;
  updated_at: string;
}

export type StampUserInfoChanges = Partial<Pick<StampUserRow, 'obtained' | 'wishlisted' | 'notes' | 'vote'>>;

export type UserStampFilter = 'obtained' | 'wishlisted';

export interface MapCoordinate {
  latitude: number;
  longitude: number;
}

export interface StampSetMarker {
  id: string;
  latitude: number;
  longitude: number;
}

export type StampLocationMarker = StampSetMarker;

export interface FetchLightOptions {
  light?: boolean;
  minimal?: boolean;
}

export interface StampRowMinimal {
  id: string;
  name: string;
  image_url: string;
}

export interface StampRowLight {
  id: string;
  stamp_location_id: string;
  name: string;
  image_url: string;
  available: boolean;
  thumbs_up: number;
  thumbs_down: number;
  created_at: string;
  updated_at: string;
  stamp_locations: { id: string; latitude: number; longitude: number } | null;
}
