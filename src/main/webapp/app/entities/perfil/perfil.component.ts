import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { IPost } from 'app/shared/model/post.model';
import * as moment from 'moment';
import { PostService } from '../post/post.service';
import { ProfileService } from '../profile/profile.service';

@Component({
  selector: 'jhi-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['perfil.scss'],
})
export class PerfilComponent implements OnInit {
  account: any = null;
  post: IPost[] = null;
  loading = false;
  feed: IPost[] = [];

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
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.loadAll();
    });
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

  navegaWeb(post: IPost) {
    window.open(post.link, '_blank');
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

  comentar(post: number) {
    this.router.navigate(['/feed/' + post]);
  }

  loadAll(): void {
    this.postService
      .query({
        'active.equals': true,
        'userId.equals': [this.account.id],
        page: 0,
        size: 999,
        sort: ['id,desc'],
      })
      .subscribe((res: HttpResponse<IPost[]>) => {
        this.loading = false;
        this.feed = res.body;
      });
  }
}
