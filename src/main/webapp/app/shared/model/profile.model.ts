import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { StatusProfile } from 'app/shared/model/enumerations/status-profile.model';

export interface IProfile {
  id?: number;
  status?: StatusProfile;
  ultimaModificacao?: Moment;
  userLogin?: string;
  userId?: number;
  listFriends?: IUser[];
}

export class Profile implements IProfile {
  constructor(
    public id?: number,
    public status?: StatusProfile,
    public ultimaModificacao?: Moment,
    public userLogin?: string,
    public userId?: number,
    public listFriends?: IUser[]
  ) {}
}
