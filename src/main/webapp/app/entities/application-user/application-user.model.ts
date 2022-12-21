import { IUser } from 'app/entities/user/user.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { IFeedbackRequest } from 'app/entities/feedback-request/feedback-request.model';
import { IGroups } from 'app/entities/groups/groups.model';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';

export interface IApplicationUser {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
  emailid?: string | null;
  phonenumber?: string | null;
  location?: string | null;
  profilepic?: string | null;
  profilepicContentType?: string | null;
  status?: boolean | null;
  internalUser?: Pick<IUser, 'id'> | null;
  designations?: Pick<IDesignation, 'id'>[] | null;
  feedbackRequest1s?: Pick<IFeedbackRequest, 'id'>[] | null;
  groups?: Pick<IGroups, 'id'>[] | null;
  feedbackRequests?: Pick<IFeedbackRequest, 'id'>[] | null;
  feedbackResponses?: Pick<IFeedbackResponse, 'id'>[] | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
