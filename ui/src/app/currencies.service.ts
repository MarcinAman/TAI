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

  getCurrencies(): Observable<GraphQLResponse<Currencies>> {

    const body = {
      query: "query { currencies { currencyName code } }"
    };

    return this.httpClient.post<GraphQLResponse<Currencies>>(this.httpEndpoint, body)
  }
}
