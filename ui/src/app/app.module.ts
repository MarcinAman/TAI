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
import {
  AuthServiceConfig,
  GoogleLoginProvider,
  LoginOpt,
  SocialLoginModule
} from 'angularx-social-login';

const googleLoginOptions: LoginOpt = {
  scope: 'profile email'
}; // https://developers.google.com/api-client-library/javascript/reference/referencedocs#gapiauth2clientconfig


let config = new AuthServiceConfig([
  {
    id: GoogleLoginProvider.PROVIDER_ID,
    provider: new GoogleLoginProvider('624796833023-clhjgupm0pu6vgga7k5i5bsfp6qp6egh.apps.googleusercontent.com', googleLoginOptions)
  }
]);

export function provideConfig() {
  return config;
}

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
    DragDropModule,
    SocialLoginModule
  ],
  providers: [
    {
      multi: true,
      provide: HTTP_INTERCEPTORS,
      useClass: AppHttpInterceptorService
    },
    MatNativeDateModule,
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
