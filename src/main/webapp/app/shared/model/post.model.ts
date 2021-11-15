import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { TipoPost } from 'app/shared/model/enumerations/tipo-post.model';

export interface IPost {
  id?: number;
  body?: string;
  date?: Moment;
  active?: boolean;
  likes?: number;
  link?: string;
  tipoPost?: TipoPost;
  userLogin?: string;
  userId?: number;
  likeDes?: IUser[];
  comentarioDeId?: number;
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public body?: string,
    public date?: Moment,
    public active?: boolean,
    public likes?: number,
    public link?: string,
    public tipoPost?: TipoPost,
    public userLogin?: string,
    public userId?: number,
    public likeDes?: IUser[],
    public comentarioDeId?: number
  ) {
    this.active = this.active || false;
  }
}
