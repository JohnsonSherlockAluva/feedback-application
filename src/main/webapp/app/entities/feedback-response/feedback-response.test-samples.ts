import { IFeedbackResponse, NewFeedbackResponse } from './feedback-response.model';

export const sampleWithRequiredData: IFeedbackResponse = {
  id: 51254,
};

export const sampleWithPartialData: IFeedbackResponse = {
  id: 60020,
  status: false,
};

export const sampleWithFullData: IFeedbackResponse = {
  id: 37230,
  response: 'next-generation Regional',
  discription: '../fake-data/blob/hipster.txt',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: true,
};

export const sampleWithNewData: NewFeedbackResponse = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
