import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-about.test-samples';

import { FeedbackAboutFormService } from './feedback-about-form.service';

describe('FeedbackAbout Form Service', () => {
  let service: FeedbackAboutFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackAboutFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackAboutFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackAboutFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbackabout: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            feedbackRequests: expect.any(Object),
          })
        );
      });

      it('passing IFeedbackAbout should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackAboutFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbackabout: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            feedbackRequests: expect.any(Object),
          })
        );
      });
    });

    describe('getFeedbackAbout', () => {
      it('should return NewFeedbackAbout for default FeedbackAbout initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFeedbackAboutFormGroup(sampleWithNewData);

        const feedbackAbout = service.getFeedbackAbout(formGroup) as any;

        expect(feedbackAbout).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackAbout for empty FeedbackAbout initial value', () => {
        const formGroup = service.createFeedbackAboutFormGroup();

        const feedbackAbout = service.getFeedbackAbout(formGroup) as any;

        expect(feedbackAbout).toMatchObject({});
      });

      it('should return IFeedbackAbout', () => {
        const formGroup = service.createFeedbackAboutFormGroup(sampleWithRequiredData);

        const feedbackAbout = service.getFeedbackAbout(formGroup) as any;

        expect(feedbackAbout).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackAbout should not enable id FormControl', () => {
        const formGroup = service.createFeedbackAboutFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackAbout should disable id FormControl', () => {
        const formGroup = service.createFeedbackAboutFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
