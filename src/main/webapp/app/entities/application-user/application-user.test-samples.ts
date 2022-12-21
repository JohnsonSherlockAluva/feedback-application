import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 60827,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 29423,
  lastname: 'mobile Plastic EXE',
  emailid: 'Consultant',
  phonenumber: 'Rupee Israel',
  location: 'Optimized Chips',
  status: true,
};

export const sampleWithFullData: IApplicationUser = {
  id: 90491,
  firstname: 'transform sexy deposit',
  lastname: 'Usability',
  emailid: 'Accountability',
  phonenumber: 'protocol reintermediate Bacon',
  location: 'orchestrate object-oriented models',
  profilepic: '../fake-data/blob/hipster.png',
  profilepicContentType: 'unknown',
  status: true,
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
