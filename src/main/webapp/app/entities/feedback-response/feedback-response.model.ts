import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IFeedbackRequest } from 'app/entities/feedback-request/feedback-request.model';

export interface IFeedbackResponse {
  id: number;
  response?: string | null;
  discription?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  status?: boolean | null;
  applicationUsers?: Pick<IApplicationUser, 'id'>[] | null;
  feedbackRequests?: Pick<IFeedbackRequest, 'id'>[] | null;
}

export type NewFeedbackResponse = Omit<IFeedbackResponse, 'id'> & { id: null };
