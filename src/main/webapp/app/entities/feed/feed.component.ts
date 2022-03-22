import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/core/login/login.service';
import { ExperienceLevelMode } from 'app/shared/model/enumerations/experience-level-mode.model';
import { TipoPost } from 'app/shared/model/enumerations/tipo-post.model';
import { IPost, Post } from 'app/shared/model/post.model';
import { IPreferences } from 'app/shared/model/preferences.model';
import { IProfile } from 'app/shared/model/profile.model';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { PostService } from '../post/post.service';
import { PreferencesService } from '../preferences/preferences.service';
import { ProfileService } from '../profile/profile.service';

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
  isSearch = false;
  input = '';
  inputLink = '';
  inputSearch = '';
  loading = true;
  post: IPost = { id: 0 };
  feed: IPost[] = [];
  account: any = null;
  isSaving = false;
  link = false;
  profile: IProfile;
  tipoPost = 'NORMAL';
  preferences: IPreferences;
  isBasicMode = false;
  destaques: any[] = [];

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
    private accountService: AccountService,
    private profileService: ProfileService,
    private preferencesService: PreferencesService,
    private ref: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (this.account) {
        this.loadPreferences();

        this.profileService.find(this.account.id).subscribe(p => {
          this.profile = p.body;
          this.loadAll();
        });
      }
    });

    document.getElementById('area-text').addEventListener('keypress', event => {
      if (event.which === 13 && !event.shiftKey) {
        this.createPost();
      }
    });

    document
      .getElementById('input-busca')
      .addEventListener('keypress', event => {
        if (event.which === 13) {
          this.search();
        }
      });
  }

  loadAll(): void {
    //lista de amigos
    const listUser = this.profile.listFriends.map(u => u.id);
    listUser.push(3);
    listUser.push(this.profile.id);

    this.postService
      .query({
        'active.equals': true,
        // 'userId.in': listUser,
        'comentarioDeId.specified': false,
        page: 0,
        size: 999,
        sort: ['id,desc'],
      })
      .subscribe((res: HttpResponse<IPost[]>) => {
        this.loading = false;
        if (this.feed.length === 0) {
          this.feed = res.body;
        } else {
          res.body.forEach(p => {
            const teste = this.feed.find(pf => pf.id === p.id);

            if (!teste) {
              this.feed.unshift(p);
            }
          });
        }
      });

    this.postService.searchDestaques().subscribe(res => {
      this.destaques = res.body;
    });
  }

  async scrollFunction(event: any, scroll: string) {
    if (scroll === 'top' && event.pageY >= 1000) {
      for (let i = event.pageY - event.clientY; i >= 0; i = i - 20) {
        document.body.scrollTop = i;
        document.documentElement.scrollTop = i;
        await delay(3);
      }
    }
    if (scroll === 'up') {
      for (
        let i = event.pageY - event.clientY;
        i >= event.pageY - event.clientY - 225;
        i = i - 6
      ) {
        document.body.scrollTop = i;
        document.documentElement.scrollTop = i;
        await delay(5);
      }
    }
    if (scroll === 'down') {
      const cord = event.pageY - event.clientY;
      for (let i = 0; i < 25; i++) {
        document.body.scrollTop = cord + i * 9;
        document.documentElement.scrollTop = cord + i * 9;
        await delay(5);
      }
    }
  }

  private createFromForm(): IPost {
    let tipoPostFromForm = null;
    console.warn(this.tipoPost);

    if (this.tipoPost === 'VIDEOS') {
      tipoPostFromForm = TipoPost.VIDEOS;
    }
    if (this.tipoPost === 'ARTIGO') {
      tipoPostFromForm = TipoPost.ARTIGO;
    }
    if (this.tipoPost === 'RECOMENDACAO') {
      tipoPostFromForm = TipoPost.RECOMENDACAO;
    }
    if (this.tipoPost === 'CONFERENCIA') {
      tipoPostFromForm = TipoPost.CONFERENCIA;
    }

    return {
      ...new Post(),
      id: null,
      body: this.input,
      date: moment(moment.now()),
      tipoPost: tipoPostFromForm,
      active: true,
      likes: 0,
      userId: this.account.id,
      link: this.inputLink.length > 0 ? this.inputLink : null,
    };
  }

  loadPreferences() {
    this.preferencesService.find(this.account.id).subscribe((p: any) => {
      this.preferences = p.body;
      this.setIsBasicMode();
    });
  }

  setIsBasicMode() {
    if (
      this.preferences &&
      this.preferences.experienceLevelMode === ExperienceLevelMode.BASICMODE
    ) {
      this.isBasicMode = true;
    } else {
      this.isBasicMode = false;
    }
    this.ref.detectChanges();
    console.warn('basicmode', this.isBasicMode);
  }

  createPost() {
    if (this.input.length === 0) return;
    this.isSaving = true;
    const post = this.createFromForm();
    if (this.link) this.changeLink();
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
    Swal.fire({
      text: 'Deseja realmente apagar a postagem?',
      icon: 'warning',
      showConfirmButton: true,
      showCancelButton: true,
      cancelButtonText: 'Não',
      confirmButtonText: 'Sim',
      confirmButtonColor: '#3f3d56',
      cancelButtonColor: '#536dfe',
    }).then(res => {
      if (res.isConfirmed) {
        if (this.account.id === post.userId) {
          this.feed.splice(
            this.feed.findIndex(p => p.id === post.id),
            1
          );
          this.subscribeToSaveResponse(this.postService.delete(post.id));
        }
      }
    });
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  changeLink() {
    this.tipoPost = 'NORMAL';
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

  perfilPost(login: string) {
    this.router.navigate(['/perfil'], { queryParams: { login: '' + login } });
  }

  search() {
    if (this.inputSearch.length > 0) {
      this.feed = [];
      this.isSearch = true;
      this.postService
        .query({
          'body.contains': [this.inputSearch],
        })
        .subscribe(res => {
          this.feed = res.body;

          this.postService.searchLogin(this.inputSearch).subscribe(suc => {
            suc.body.map(i => {
              if (!this.feed.find(pf => pf.id === i.id)) this.feed.push(i);
            });
          });
        });
    } else {
      this.feed = [];
      this.isSearch = false;
      this.loadAll();
    }
  }

  searchDestaques(destaque: string) {
    this.inputSearch = destaque;
    this.isSearch = true;
    this.search();
  }

  clean() {
    this.feed = [];
    this.inputSearch = '';
    this.isSearch = false;
    this.loadAll();
  }
}
