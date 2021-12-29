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
import { IPost, Post } from 'app/shared/model/post.model';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { PostService } from '../post/post.service';
import { FeedDetailComponent } from './feed-detail.component';
import { FeedComponent } from './feed.component';

@Injectable({ providedIn: 'root' })
export class FeedResolve implements Resolve<IPost> {
  constructor(private service: PostService, private router: Router) {}

  resolve(
    route: ActivatedRouteSnapshot
  ): Observable<IPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((post: HttpResponse<Post>) => {
          if (post.body) {
            return of(post.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Post());
  }
}

export const feedRoute: Routes = [
  {
    path: 'feed',
    component: FeedComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Inicio - Interface Adaptativa',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'feed/:id',
    component: FeedDetailComponent,
    resolve: {
      post: FeedResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Detalhes - Interface Adaptativa',
    },
    canActivate: [UserRouteAccessService],
  },
];
