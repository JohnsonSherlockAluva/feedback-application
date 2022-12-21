import { IDesignation, NewDesignation } from './designation.model';

export const sampleWithRequiredData: IDesignation = {
  id: 18278,
};

export const sampleWithPartialData: IDesignation = {
  id: 88078,
  designationName: 'salmon',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
};

export const sampleWithFullData: IDesignation = {
  id: 22515,
  designationName: 'Officer solutions',
  discription: '../fake-data/blob/hipster.txt',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: false,
};

export const sampleWithNewData: NewDesignation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
