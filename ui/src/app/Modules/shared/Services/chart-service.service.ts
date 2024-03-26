import { Injectable } from '@angular/core';
import { ChartModel } from '../model/chart-model';

@Injectable({
    providedIn: 'root',
})
export class ChartService {
    getPieChart(data: ChartModel) {
        return {
            credits: {
                enabled: false,
            },
            chart: {
                type: 'pie',
            },
            title: {
                text: data.title,
                align: 'center',
            },
            colors: data.colors,
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y:.2f}',
                    },
                },
            },
            tooltip: {
                headerFormat:
                    '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat:
                    '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b><br/>',
            },
            series: [
                {
                    name: data.title,
                    colorByPoint: true,
                    data: data.innerData,
                },
            ],

            responsive: {
                rules: [
                    {
                        condition: {
                            maxWidth: 500,
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom',
                            },
                        },
                    },
                ],
            },
        };
    }
    getBarChart(data: ChartModel) {
        return {
            colors: data.colors,
            credits: {
                enabled: false,
            },
            chart: {
                type: 'column',
            },
            title: {
                align: 'center',
                text: data.title,
            },

            accessibility: {
                announceNewData: {
                    enabled: true,
                },
            },
            xAxis: {
                type: 'category',
            },
            yAxis: {
                title: {
                    text: data.yAxisText,
                },
            },
            legend: {
                enabled: false,
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.1f}',
                    },
                },
            },

            tooltip: {
                headerFormat:
                    '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat:
                    '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}</b> <br/>',
            },

            series: [
                {
                    name: data.title,
                    colorByPoint: true,
                    data: data.innerData,
                },
            ],
            responsive: {
                rules: [
                    {
                        condition: {
                            maxWidth: 500,
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom',
                            },
                        },
                    },
                ],
            },
        };
    }
    getLineChart(data:ChartModel) {
        return {
            title: {
                text: data.title,
                align: 'left',
            },

           
            yAxis: {
                title: {
                    text: data.yAxisText,
                },
            },

            xAxis: {
                accessibility: {
                    rangeDescription: data.xAxisText,
                },
            },

            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
            },

            plotOptions: {
                series: {
                    label: {
                        connectorAllowed: false,
                    },
                    pointStart: data.baseStart,
                },
            },

            series: data.innerDataForLineChart,

            responsive: {
                rules: [
                    {
                        condition: {
                            maxWidth: 500,
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom',
                            },
                        },
                    },
                ],
            },
        };
    }
}
