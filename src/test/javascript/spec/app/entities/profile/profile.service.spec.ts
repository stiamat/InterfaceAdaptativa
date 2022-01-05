import { TestBed, getTestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ProfileService } from 'app/entities/profile/profile.service';
import { IProfile, Profile } from 'app/shared/model/profile.model';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';

describe('Service Tests', () => {
  describe('Profile Service', () => {
    let injector: TestBed;
    let service: ProfileService;
    let httpMock: HttpTestingController;
    let elemDefault: IProfile;
    let expectedResult: IProfile | IProfile[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ProfileService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Profile(
        0,
        StatusProfile.NOVO,
        currentDate,
        0,
        0,
        false,
        false,
        false,
        false,
        false,
        false,
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            ultimaModificacao: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Profile', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            ultimaModificacao: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ultimaModificacao: currentDate,
          },
          returnedFromService
        );

        service
          .create(new Profile())
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Profile', () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            ultimaModificacao: currentDate.format(DATE_TIME_FORMAT),
            numModificacao: 1,
            age: 1,
            auditoryDisabilities: true,
            blindness: true,
            colorVision: true,
            contrastSensitivity: true,
            fildOfVision: true,
            lightSensitivity: true,
            visualAcuity: true,
            education: 'BBBBBB',
            experienceLevel: 'BBBBBB',
            gender: 'BBBBBB',
            language: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ultimaModificacao: currentDate,
          },
          returnedFromService
        );

        service
          .update(expected)
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Profile', () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            ultimaModificacao: currentDate.format(DATE_TIME_FORMAT),
            numModificacao: 1,
            age: 1,
            auditoryDisabilities: true,
            blindness: true,
            colorVision: true,
            contrastSensitivity: true,
            fildOfVision: true,
            lightSensitivity: true,
            visualAcuity: true,
            education: 'BBBBBB',
            experienceLevel: 'BBBBBB',
            gender: 'BBBBBB',
            language: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            ultimaModificacao: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Profile', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
