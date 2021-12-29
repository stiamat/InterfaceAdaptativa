import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import {
  EMAIL_ALREADY_USED_TYPE,
  LOGIN_ALREADY_USED_TYPE,
} from 'app/shared/constants/error.constants';
import Swal from 'sweetalert2';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['register.scss'],
})
export class RegisterComponent implements AfterViewInit {
  @ViewChild('login', { static: false })
  login?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  registerForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern(
          '^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'
        ),
      ],
    ],
    email: [
      '',
      [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(254),
        Validators.email,
      ],
    ],
    password: [
      '',
      [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    ],
    confirmPassword: [
      '',
      [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    ],
  });

  constructor(
    private loginModalService: LoginModalService,
    private loginService: LoginService,
    private router: Router,
    private registerService: RegisterService,
    private fb: FormBuilder
  ) {}

  ngAfterViewInit(): void {
    if (this.login) {
      this.login.nativeElement.focus();
    }
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const login = this.registerForm.get(['login'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      this.registerService
        .save({ login, email, password, langKey: 'pt-br', activated: true })
        .subscribe(
          () => {
            this.success = true;
            this.loginTry();
          },
          response => this.processError(response)
        );
    }
  }

  loginTry(): void {
    // this.loginModalService.open();

    this.loginService
      .login({
        username: this.registerForm.get(['login'])!.value,
        password: this.registerForm.get(['password'])!.value,
        rememberMe: false,
      })
      .subscribe(
        account => {
          this.router.navigate(['feed']);
          Swal.fire({
            title: 'Sucesso!',
            text: 'Login Efetuado!',
            icon: 'success',
            showClass: {
              popup: 'animate__animated animate__fadeInDown',
            },
            hideClass: {
              popup: 'animate__animated animate__fadeOutUp',
            },
            showConfirmButton: false,
            timer: 3000,
          });
        },
        () => console.warn('error')
      );
  }

  openLogin(): void {
    // this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse): void {
    if (
      response.status === 400 &&
      response.error.type === LOGIN_ALREADY_USED_TYPE
    ) {
      this.errorUserExists = true;
    } else if (
      response.status === 400 &&
      response.error.type === EMAIL_ALREADY_USED_TYPE
    ) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }
}
