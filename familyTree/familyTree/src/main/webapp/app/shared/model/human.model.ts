export interface IHuman {
  id?: number;
  surname?: string;
  name?: string;
  patronymic?: string;
  humanInfo?: number;
}

export const defaultValue: Readonly<IHuman> = {};
