import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';

export interface IProfile {
  id?: number;
  status?: StatusProfile;
  ultimaModificacao?: Moment;
  numModificacao?: number;
  age?: number;
  auditoryDisabilities?: boolean;
  blindness?: boolean;
  colorVision?: boolean;
  contrastSensitivity?: boolean;
  fildOfVision?: boolean;
  lightSensitivity?: boolean;
  visualAcuity?: boolean;
  education?: string;
  experienceLevel?: string;
  gender?: string;
  language?: string;
  userLogin?: string;
  userId?: number;
  listFriends?: IUser[];
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public status?: StatusProfile,
    public ultimaModificacao?: Moment,
    public numModificacao?: number,
    public age?: number,
    public auditoryDisabilities?: boolean,
    public blindness?: boolean,
    public colorVision?: boolean,
    public contrastSensitivity?: boolean,
    public fildOfVision?: boolean,
    public lightSensitivity?: boolean,
    public visualAcuity?: boolean,
    public education?: string,
    public experienceLevel?: string,
    public gender?: string,
    public language?: string,
    public userLogin?: string,
    public userId?: number,
    public listFriends?: IUser[]
  ) {
    this.auditoryDisabilities = this.auditoryDisabilities || false;
    this.blindness = this.blindness || false;
    this.colorVision = this.colorVision || false;
    this.contrastSensitivity = this.contrastSensitivity || false;
    this.fildOfVision = this.fildOfVision || false;
    this.lightSensitivity = this.lightSensitivity || false;
    this.visualAcuity = this.visualAcuity || false;
  }
}
