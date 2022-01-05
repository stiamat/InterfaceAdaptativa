import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IProfile, Profile } from 'app/shared/model/profile.model';
import { ProfileService } from './profile.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-profile-update',
  templateUrl: './profile-update.component.html',
})
export class ProfileUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    ultimaModificacao: [],
    numModificacao: [],
    age: [],
    auditoryDisabilities: [],
    blindness: [],
    colorVision: [],
    contrastSensitivity: [],
    fildOfVision: [],
    lightSensitivity: [],
    visualAcuity: [],
    education: [],
    experienceLevel: [],
    gender: [],
    language: [],
    userId: [],
    listFriends: [],
  });

  constructor(
    protected profileService: ProfileService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profile }) => {
      if (!profile.id) {
        const today = moment().startOf('day');
        profile.ultimaModificacao = today;
      }

      this.updateForm(profile);

      this.userService
        .query()
        .subscribe(
          (res: HttpResponse<IUser[]>) => (this.users = res.body || [])
        );
    });
  }

  updateForm(profile: IProfile): void {
    this.editForm.patchValue({
      id: profile.id,
      status: profile.status,
      ultimaModificacao: profile.ultimaModificacao
        ? profile.ultimaModificacao.format(DATE_TIME_FORMAT)
        : null,
      numModificacao: profile.numModificacao,
      age: profile.age,
      auditoryDisabilities: profile.auditoryDisabilities,
      blindness: profile.blindness,
      colorVision: profile.colorVision,
      contrastSensitivity: profile.contrastSensitivity,
      fildOfVision: profile.fildOfVision,
      lightSensitivity: profile.lightSensitivity,
      visualAcuity: profile.visualAcuity,
      education: profile.education,
      experienceLevel: profile.experienceLevel,
      gender: profile.gender,
      language: profile.language,
      userId: profile.userId,
      listFriends: profile.listFriends,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profile = this.createFromForm();
    if (profile.id !== undefined) {
      this.subscribeToSaveResponse(this.profileService.update(profile));
    } else {
      this.subscribeToSaveResponse(this.profileService.create(profile));
    }
  }

  private createFromForm(): IProfile {
    return {
      ...new Profile(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      ultimaModificacao: this.editForm.get(['ultimaModificacao'])!.value
        ? moment(
            this.editForm.get(['ultimaModificacao'])!.value,
            DATE_TIME_FORMAT
          )
        : undefined,
      numModificacao: this.editForm.get(['numModificacao'])!.value,
      age: this.editForm.get(['age'])!.value,
      auditoryDisabilities: this.editForm.get(['auditoryDisabilities'])!.value,
      blindness: this.editForm.get(['blindness'])!.value,
      colorVision: this.editForm.get(['colorVision'])!.value,
      contrastSensitivity: this.editForm.get(['contrastSensitivity'])!.value,
      fildOfVision: this.editForm.get(['fildOfVision'])!.value,
      lightSensitivity: this.editForm.get(['lightSensitivity'])!.value,
      visualAcuity: this.editForm.get(['visualAcuity'])!.value,
      education: this.editForm.get(['education'])!.value,
      experienceLevel: this.editForm.get(['experienceLevel'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      language: this.editForm.get(['language'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      listFriends: this.editForm.get(['listFriends'])!.value,
    };
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IProfile>>
  ): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
