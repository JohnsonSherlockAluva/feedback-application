import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { IFeedbackAbout } from 'app/entities/feedback-about/feedback-about.model';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';
import { IGroups } from 'app/entities/groups/groups.model';

export interface IFeedbackRequest {
  id: number;
  subject?: string | null;
  discription?: string | null;
  picture?: string | null;
  pictureContentType?: string | null;
  status?: boolean | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  createdBy?: number | null;
  feedbackAboutUsers?: Pick<IApplicationUser, 'id'>[] | null;
  feedbackAbouts?: Pick<IFeedbackAbout, 'id'>[] | null;
  feedbackResponses?: Pick<IFeedbackResponse, 'id'>[] | null;
  feedbackToUsers?: Pick<IApplicationUser, 'id'>[] | null;
  groups?: Pick<IGroups, 'id'>[] | null;
}

export type NewFeedbackRequest = Omit<IFeedbackRequest, 'id'> & { id: null };
