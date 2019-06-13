import { Component, OnInit } from '@angular/core';
import {CurrenciesService} from '../../currencies.service';
import {Chart} from './chart';
import {ChartOptions} from 'chart.js';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {UserService} from "../../user.service";

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  public currencies: any[];
  public newCurrency: string;
  public charts: Chart[] = [];

  private readonly options: ChartOptions = {
    elements: {
      line: {
        fill: false,
        tension: 0
      }
    },
    scales: {
      xAxes: [{
        type: "time",
        display: true,
        scaleLabel: {
          display: true,
          labelString: "Date"
        },
        ticks: {
          major:{
            fontStyle: "bold",
          }
        }
      }]
    }
  };


  constructor(
    private currenciesService: CurrenciesService,
    private userService: UserService,
  ) {}

  async ngOnInit() {
    const currenciesRaw  = await this.currenciesService.getCurrencyCodes().toPromise();
    this.currencies = currenciesRaw.data.currencies
    if (this.userService.currentUser) {
      this.userService.currentUser.dashboardCurrencies.map(dbCurrency => {
        const from = new Date();
        from.setDate(from.getDate() - 30);
        // From last 30 days
        const name = this.currencies.find(value => value.code === dbCurrency);
        this.currenciesService.getRatesFromPeriod(name.code, from, new Date())
          .subscribe(v => {
            const data = v.data.currencyFromPeriod.rates.map(x => {
              return {
                x: Date.parse(x.date),
                y: x.bid
              }
            });
            this.charts.push({
              type: "line",
              data: data,
              options: this.options,
              currencyName: name.code + " " + name.currencyName,
              currencyCode: name.code
            });
          });
      });
    }
  }

  addChart() {
    const from = new Date();
    from.setDate(from.getDate()-30);
    // From last 30 days
    const name = this.currencies.find(value => value.code === this.newCurrency);

    this.currenciesService.getRatesFromPeriod(this.newCurrency, from, new Date()).subscribe(v =>{
      const data = v.data.currencyFromPeriod.rates.map( x => {
        return {
          x: Date.parse(x.date),
          y: x.bid
        }
      });
      this.charts.push({
        type: "line",
        data: data,
        options: this.options,
        currencyName: name.code + " " + name.currencyName,
        currencyCode: name.code
      });

      if(this.userService.currentUser){
        this.userService.currentUser.dashboardCurrencies.push(name.code.toString());
        this.userService.saveUser(this.userService.currentUser).subscribe(() => {})
      }
    });
  }

  deleteChart(index : number){
    this.charts.splice(index, 1);
    this.userService.currentUser.dashboardCurrencies.splice(index, 1);
    this.userService.saveUser(this.userService.currentUser).subscribe(() => {})
  }

  onDrop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.charts, event.previousIndex, event.currentIndex);
  }
}
