import {
  AfterViewInit,
  Component,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
} from '@angular/material/core';
import { MatDialog } from '@angular/material/dialog';
import { AccountService } from 'app/core/auth/account.service';
import { ExperienceLevelMode } from 'app/shared/model/enumerations/experience-level-mode.model';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';
import { IProfile } from 'app/shared/model/profile.model';
import * as moment from 'moment';
import Swal from 'sweetalert2';
import { ProfileService } from '../profile/profile.service';

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
interface Lang {
  value: string;
  viewValue: string;
}

export const MY_FORMATS = {
  parse: {
    dateInput: 'D/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.scss'],
  providers: [
    { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'fr' },
  ],
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
    private accountService: AccountService,
    private _adapter: DateAdapter<any>
  ) {}

  ngOnInit(): void {
    this.totalTabSearch = 4;
    this.tabSelectedSearch = 0;
    this._adapter.setLocale('pt-br');
  }

  ngAfterViewInit(): void {
    (async () => {
      await delay(4000);
      this.accountService.getAuthenticationState().subscribe(account => {
        this.account = account;

        this.profileService.find(this.account.id).subscribe(profile => {
          this.profile = profile.body;
          this.cleanProfile();

          if (
            this.profile.status === StatusProfile.NOVO ||
            this.profile.status === null
          ) {
            this.openModal();
          }
        });
      });
    })();
  }

  cleanProfile() {
    this.profile.auditoryDisabilities = null;
    this.profile.blindness = null;
    this.profile.colorVision = null;
    this.profile.contrastSensitivity = null;
    this.profile.fildOfVision = null;
    this.profile.lightSensitivity = null;
    this.profile.visualAcuity = null;
  }

  openModal(): void {
    this.dialog.closeAll();
    let modal = null;
    if (window.innerWidth < 1024) {
      modal = this.dialog.open(this.modal, {
        width: '100%',
        data: { profile: this.profile, dataNascimento: this.dataNascimento },
        disableClose: true,
        closeOnNavigation: true,
      });
    } else {
      modal = this.dialog.open(this.modal, {
        width: '60%',
        data: { profile: this.profile, dataNascimento: this.dataNascimento },
        disableClose: true,
        closeOnNavigation: true,
      });
    }

    modal.afterClosed().subscribe(result => {
      if (result) {
        this.dataNascimento = result.dataNascimento;
        this.profile = result.profile;
        this.save();
      } else {
        this.close();
      }
    });
  }
  close() {
    this.profile.status = StatusProfile.LEGADO;
    this.profile.ultimaModificacao = moment(moment.now());
    this.profile.age = 0;

    this.profileService.update(this.profile).subscribe(() => {
      Swal.fire({
        icon: 'success',
        title: 'Está tudo bem!',
        confirmButtonColor: '#536dfe',
        text:
          'Caso queira realizar a pesquisa depois, basta apenas ir nas configurações e redefinir o perfil.',
      });
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
    console.warn(this.profile.age);

    this.profileService.update(this.profile).subscribe(() => {
      Swal.fire({
        icon: 'success',
        title: 'Espere por ai!',
        text:
          'Estamos analisando as modificações necessárias para sua interface!',
        confirmButtonColor: '#536dfe',
        showConfirmButton: false,
        timer: 5000,
      }).then(() => {
        location.reload();
      });
    });
  }

  onDate(event: any): void {
    this.dataNascimento = event;
  }

  onLanguage(event: any): void {
    this.profile.language = event.value;
    console.warn(event.value);
  }

  onGender(event: any): void {
    this.profile.gender = event.value;
  }

  onExp(event: any): void {
    if (
      event.value === 'Experiência Básica' ||
      event.value === 'Sem Experiência'
    ) {
      this.profile.experienceLevel = ExperienceLevelMode.BASICMODE;
    } else if (event.value === 'Experiência Média') {
      this.profile.experienceLevel = ExperienceLevelMode.AVERAGEMODE;
    } else {
      this.profile.experienceLevel = ExperienceLevelMode.HIGHMODE;
    }
  }
}
