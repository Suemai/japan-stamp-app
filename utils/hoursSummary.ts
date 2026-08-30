// Building the summary for the opening hours of the location.

export const Day_Keys = ['mon', 'tue', 'wed', 'thu', 'fri', 'sat', 'sun'] as const;
export type DayKey = typeof Day_Keys[number];

export const Day_Labels: Record<DayKey, string> = {
    mon: 'Mon', tue: 'Tue', wed: 'Wed', thu: 'Thu', fri: 'Fri',
    sat: 'Sat', sun: 'Sun'
};

export const WEEKDAYS: DayKey[] = ['mon', 'tue', 'wed', 'thu', 'fri'];
export const WEEKEND: DayKey[]  = ['sat', 'sun'];

export function getDayGroupLabel(days: DayKey[]): string {
    const sorted = [...days].sort((a, b) => Day_Keys.indexOf(a) - Day_Keys.indexOf(b));
    const daily = Day_Keys.every(d => sorted.includes(d));
    const weekdays = WEEKDAYS.every(d => sorted.includes(d)) && !WEEKEND.some(d => sorted.includes(d));
    const weekend  = WEEKEND.every(d => sorted.includes(d))  && !WEEKDAYS.some(d => sorted.includes(d));

    if (daily) return 'Daily';
    if (weekdays) return 'Weekdays';
    if (weekend)  return 'Weekends';

    const remaining = [...sorted];
    const parts: string[] = [];

    if (WEEKDAYS.every(d => remaining.includes(d))) {
        WEEKDAYS.forEach(d => remaining.splice(remaining.indexOf(d), 1));
        parts.push('Weekdays');
    }

    if (WEEKEND.every(d => remaining.includes(d))) {
        WEEKEND.forEach(d => remaining.splice(remaining.indexOf(d), 1));
    }

    // Collapse consecutive days into ranges e.g. Mon – Wed, Fri
    const indices = remaining.map(d => Day_Keys.indexOf(d));
    let i = 0;
    while (i < indices.length) {
        let j = i;
        while (j + 1 < indices.length && indices[j + 1] === indices[j] + 1) j++;
        if (j > i + 1) {
            parts.push(`${Day_Labels[remaining[i]]} – ${Day_Labels[remaining[j]]}`);
        } else if (j > i) {
            parts.push(Day_Labels[remaining[i]], Day_Labels[remaining[j]]);
        } else {
            parts.push(Day_Labels[remaining[i]]);
        }
        i = j + 1;
    }

    if (WEEKEND.every(d => sorted.includes(d))) {
        parts.push('Weekends');
    }

    return parts.join(', ');
}

export function buildHoursSummary(hours: OpeningHours): string {
    const groups:Record<string, DayKey[]> = {};

    for (const day of Day_Keys) {
        const d = hours[day];
        const key = d.open ? `${d.openTime}-${d.closeTime}` : 'closed';
        if (!groups[key]) {
            groups[key] = [];
        }
        groups[key].push(day);
    }

    const lines = Object.entries(groups)
        .filter(([key]) => key !== 'closed')
        .map(([key, days]) => {
            const [open, close] = key.split('-');
            return `${getDayGroupLabel(days)}: ${open} – ${close}`;
        });
    return lines.length > 0 ? lines.join('\n') : 'No opening hours set';
}