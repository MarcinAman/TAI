import {ChartOptions, ChartType} from 'chart.js';

export interface Chart {
  type: ChartType,
  data: any[],
  options: ChartOptions,
  currencyName: string
}
