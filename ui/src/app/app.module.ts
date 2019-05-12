import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppHttpInterceptorService } from './http-interceptor.service';
import { ROUTES } from './app.routes';
import { DashboardModule } from './dashboard/dashboard.module';
import { FormsModule }   from '@angular/forms';
import {ProfitEstimationModule} from './profit-estimation/profit-estimation.module';
import {MatNativeDateModule} from '@angular/material';
import { ChartsModule } from 'ng2-charts';
import { DragDropModule } from '@angular/cdk/drag-drop';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'Csrf-Token',
      headerName: 'Csrf-Token',
    }),
    NgbModule,
    RouterModule.forRoot(ROUTES),
    DashboardModule,
    FormsModule,
    ChartsModule,
    ProfitEstimationModule,
    MatNativeDateModule,
    DragDropModule
  ],
  providers: [
    {
      multi: true,
      provide: HTTP_INTERCEPTORS,
      useClass: AppHttpInterceptorService
    },
    MatNativeDateModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
