import { IFeedbackRequest } from 'app/entities/feedback-request/feedback-request.model';

export interface IFeedbackAbout {
  id: number;
  feedbackabout?: string | null;
  discription?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  status?: boolean | null;
  feedbackRequests?: Pick<IFeedbackRequest, 'id'>[] | null;
}

export type NewFeedbackAbout = Omit<IFeedbackAbout, 'id'> & { id: null };
