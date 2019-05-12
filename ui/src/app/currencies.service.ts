import {Injectable} from '@angular/core';
import {Observable} from 'rxjs'
import {Currencies, Currency, GraphQLResponse} from './app.model';
import {HttpClient} from '@angular/common/http';
import {environment} from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CurrenciesService {
  private httpEndpoint = environment.http_endpoint;

  constructor(private httpClient: HttpClient) {
  }

  getCurrencyCodes(): Observable<GraphQLResponse<Currencies>> {
    const body = {
      query: "query { currencies { currencyName code } }"
    };
    return this.httpClient.post<GraphQLResponse<Currencies>>(this.httpEndpoint, body)
  }

  getRatesFromPeriod(code: string, from: Date, to: Date): Observable<GraphQLResponse<any>>{
    const body = {
      query: "query sample($code: String!,$from: DateTime!, $to: DateTime!){ currencyFromPeriod(code: $code, from: $from, to: $to) { currencyPeriod { from to currency { currencyName code } } rates { date bid } } }",
      variables: {
        code : code,
        from : from,
        to: to
      }
    };
    return this.httpClient.post<GraphQLResponse<Currencies>>(this.httpEndpoint, body)
  }
}
