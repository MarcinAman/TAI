import {Component, OnInit} from '@angular/core';
import {CurrenciesService} from '../../currencies.service';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {

  public availableCurrencies: string[];
  public newCurrency: string;
  public charts: any[];

  constructor(
    private currenciesService: CurrenciesService
  ) {
  }

  ngOnInit() {
    this.currenciesService.getCurrencies().subscribe(v => this.availableCurrencies = v.data.currencies.map(e => e.code))
  }

  addChart() {

  }
}
