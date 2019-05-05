import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {ProfitEstimationComponent} from './profit-estimation.component';
import {PROFIT_ESTIMATION_ROUTES} from './profit-estimation.routes';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule, MatInputModule, MatNativeDateModule} from '@angular/material';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {ProfitEstimationService} from './profit-estimation.service';

@NgModule({
  declarations: [
    ProfitEstimationComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(PROFIT_ESTIMATION_ROUTES),
    BrowserAnimationsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule
  ], exports: [
    MatFormFieldModule,
    MatInputModule,
  ], providers: [
    MatDatepickerModule,
    ProfitEstimationService
  ]
})

export class ProfitEstimationModule { }
