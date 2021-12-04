import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { Account } from 'app/core/user/account.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  inputlogin = true;
  authenticationError = false;
  input = '';
  password = '';

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService
      .getAuthenticationState()
      .subscribe(account => {
        this.account = account;
        if (this.isAuthenticated()) {
          this.router.navigate(['feed']);
        }
      });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    // this.loginModalService.open();

    this.loginService
      .login({
        username: this.input,
        password: this.password,
        rememberMe: false,
      })
      .subscribe(
        account => {
          this.authenticationError = false;
          this.account = account;
          this.router.navigate(['feed']);
        },
        () => (this.authenticationError = true)
      );
  }

  register(): void {
    this.router.navigate(['/account/register']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  changeInput(): void {
    const cadastrar = document.querySelector('#novoporaqui');
    const login = document.querySelector('#login');

    if (this.inputlogin) {
      login?.classList.add('removeOpacity');
      login?.classList.remove('addOpacity');

      cadastrar?.classList.add('addOpacity');
      cadastrar?.classList.remove('removeOpacity');

      cadastrar?.classList.replace('novo', 'novoAtivo');
      login?.classList.replace('loginAtivo', 'loginDesativado');

      document.querySelector('#circleFloat')?.classList.add('addCircleFloat');
      document
        .querySelector('#circleFloat')
        ?.classList.remove('removeCircleFloat');
    } else {
      login?.classList.remove('removeOpacity');
      login?.classList.add('addOpacity');

      cadastrar?.classList.remove('addOpacity');
      cadastrar?.classList.add('removeOpacity');

      cadastrar?.classList.replace('novoAtivo', 'novo');
      login?.classList.replace('loginDesativado', 'loginAtivo');

      document
        .querySelector('#circleFloat')
        ?.classList.add('removeCircleFloat');
      document
        .querySelector('#circleFloat')
        ?.classList.remove('addCircleFloat');
    }

    this.inputlogin = !this.inputlogin;
  }
}
