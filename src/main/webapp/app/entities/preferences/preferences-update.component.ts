import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';
import { IPreferences, Preferences } from 'app/shared/model/preferences.model';
import { IProfile, Profile } from 'app/shared/model/profile.model';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { ProfileService } from '../profile/profile.service';
import { PreferencesService } from './preferences.service';

@Component({
  selector: 'jhi-preferences-update',
  templateUrl: './preferences-update.component.html',
  styleUrls: ['../feed/feed.component.scss'],
})
export class PreferencesUpdateComponent implements OnInit {
  isSaving = false;
  account: any = null;
  users: IUser[] = [];
  inputImgUrl = '';

  editForm = this.fb.group({
    id: [],
    darkMode: [],
    experienceLevelMode: [],
    fontMode: [],
    contrastMode: [],
    colorVisionMode: [],
    userId: [],
  });

  constructor(
    private loginService: LoginService,
    private router: Router,
    protected profileService: ProfileService,
    private accountService: AccountService,
    protected preferencesService: PreferencesService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.inputImgUrl = account.imageUrl;
    });

    this.activatedRoute.data.subscribe(({ preferences }) => {
      this.updateForm(preferences);
    });
  }

  updateForm(preferences: IPreferences): void {
    this.editForm.patchValue({
      id: preferences.id,
      darkMode: preferences.darkMode,
      experienceLevelMode: preferences.experienceLevelMode,
      fontMode: preferences.fontMode,
      contrastMode: preferences.contrastMode,
      colorVisionMode: preferences.colorVisionMode,
      userId: preferences.userId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const preferences = this.createFromForm();
    if (preferences.id !== undefined) {
      this.subscribeToSaveResponse(this.preferencesService.update(preferences));
    } else {
      this.subscribeToSaveResponse(this.preferencesService.create(preferences));
    }
  }

  resetProfile(): void {
    this.isSaving = true;
    const profile = this.createFromFormProfile();
    console.warn(profile);

    this.subscribeToSaveResponse(this.profileService.update(profile));
  }

  private createFromForm(): IPreferences {
    return {
      ...new Preferences(),
      id: this.editForm.get(['id'])!.value,
      darkMode: this.editForm.get(['darkMode'])!.value,
      experienceLevelMode: this.editForm.get(['experienceLevelMode'])!.value,
      fontMode: this.editForm.get(['fontMode'])!.value,
      contrastMode: this.editForm.get(['contrastMode'])!.value,
      colorVisionMode: this.editForm.get(['colorVisionMode'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }

  private createFromFormProfile(): IProfile {
    return {
      ...new Profile(),
      id: this.account.id,
      status: StatusProfile.NOVO,
      userId: this.account.id,
    };
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IPreferences>>
  ): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    location.assign('/feed');
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  changeImgPerfil() {
    this.userService.find(this.account.login).subscribe(s => {
      const user = s;
      user.imageUrl = this.inputImgUrl;
      this.userService.update(user).subscribe(() => {
        Swal.fire({
          title: 'Sucesso!',
          text: 'Login Efetuado!',
          icon: 'success',
          showConfirmButton: false,
          timer: 3000,
        });
      });
    });
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  itens(item: string) {
    if (item === 'inicio') {
      this.router.navigate(['/feed']);
    }
    if (item === 'perfil') {
      this.router.navigate(['/perfil'], {
        queryParams: { login: '' + this.account.login },
      });
    }
    if (item === 'config') {
      this.router.navigate(['/config/' + this.account.id + '/edit']);
    }
  }
}
