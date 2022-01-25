import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPost } from 'app/shared/model/post.model';
import { IProfile } from 'app/shared/model/profile.model';
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
  user: IUser = null;
  profile: IProfile = null;
  post: IPost[] = null;
  loading = false;
  feed: IPost[] = [];
  loginProfile: string;
  isFriend = false;

  constructor(
    private postService: PostService,
    private loginService: LoginService,
    private router: Router,
    protected activatedRoute: ActivatedRoute,
    private userService: UserService,
    private accountService: AccountService,
    private profileService: ProfileService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .subscribe(account => (this.account = account));

    this.activatedRoute.queryParams.subscribe(
      param => {
        this.loginProfile = param.login;
        if (!this.loginProfile) this.loginProfile = this.account.login;
        this.loadUser();
      },
      err => {
        this.loginProfile = this.account.login;
        this.loadUser();
      }
    );
  }

  loadUser() {
    this.userService.find(this.loginProfile).subscribe(
      suc => {
        this.user = suc;
        this.loadProfile();
        this.loadAll();
      },
      err => {
        this.router.navigate(['404']);
      }
    );
  }

  itens(item: string) {
    if (item === 'inicio') {
      this.router.navigate(['/feed']);
    }
    if (item === 'perfil') {
      this.router.navigate(['/perfil']);
    }
    if (item === 'config') {
      this.router.navigate(['/config/' + this.account.id + '/edit']);
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

  loadAll(queryIn: any = null): void {
    let query = {
      'active.equals': true,
      'userId.equals': [this.user.id],
      page: 0,
      size: 999,
      sort: ['id,desc'],
    };
    if (queryIn != null) query = queryIn;

    this.postService.query(query).subscribe((res: HttpResponse<IPost[]>) => {
      this.loading = false;
      this.feed = res.body;
    });
  }

  loadProfile(): void {
    this.profileService.find(this.account.id).subscribe(suc => {
      this.profile = suc.body;
      if (this.profile.listFriends.find(i => i.id === this.user.id)) {
        this.isFriend = true;
      } else {
        this.isFriend = false;
      }
    });
  }

  carregar(tipo: any) {
    this.feed = [];
    tipo = tipo.tab.textLabel;

    let query = null;

    if (tipo === 'Respostas') {
      this.postService.respostasUser(this.user.id).subscribe(suc => {
        this.feed = suc.body;
      });
      return;
    } else {
      query = {
        'active.equals': true,
        'userId.equals': [this.user.id],
        page: 0,
        size: 999,
        sort: ['id,desc'],
      };
      if (tipo === 'Videos') {
        query = Object.assign({ 'tipoPost.equals': 'VIDEOS' }, query);
      }
      if (tipo === 'PDF') {
        query = Object.assign({ 'tipoPost.equals': 'RECOMENDACAO' }, query);
      }
      if (tipo === 'Artigos') {
        query = Object.assign({ 'tipoPost.equals': 'ARTIGO' }, query);
      }
      if (tipo === 'Conferencia') {
        query = Object.assign({ 'tipoPost.equals': 'CONFERENCIA' }, query);
      }
    }

    this.loadAll(query);
  }

  friends() {
    this.profileService
      .friends(this.profile.id, this.user.id)
      .subscribe(suc => {
        this.profile = suc.body;
        if (this.profile.listFriends.find(i => i.id === this.user.id)) {
          this.isFriend = true;
        } else {
          this.isFriend = false;
        }
      });
  }
}
