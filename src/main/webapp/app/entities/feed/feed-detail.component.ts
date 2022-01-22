import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { IPost, Post } from 'app/shared/model/post.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { PostService } from '../post/post.service';
import { ProfileService } from '../profile/profile.service';

@Component({
  selector: 'jhi-feed-detail',
  templateUrl: './feed-detail.component.html',
  styleUrls: ['feed.component.scss'],
})
export class FeedDetailComponent implements OnInit {
  account: any = null;
  post: IPost = null;
  respostas: IPost[] = [];

  input = '';
  inputLink = '';
  link = false;
  isSaving = true;

  constructor(
    private postService: PostService,
    private loginService: LoginService,
    private router: Router,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private accountService: AccountService,
    private profileService: ProfileService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ post }) => {
      this.post = post;
      this.loadRespostas();
    });
    this.accountService
      .getAuthenticationState()
      .subscribe(account => (this.account = account));

    document
      .getElementById('area-text-resp')
      .addEventListener('keypress', event => {
        if (event.which === 13 && !event.shiftKey) {
          this.createPost();
        }
      });
  }

  loadRespostas() {
    this.isSaving = true;
    this.postService.respostas(this.post.id).subscribe(suc => {
      this.respostas = suc.body;
      this.respostas = this.respostas.sort((a, b) => {
        if (a.date > b.date) return 1;
        if (a.date < b.date) return -1;
        return 0;
      });
      this.isSaving = false;
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
      comentarioDeId: this.post.id,
    };
  }

  perfilPost(login: string) {
    this.router.navigate(['/perfil'], { queryParams: { login: '' + login } });
  }

  timePublicacao(date: moment.Moment) {
    const atual = moment(moment.now());
    if (atual.diff(date, 'years') > 0)
      return 'há ' + atual.diff(date, 'years') + ' ano(s)';
    if (atual.diff(date, 'months') >= 1)
      return 'há ' + atual.diff(date, 'months') + ' mês';
    if (atual.diff(date, 'days') >= 1 && atual.diff(date, 'days') < 30)
      return 'há ' + atual.diff(date, 'days') + ' dia(s)';
    if (atual.diff(date, 'hours') >= 1 && atual.diff(date, 'hours') < 24)
      return 'há ' + atual.diff(date, 'hours') + ' hora(s)';
    if (atual.diff(date, 'minutes') >= 1 && atual.diff(date, 'minutes') < 60)
      return 'há ' + atual.diff(date, 'minutes') + ' minuto(s)';
    if (atual.diff(date, 'seconds') >= 1 && atual.diff(date, 'seconds') < 60)
      return 'há ' + 1 + ' minuto';
  }

  curti(post: IPost) {
    this.postService.curti(post.id, this.account.id).subscribe(suc => {
      post.likeDes = suc.body;
    });
  }

  curtido(post: IPost): boolean {
    if (post.likeDes.find(u => u.id === this.account.id)) return true;
    return false;
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
    this.loadRespostas();

    // add na lista e respostas
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  itens(item: string) {
    if (item === 'inicio') {
      this.router.navigate(['/feed']);
    }
    if (item === 'perfil') {
      this.router.navigate(['/perfil']);
    }
    if (item === 'config') {
      this.router.navigate(['/config']);
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

  previousState(): void {
    window.history.back();
  }

  deletePost(post: IPost) {
    if (this.account.id === post.userId) {
      if (post.id === this.post.id) {
        this.postService.delete(post.id).subscribe(suc => {
          this.router.navigate(['/feed']);
        });
      } else {
        this.respostas.splice(
          this.respostas.findIndex(p => p.id === post.id),
          1
        );
        this.subscribeToSaveResponse(this.postService.delete(post.id));
      }
    }
  }
}
