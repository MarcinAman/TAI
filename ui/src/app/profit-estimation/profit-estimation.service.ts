import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {ProfitEstimationRequestData, ProfitEstimationResponseData} from './protfit-estimation.model';
import {Observable} from 'rxjs';
import {GraphQLResponse} from '../app.model';

@Injectable({
  providedIn: 'root'
})
export class ProfitEstimationService {
  private httpEndpoint = environment.http_endpoint;

  constructor(private httpClient: HttpClient) {
  }

  estimateProfit(data: ProfitEstimationRequestData): Observable<GraphQLResponse<ProfitEstimationResponseData>> {
    const body = {
      query: "query estimate($code: String!,$from: DateTime!, $to: DateTime!, $tax: Int){ estimateProfit(code: $code, from: $from, to: $to, tax: $tax) { profitPreTax, profitPostTax }",
      variables: {
        code: data.currency.code,
        from: data.dateFrom,
        to: data.dateTo,
        tax: data.taxPercentage
      }
    };

    return this.httpClient.post<GraphQLResponse<ProfitEstimationResponseData>>(this.httpEndpoint, body)
  }
}
