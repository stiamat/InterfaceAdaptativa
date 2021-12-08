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
    userId: [],
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
      userId: profile.userId,
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
      userId: this.editForm.get(['userId'])!.value,
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
}
