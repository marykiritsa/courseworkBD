export interface IRelatives {
  id?: number;
  surname?: string;
  name?: string;
  patronymic?: string;
  typeOfRelationship?: number;
}

export const defaultValue: Readonly<IRelatives> = {};
