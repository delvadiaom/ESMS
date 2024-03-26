export interface ChartModel {
    title: string;
    innerData?: { name: string; y: number }[];
    innerDataForLineChart?: { name: string, data: number[] }[];
    colors: string[];
    yAxisText?: string;
    xAxisText?: string;
    baseStart?: number;
}