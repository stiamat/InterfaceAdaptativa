import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import {
  ActivatedRouteSnapshot,
  NavigationEnd,
  NavigationError,
  Router,
} from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { PreferencesService } from 'app/entities/preferences/preferences.service';
import { ProfileService } from 'app/entities/profile/profile.service';
import { FontMode } from 'app/shared/model/enumerations/font-mode.model';
import { StatusPreferences } from 'app/shared/model/enumerations/status-preferences.model';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';
import * as moment from 'moment';

function delay(ms: number) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {
  account: any = null;
  erro = false;
  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private profileService: ProfileService,
    private preferencesService: PreferencesService,
    private router: Router
  ) {}

  ngOnInit(): void {
    (async () => {
      while (this.account === null) {
        this.accountService.identity().subscribe(
          suc => {
            this.account = suc;

            if (this.account?.id) this.adaptative();
          },
          () => {
            this.erro = true;
          }
        );
        await delay(10000);
      }
    })();
    // try to log in automatically

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
      if (event instanceof NavigationError && event.error.status === 404) {
        this.router.navigate(['/404']);
      }
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string =
      routeSnapshot.data && routeSnapshot.data['pageTitle']
        ? routeSnapshot.data['pageTitle']
        : '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'InterfaceAdaptativa';
    }
    this.titleService.setTitle(pageTitle);
  }

  adaptative() {
    this.profileService.find(this.account.id).subscribe(p => {
      if (p.body.status === StatusProfile.ATUAL) {
        const element = document.querySelector('.body_adaptative');
        if (!element) return;
        this.preferencesService.find(this.account.id).subscribe(pref => {
          console.warn(pref.body);
          const preferences = pref.body;

          if (preferences.darkMode === StatusPreferences.TRUE) {
            element.classList.add('dark_mode');
          } else if (
            preferences.darkMode === StatusPreferences.AUTO &&
            moment(moment.now()).get('hour') >= 18
          ) {
            element.classList.add('dark_mode');
          }

          if (preferences.fontMode === FontMode.INCREASE) {
            element.classList.add('font_grande_1');
          } else if (preferences.fontMode === FontMode.DECREASE) {
            element.classList.add('font_pequena_1');
          }
        });
        // element.classList.add('dark_mode');
      }
    });
  }
}
