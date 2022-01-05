import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IProfile } from 'app/shared/model/profile.model';
import { createRequestOption } from 'app/shared/util/request-util';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IProfile>;
type EntityArrayResponseType = HttpResponse<IProfile[]>;

@Injectable({ providedIn: 'root' })
export class ProfileService {
  public resourceUrl = SERVER_API_URL + 'api/profiles';

  constructor(protected http: HttpClient) {}

  create(profile: IProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profile);
    return this.http
      .post<IProfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(profile: IProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profile);
    return this.http
      .put<IProfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProfile[]>(this.resourceUrl, {
        params: options,
        observe: 'response',
      })
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

  friends(
    profileId: number,
    userId: number
  ): Observable<HttpResponse<IProfile>> {
    return this.http.get<IProfile>(
      `${this.resourceUrl}/friends/${profileId}/${userId}`,
      {
        observe: 'response',
      }
    );
  }

  protected convertDateFromClient(profile: IProfile): IProfile {
    const copy: IProfile = Object.assign({}, profile, {
      ultimaModificacao:
        profile.ultimaModificacao && profile.ultimaModificacao.isValid()
          ? profile.ultimaModificacao.toJSON()
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.ultimaModificacao = res.body.ultimaModificacao
        ? moment(res.body.ultimaModificacao)
        : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(
    res: EntityArrayResponseType
  ): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((profile: IProfile) => {
        profile.ultimaModificacao = profile.ultimaModificacao
          ? moment(profile.ultimaModificacao)
          : undefined;
      });
    }
    return res;
  }
}
