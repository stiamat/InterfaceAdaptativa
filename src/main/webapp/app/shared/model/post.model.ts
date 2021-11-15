import { Moment } from 'moment';

export interface IPost {
  id?: number;
  body?: string;
  date?: Moment;
  active?: boolean;
  likes?: number;
  userLogin?: string;
  userId?: number;
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public body?: string,
    public date?: Moment,
    public active?: boolean,
    public likes?: number,
    public userLogin?: string,
    public userId?: number
  ) {
    this.active = this.active || false;
  }
}
