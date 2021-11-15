import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPost, Post } from 'app/shared/model/post.model';
import { PostService } from './post.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = IUser | IPost;

@Component({
  selector: 'jhi-post-update',
  templateUrl: './post-update.component.html',
})
export class PostUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  posts: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    body: [],
    date: [],
    active: [],
    likes: [],
    link: [],
    tipoPost: [],
    userId: [],
    likeDes: [],
    comentarioDeId: [],
  });

  constructor(
    protected postService: PostService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ post }) => {
      if (!post.id) {
        const today = moment().startOf('day');
        post.date = today;
      }

      this.updateForm(post);

      this.userService
        .query()
        .subscribe(
          (res: HttpResponse<IUser[]>) => (this.users = res.body || [])
        );

      this.postService
        .query()
        .subscribe(
          (res: HttpResponse<IPost[]>) => (this.posts = res.body || [])
        );
    });
  }

  updateForm(post: IPost): void {
    this.editForm.patchValue({
      id: post.id,
      body: post.body,
      date: post.date ? post.date.format(DATE_TIME_FORMAT) : null,
      active: post.active,
      likes: post.likes,
      link: post.link,
      tipoPost: post.tipoPost,
      userId: post.userId,
      likeDes: post.likeDes,
      comentarioDeId: post.comentarioDeId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const post = this.createFromForm();
    if (post.id !== undefined) {
      this.subscribeToSaveResponse(this.postService.update(post));
    } else {
      this.subscribeToSaveResponse(this.postService.create(post));
    }
  }

  private createFromForm(): IPost {
    return {
      ...new Post(),
      id: this.editForm.get(['id'])!.value,
      body: this.editForm.get(['body'])!.value,
      date: this.editForm.get(['date'])!.value
        ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT)
        : undefined,
      active: this.editForm.get(['active'])!.value,
      likes: this.editForm.get(['likes'])!.value,
      link: this.editForm.get(['link'])!.value,
      tipoPost: this.editForm.get(['tipoPost'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      likeDes: this.editForm.get(['likeDes'])!.value,
      comentarioDeId: this.editForm.get(['comentarioDeId'])!.value,
    };
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IPost>>
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

  trackById(index: number, item: SelectableEntity): any {
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
