// To be deleted at a later date
// For now this is for the stamp details page, will add more for the all stamps page
// since the stamp id page is the same for all.

export type Stamp = {
    id: number;
    name: string;
    image: string;
    address: string;
    location: string;
    openingHours: string;
    holiday: string;
    fee: string;
    available: boolean;
    obtained: boolean;
    wishlisted: boolean;
    dateObtained: Date | null;
    notes: string;
};

export const PLACEHOLDER_LOCATIONS: Stamp[] = [
    {
        id: 1,
        name: 'Mount Fuji Stamp',
        image: 'https://picsum.photos/seed/tokyo/200',
        address: '1-1 Fujisan, Fujinomiya, Shizuoka 418-0112, Japan',
        location: 'Shizuoka, Japan',
        openingHours: 'Mon–Sun: 09:00 – 17:00',
        holiday: 'None – open year round',
        fee: '¥1,000',
        available: true,
        obtained: true,
        wishlisted: false,
        dateObtained: new Date('2023-08-14'),
        notes: 'Picked this up at the 5th station gift shop. Beautiful clear day — could see all the way to the coast.',
    },
    {
        id: 2,
        name: 'Kyoto Temple Seal',
        image: 'https://picsum.photos/seed/kyoto/200',
        address: '1 Kinkakujicho, Kita-ku, Kyoto 603-8361',
        location: 'Kyoto, Japan',
        openingHours: 'Daily: 09:00 – 17:00',
        holiday: 'No regular closures',
        fee: '¥500',
        available: true,
        obtained: false,
        wishlisted: true,
        dateObtained: null,
        notes: '',
    },
    {
        id: 3,
        name: 'Eiffel Tower Stamp',
        image: 'https://picsum.photos/seed/paris/200',
        address: 'Champ de Mars, 5 Av. Anatole France, 75007 Paris',
        location: 'Paris, France',
        openingHours: 'Daily: 09:30 – 22:45',
        holiday: 'Closed Jan 1st',
        fee: '€29.40',
        available: false,
        obtained: false,
        wishlisted: true,
        dateObtained: null,
        notes: 'Need to check seasonal availability again in spring.',
    }
];

