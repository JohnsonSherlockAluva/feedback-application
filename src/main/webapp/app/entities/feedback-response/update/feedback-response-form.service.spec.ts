import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-response.test-samples';

import { FeedbackResponseFormService } from './feedback-response-form.service';

describe('FeedbackResponse Form Service', () => {
  let service: FeedbackResponseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackResponseFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackResponseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackResponseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            response: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            applicationUsers: expect.any(Object),
            feedbackRequests: expect.any(Object),
          })
        );
      });

      it('passing IFeedbackResponse should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackResponseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            response: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            applicationUsers: expect.any(Object),
            feedbackRequests: expect.any(Object),
          })
        );
      });
    });

    describe('getFeedbackResponse', () => {
      it('should return NewFeedbackResponse for default FeedbackResponse initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFeedbackResponseFormGroup(sampleWithNewData);

        const feedbackResponse = service.getFeedbackResponse(formGroup) as any;

        expect(feedbackResponse).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackResponse for empty FeedbackResponse initial value', () => {
        const formGroup = service.createFeedbackResponseFormGroup();

        const feedbackResponse = service.getFeedbackResponse(formGroup) as any;

        expect(feedbackResponse).toMatchObject({});
      });

      it('should return IFeedbackResponse', () => {
        const formGroup = service.createFeedbackResponseFormGroup(sampleWithRequiredData);

        const feedbackResponse = service.getFeedbackResponse(formGroup) as any;

        expect(feedbackResponse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackResponse should not enable id FormControl', () => {
        const formGroup = service.createFeedbackResponseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackResponse should disable id FormControl', () => {
        const formGroup = service.createFeedbackResponseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
