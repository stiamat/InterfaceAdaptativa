import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { PerfilComponent } from './perfil.component';

export const perfilRoute: Routes = [
  {
    path: 'perfil',
    component: PerfilComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Perfil - Interface Adaptativa',
    },
    canActivate: [UserRouteAccessService],
  },
];
