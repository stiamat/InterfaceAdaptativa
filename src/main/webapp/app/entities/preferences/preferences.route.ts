import { HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
  Routes,
} from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { IPreferences, Preferences } from 'app/shared/model/preferences.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PreferencesUpdateComponent } from './preferences-update.component';
import { PreferencesService } from './preferences.service';

@Injectable({ providedIn: 'root' })
export class PreferencesResolve implements Resolve<IPreferences> {
  constructor(private service: PreferencesService, private router: Router) {}

  resolve(
    route: ActivatedRouteSnapshot
  ): Observable<IPreferences> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((preferences: HttpResponse<Preferences>) => {
          if (preferences.body) {
            return of(preferences.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Preferences());
  }
}

export const preferencesRoute: Routes = [
  {
    path: 'config/:id/edit',
    component: PreferencesUpdateComponent,
    resolve: {
      preferences: PreferencesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Preferences',
    },
    canActivate: [UserRouteAccessService],
  },
];
