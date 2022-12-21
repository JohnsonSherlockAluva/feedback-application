import { IGroups, NewGroups } from './groups.model';

export const sampleWithRequiredData: IGroups = {
  id: 3292,
};

export const sampleWithPartialData: IGroups = {
  id: 15049,
  groupName: 'Senegal program SDD',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: false,
};

export const sampleWithFullData: IGroups = {
  id: 1490,
  groupName: 'Orchestrator Steel index',
  discription: '../fake-data/blob/hipster.txt',
  picture: '../fake-data/blob/hipster.png',
  pictureContentType: 'unknown',
  status: false,
};

export const sampleWithNewData: NewGroups = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
