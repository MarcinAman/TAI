import {Component, OnInit} from '@angular/core';
import {AuthService, GoogleLoginProvider, SocialUser} from 'angularx-social-login';
import {operators} from 'rxjs/internal/Rx';
import {UserService} from './user.service';
import {EMPTY, Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public user: SocialUser;

  constructor(
    private authService: AuthService,
    private userService: UserService,
  ) {
  }

  ngOnInit() {
    this.authService.authState.pipe(operators.flatMap((user) => {
        this.user = user;
        if (user) {
          this.userService.currentUser = {
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            email: this.user.email,
            dashboardCurrencies: []
          };
          return this.userService.saveIfNotExistsUser(this.userService.currentUser);
        } else {
          return EMPTY
        }
      })
    ).subscribe(x => {
      if (x.data) {
        this.userService.currentUser = x.data.saveIfNotExistsUser
      }
    })

  }

  signInWithGoogle(): void {
    Observable.from(this.authService.signIn(GoogleLoginProvider.PROVIDER_ID)).pipe(operators.flatMap((user) => {
        this.user = user;
        if (user) {
          this.userService.currentUser = {
            firstName: this.user.firstName,
            lastName: this.user.lastName,
            email: this.user.email,
            dashboardCurrencies: []
          };
          return this.userService.saveIfNotExistsUser(this.userService.currentUser);
        } else {
          return EMPTY
        }
      })
    ).subscribe(x => {
      if (x.data) {
        this.userService.currentUser = x.data.saveIfNotExistsUser
      }
    })

  }

  signOut(): void {
    this.authService.signOut().then(() => this.userService.currentUser = null);
  }

}
