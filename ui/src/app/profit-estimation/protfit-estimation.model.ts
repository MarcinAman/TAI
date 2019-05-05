import {Currency} from '../app.model';

export class ProfitEstimationRequestData {
  constructor(
    public currency?: Currency,
    public dateFrom?: Date,
    public dateTo?: Date,
    public taxPercentage?: number
  ){ }
}

export interface ProfitEstimationResponseData {
  profitPreTax: number,
  profitPostTax: number
}
