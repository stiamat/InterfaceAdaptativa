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
  profile: IProfile;
  @ViewChild('modal') modal: TemplateRef<any>;

  constructor(
    private dialog: MatDialog,
    private profileService: ProfileService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.totalTabSearch = 3;
    this.tabSelectedSearch = 0;
  }

  ngAfterViewInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.profileService.find(this.account.id).subscribe(profile => {
        this.profile = profile.body;
        if (this.profile.status === StatusProfile.NOVO) {
          this.openModal();
        }
      });
    });
  }

  openModal(): void {
    const modal = this.dialog.open(this.modal, {
      width: '80%',
      disableClose: true,
      closeOnNavigation: true,
    });

    modal.afterClosed().subscribe(result => {
      console.warn(result);
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
    this.profileService.update(this.profile).subscribe(s => {
      console.warn(s);
    });
  }
}
