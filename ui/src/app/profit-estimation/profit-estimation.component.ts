import {Component, OnInit} from "@angular/core";
import {CurrenciesService} from '../currencies.service';
import {Currency} from '../app.model';
import {ProfitEstimationRequestData, ProfitEstimationResponseData} from './protfit-estimation.model';
import {ProfitEstimationService} from './profit-estimation.service';

@Component({
  selector: 'app-profit-estimation',
  templateUrl: './profit-estimation.component.html',
})
export class ProfitEstimationComponent implements OnInit {
  availableCurrencies: Currency[];

  minDate = new Date();

  profitEstimationData: ProfitEstimationRequestData;
  calculatedProfit: ProfitEstimationResponseData;

  constructor(
    private currenciesService: CurrenciesService,
    private profitEstimationService: ProfitEstimationService
  ) {
    this.availableCurrencies = [];
    this.profitEstimationData = new ProfitEstimationRequestData();
  }

  ngOnInit(): void {
    this.currenciesService.getCurrencies()
      .subscribe(e => {
        this.availableCurrencies = e.data.currencies;
      })
  }

  calculateProfit() {
    this.profitEstimationService.estimateProfit(this.profitEstimationData).subscribe(e => this.calculatedProfit = e.data)
  }
}
