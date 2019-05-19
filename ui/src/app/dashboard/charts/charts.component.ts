import { Component, OnInit } from '@angular/core';
import {CurrenciesService} from '../../currencies.service';
import {Chart} from './chart';
import {ChartOptions} from 'chart.js';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {AuthService, GoogleLoginProvider, SocialUser} from 'angularx-social-login';

@Component({
  selector: 'app-charts',
  templateUrl: './charts.component.html',
  styleUrls: ['./charts.component.css']
})
export class ChartsComponent implements OnInit {
  public currencies: any[];
  public newCurrency: string;
  public charts: Chart[] = [];
  public user: SocialUser;

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


  constructor(private currenciesService: CurrenciesService, private authService: AuthService) {}

  ngOnInit() {
    this.currenciesService.getCurrencyCodes().subscribe(v => this.currencies = v.data.currencies);
    this.authService.authState.subscribe((user) => {
      this.user = user;
    });
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
        currencyName: name.code + " " + name.currencyName
      })
      //TODO save in database
    });
  }

  deleteChart(index : number){
    this.charts.splice(index, 1);
    //TODO update in database
  }

  onDrop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.charts, event.previousIndex, event.currentIndex);
  }

  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  signOut(): void {
    this.authService.signOut();
  }
}
