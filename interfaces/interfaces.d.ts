/* TODO
Declare the interface or in our java terms, the constructor of the class that implements it.
Example:

interface Stamp {
    name: string;
    location: string;
 */

// Report Location

interface StampPhoto{
    uri:string;
    id:string;
}

type HolidayMode = 'known' | 'unknown' | 'none';

interface DayHours {
    open: boolean;
    openTime: string;
    closeTime: string;
}

interface OpeningHours {
    mon: DayHours;
    tue: DayHours;
    wed: DayHours;
    thu: DayHours;
    fri: DayHours;
    sat: DayHours;
    sun: DayHours;
}

interface LocationForm{
    name: string;
    address: string;
    usingCurrentLocation: boolean;
    hours: OpeningHours;
    holidayMode: HolidayMode;
    holidayDetails: String;
    stampAvailable: boolean;
    hasFee: boolean;
    feeAmount: number;
    feeCurrency: string;
}

interface StampData {
    stampName: string;
    stampPhoto: StampPhoto | null;
}

interface ReportSubmission {
    location: LocationForm;
    stamps: StampFormData[];
}