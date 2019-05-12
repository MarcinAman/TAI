import {Currency} from '../app.model';

export class ProfitEstimationRequestData {
  constructor(
    public currency?: Currency,
    public dateFrom?: Date,
    public dateTo?: Date,
    public taxPercentage?: number,
    public amount?: number
  ){ }
}

export interface ProfitEstimationResponse {
  estimateProfit: ProfitEstimationResponseData
}

export interface ProfitEstimationResponseData {
  profitPreTax: number,
  profitPostTax: number
}
