import { Component, OnInit } from '@angular/core';
import {CurrenciesService} from '../../currencies.service';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {

  private availableCurrencies: string[];
  private newCurrency: string;
  private charts: any[];

  constructor(
    private currenciesService: CurrenciesService
  ) { }

  ngOnInit() {
    this.currenciesService.getCurrencies().subscribe( v => this.availableCurrencies = v)
  }

  addChart() {

  }
}
