import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface IDesignation {
  id: number;
  designationName?: string | null;
  discription?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  status?: boolean | null;
  applicationUsers?: Pick<IApplicationUser, 'id'>[] | null;
}

export type NewDesignation = Omit<IDesignation, 'id'> & { id: null };
