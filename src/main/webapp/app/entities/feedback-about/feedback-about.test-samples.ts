import { IFeedbackAbout, NewFeedbackAbout } from './feedback-about.model';

export const sampleWithRequiredData: IFeedbackAbout = {
  id: 30551,
};

export const sampleWithPartialData: IFeedbackAbout = {
  id: 48182,
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: true,
};

export const sampleWithFullData: IFeedbackAbout = {
  id: 57158,
  feedbackabout: '24/7 Rustic',
  discription: '../fake-data/blob/hipster.txt',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: false,
};

export const sampleWithNewData: NewFeedbackAbout = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
