import { StatusPreferences } from 'app/shared/model/enumerations/status-preferences.model';

export interface IPreferences {
  id?: number;
  darkMode?: StatusPreferences;
  userLogin?: string;
  userId?: number;
}

export class Preferences implements IPreferences {
  constructor(
    public id?: number,
    public darkMode?: StatusPreferences,
    public userLogin?: string,
    public userId?: number
  ) {}
}
