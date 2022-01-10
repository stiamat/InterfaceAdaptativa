import {
  AfterViewInit,
  Component,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AccountService } from 'app/core/auth/account.service';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';
import { IProfile } from 'app/shared/model/profile.model';
import * as moment from 'moment';
import { ProfileService } from '../profile/profile.service';

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
interface Lang {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.scss'],
})
export class SearchComponent implements OnInit, AfterViewInit {
  tabSelectedSearch: number;
  totalTabSearch: number;
  resultSearch: any;
  account: any;
  dataNascimento: Date;

  profile: IProfile;
  language: Lang[] = [
    { value: 'pt-br', viewValue: 'Português - Brasil' },
    { value: 'pt-pt', viewValue: 'Português - Portugal' },
    { value: 'en', viewValue: 'Inglês' },
    { value: 'es', viewValue: 'Espanhol' },
    { value: 'default', viewValue: 'Outra Língua' },
  ];
  genero: string[] = ['Feminino', 'Masculino', 'Neutro', 'Outro'];
  experiencia: string[] = [
    'Experiência Básica',
    'Experiência Média',
    'Experiência Alta',
    'Sem Experiência',
  ];
  @ViewChild('modal') modal: TemplateRef<any>;

  constructor(
    private dialog: MatDialog,
    private profileService: ProfileService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.totalTabSearch = 4;
    this.tabSelectedSearch = 0;
  }

  ngAfterViewInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.profileService.find(this.account.id).subscribe(profile => {
        this.profile = profile.body;

        if (this.profile.status !== StatusProfile.ATUAL) {
          (async () => {
            await delay(4000);
            this.openModal();
          })();
        }
      });
    });
  }

  openModal(): void {
    const modal = this.dialog.open(this.modal, {
      width: '80%',
      data: { profile: this.profile, dataNascimento: this.dataNascimento },
      disableClose: true,
      closeOnNavigation: true,
    });

    modal.afterClosed().subscribe(result => {
      if (result) {
        this.dataNascimento = result.dataNascimento;
        this.profile = result.profile;
        this.save();
      }
    });
  }

  selectedNextTabSearch(): void {
    this.tabSelectedSearch = (this.tabSelectedSearch + 1) % this.totalTabSearch;
  }
  selectedPreviesTabSearch(): void {
    this.tabSelectedSearch = (this.tabSelectedSearch - 1) % this.totalTabSearch;
  }

  save() {
    this.profile.status = StatusProfile.ATUAL;
    this.profile.ultimaModificacao = moment(moment.now());

    this.profile.age =
      this.profile.ultimaModificacao.year() - this.dataNascimento.getFullYear();

    this.profileService.update(this.profile).subscribe(() => {
      location.reload();
    });
  }

  onDate(event: any): void {
    this.dataNascimento = event;
  }

  onLanguage(event: any): void {
    this.profile.language = event.value;
  }

  onGender(event: any): void {
    this.profile.gender = event.value;
  }

  onExp(event: any): void {
    this.profile.experienceLevel = event.value;
  }
}
