import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { IPost, Post } from 'app/shared/model/post.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { PostService } from '../post/post.service';

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function submitOnEnter(event: any) {
  if (event.which === 13) {
    // event.preventDefault(); // Prevents the addition of a new line in the text field (not needed in a lot of cases)
  }
}
@Component({
  selector: 'jhi-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['feed.component.scss'],
})
export class FeedComponent implements OnInit {
  input = '';
  inputLink = '';
  loading = true;
  post: IPost = { id: 0 };
  feed = [this.post, this.post, this.post];
  account: any = null;
  isSaving = false;
  link = false;

  editForm = this.fb.group({
    id: [],
    body: [],
    date: [],
    active: [],
    likes: [],
    userId: [],
  });

  constructor(
    private postService: PostService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    (async () => {
      await delay(3000);
      this.loadAll();
    })();

    this.accountService
      .getAuthenticationState()
      .subscribe(account => (this.account = account));

    document.getElementById('area-text').addEventListener('keypress', event => {
      if (event.which === 13 && !event.shiftKey) {
        this.createPost();
      }
    });
  }

  loadAll(): void {
    this.postService
      .query({
        'active.equals': true,
        page: 0,
        size: 999,
        sort: ['id,desc'],
      })
      .subscribe((res: HttpResponse<IPost[]>) => {
        this.loading = false;
        this.feed = res.body;
        console.log(res.body);
      });
  }

  private createFromForm(): IPost {
    return {
      ...new Post(),
      id: null,
      body: this.input,
      date: moment(moment.now()),
      active: true,
      likes: 0,
      userId: this.account.id,
      link: this.inputLink.length > 0 ? this.inputLink : null,
    };
  }

  createPost() {
    if (this.input.length === 0) return;
    this.isSaving = true;
    const post = this.createFromForm();
    this.subscribeToSaveResponse(this.postService.create(post));
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
    this.input = null;
    this.loadAll();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  checkinput(event: any): void {
    // if (event.which === 13) {
    //   this.createPost();
    //   event.preventDefault(); // Prevents the addition of a new line in the text field (not needed in a lot of cases)
    // }
  }

  deletePost(post: IPost) {
    if (this.account.id === post.userId) {
      this.subscribeToSaveResponse(this.postService.delete(post.id));
    }
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  changeLink() {
    this.link = !this.link;
    this.inputLink = '';
    const link = document.querySelector('#link');
    if (this.link) {
      link?.classList.add('visible');
    } else {
      link?.classList.remove('visible');
    }
  }

  navegaWeb(post: IPost) {
    window.open(post.link, '_blank');
  }
}
