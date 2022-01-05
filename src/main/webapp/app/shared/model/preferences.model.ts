import { StatusPreferences } from 'app/shared/model/enumerations/status-preferences.model';
import { ExperienceLevelMode } from 'app/shared/model/enumerations/experience-level-mode.model';
import { FontMode } from 'app/shared/model/enumerations/font-mode.model';

export interface IPreferences {
  id?: number;
  darkMode?: StatusPreferences;
  experienceLevelMode?: ExperienceLevelMode;
  fontMode?: FontMode;
  contrastMode?: boolean;
  colorVisionMode?: boolean;
  userLogin?: string;
  userId?: number;
}

export class Preferences implements IPreferences {
  constructor(
    public id?: number,
    public darkMode?: StatusPreferences,
    public experienceLevelMode?: ExperienceLevelMode,
    public fontMode?: FontMode,
    public contrastMode?: boolean,
    public colorVisionMode?: boolean,
    public userLogin?: string,
    public userId?: number
  ) {
    this.contrastMode = this.contrastMode || false;
    this.colorVisionMode = this.colorVisionMode || false;
  }
}
