import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Authority } from 'app/shared/constants/authority.constants';
import { FeedComponent } from './feed.component';


// @Injectable({ providedIn: 'root' })
// export class FeedResolve implements Resolve<IPost> {
//   constructor(private service: PostService, private router: Router) {}

//   resolve(route: ActivatedRouteSnapshot): Observable<IPost> | Observable<never> {
//     const id = route.params['id'];
//     if (id) {
//       return this.service.find(id).pipe(
//         flatMap((post: HttpResponse<Post>) => {
//           if (post.body) {
//             return of(post.body);
//           } else {
//             this.router.navigate(['404']);
//             return EMPTY;
//           }
//         })
//       );
//     }
//     return of(new Post());
//   }
// }

export const feedRoute: Routes = [
  {
    path: 'feed',
    component: FeedComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Feeds',
    },
    canActivate: [UserRouteAccessService],
  },
];
