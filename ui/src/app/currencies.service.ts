import { Injectable } from '@angular/core';
import {Observable, of} from 'rxjs'
@Injectable({
  providedIn: 'root'
})
export class CurrenciesService {

  constructor() { }


  getCurrencies(): Observable<string[]>{
    return of(['USD', 'EUR', 'GBP'])
  }
}
