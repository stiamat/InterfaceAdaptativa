import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IUser } from 'app/core/user/user.model';
import { IPost } from 'app/shared/model/post.model';
import { createRequestOption } from 'app/shared/util/request-util';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IPost>;
type EntityArrayResponseType = HttpResponse<IPost[]>;

@Injectable({ providedIn: 'root' })
export class PostService {
  public resourceUrl = SERVER_API_URL + 'api/posts';

  constructor(protected http: HttpClient) {}

  create(post: IPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(post);
    return this.http
      .post<IPost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(post: IPost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(post);
    return this.http
      .put<IPost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPost>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPost[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(
        map((res: EntityArrayResponseType) =>
          this.convertDateArrayFromServer(res)
        )
      );
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {
      observe: 'response',
    });
  }

  curti(postId: number, userId: number): Observable<HttpResponse<IUser[]>> {
    return this.http.get<IUser[]>(
      `${this.resourceUrl}/curti/${postId}/${userId}`,
      {
        observe: 'response',
      }
    );
  }

  respostas(postId: number): Observable<HttpResponse<IPost[]>> {
    return this.http.get<IPost[]>(`${this.resourceUrl}/answer/${postId}`, {
      observe: 'response',
    });
  }

  respostasUser(userId: number): Observable<HttpResponse<IPost[]>> {
    return this.http.get<IPost[]>(`${this.resourceUrl}/answer/user/${userId}`, {
      observe: 'response',
    });
  }

  searchLogin(login: string): Observable<HttpResponse<IPost[]>> {
    return this.http.get<IPost[]>(`${this.resourceUrl}/search/${login}`, {
      observe: 'response',
    });
  }

  searchDestaques(): Observable<HttpResponse<Object[]>> {
    return this.http.get<IPost[]>(`${this.resourceUrl}/search/destaques`, {
      observe: 'response',
    });
  }

  protected convertDateFromClient(post: IPost): IPost {
    const copy: IPost = Object.assign({}, post, {
      date: post.date && post.date.isValid() ? post.date.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(
    res: EntityArrayResponseType
  ): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((post: IPost) => {
        post.date = post.date ? moment(post.date) : undefined;
      });
    }
    return res;
  }
}
