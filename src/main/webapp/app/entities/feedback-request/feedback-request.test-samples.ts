import dayjs from 'dayjs/esm';

import { IFeedbackRequest, NewFeedbackRequest } from './feedback-request.model';

export const sampleWithRequiredData: IFeedbackRequest = {
  id: 89225,
};

export const sampleWithPartialData: IFeedbackRequest = {
  id: 21878,
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  endDate: dayjs('2022-12-21T02:05'),
  createdBy: 96230,
};

export const sampleWithFullData: IFeedbackRequest = {
  id: 57876,
  subject: 'Cross-platform Director',
  discription: '../fake-data/blob/hipster.txt',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: true,
  startDate: dayjs('2022-12-21T11:43'),
  endDate: dayjs('2022-12-21T01:42'),
  createdBy: 60915,
};

export const sampleWithNewData: NewFeedbackRequest = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
