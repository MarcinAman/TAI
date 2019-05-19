import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsComponent } from './charts/charts.component';
import {RouterModule} from '@angular/router';
import {DASHBOARD_ROUTES} from './dashboard.routes';
import {FormsModule} from '@angular/forms';
import {ChartsModule} from 'ng2-charts';
import {DragDropModule} from '@angular/cdk/drag-drop';
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
    ChartsComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule.forChild(DASHBOARD_ROUTES),
    ChartsModule,
    DragDropModule,
    SocialLoginModule
  ],
  providers: [
    {
      provide: AuthServiceConfig,
      useFactory: provideConfig
    }
  ]
})
export class DashboardModule {}
