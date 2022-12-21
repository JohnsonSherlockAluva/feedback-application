import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-request.test-samples';

import { FeedbackRequestFormService } from './feedback-request-form.service';

describe('FeedbackRequest Form Service', () => {
  let service: FeedbackRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackRequestFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subject: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            createdBy: expect.any(Object),
            feedbackAboutUsers: expect.any(Object),
            feedbackAbouts: expect.any(Object),
            feedbackResponses: expect.any(Object),
            feedbackToUsers: expect.any(Object),
            groups: expect.any(Object),
          })
        );
      });

      it('passing IFeedbackRequest should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            subject: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            createdBy: expect.any(Object),
            feedbackAboutUsers: expect.any(Object),
            feedbackAbouts: expect.any(Object),
            feedbackResponses: expect.any(Object),
            feedbackToUsers: expect.any(Object),
            groups: expect.any(Object),
          })
        );
      });
    });

    describe('getFeedbackRequest', () => {
      it('should return NewFeedbackRequest for default FeedbackRequest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFeedbackRequestFormGroup(sampleWithNewData);

        const feedbackRequest = service.getFeedbackRequest(formGroup) as any;

        expect(feedbackRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackRequest for empty FeedbackRequest initial value', () => {
        const formGroup = service.createFeedbackRequestFormGroup();

        const feedbackRequest = service.getFeedbackRequest(formGroup) as any;

        expect(feedbackRequest).toMatchObject({});
      });

      it('should return IFeedbackRequest', () => {
        const formGroup = service.createFeedbackRequestFormGroup(sampleWithRequiredData);

        const feedbackRequest = service.getFeedbackRequest(formGroup) as any;

        expect(feedbackRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackRequest should not enable id FormControl', () => {
        const formGroup = service.createFeedbackRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackRequest should disable id FormControl', () => {
        const formGroup = service.createFeedbackRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
