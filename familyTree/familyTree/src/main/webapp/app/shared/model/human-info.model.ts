import dayjs from 'dayjs';

export interface IHumanInfo {
  id?: number;
  dateOfBirth?: string | null;
  dateOfDeath?: string | null;
  places?: number;
  relatives?: number;
}

export const defaultValue: Readonly<IHumanInfo> = {};
