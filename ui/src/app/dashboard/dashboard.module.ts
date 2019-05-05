import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsComponent } from './charts/charts.component';
import {RouterModule} from '@angular/router';
import {DASHBOARD_ROUTES} from './dashboard.routes';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    ChartsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(DASHBOARD_ROUTES)
  ]
})
export class DashboardModule {}
