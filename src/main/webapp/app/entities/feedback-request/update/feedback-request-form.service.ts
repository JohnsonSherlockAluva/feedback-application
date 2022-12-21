import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeedbackRequest, NewFeedbackRequest } from '../feedback-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackRequest for edit and NewFeedbackRequestFormGroupInput for create.
 */
type FeedbackRequestFormGroupInput = IFeedbackRequest | PartialWithRequiredKeyOf<NewFeedbackRequest>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeedbackRequest | NewFeedbackRequest> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

type FeedbackRequestFormRawValue = FormValueOf<IFeedbackRequest>;

type NewFeedbackRequestFormRawValue = FormValueOf<NewFeedbackRequest>;

type FeedbackRequestFormDefaults = Pick<
  NewFeedbackRequest,
  'id' | 'status' | 'startDate' | 'endDate' | 'feedbackAboutUsers' | 'feedbackAbouts' | 'feedbackResponses' | 'feedbackToUsers' | 'groups'
>;

type FeedbackRequestFormGroupContent = {
  id: FormControl<FeedbackRequestFormRawValue['id'] | NewFeedbackRequest['id']>;
  subject: FormControl<FeedbackRequestFormRawValue['subject']>;
  discription: FormControl<FeedbackRequestFormRawValue['discription']>;
  picture: FormControl<FeedbackRequestFormRawValue['picture']>;
  pictureContentType: FormControl<FeedbackRequestFormRawValue['pictureContentType']>;
  status: FormControl<FeedbackRequestFormRawValue['status']>;
  startDate: FormControl<FeedbackRequestFormRawValue['startDate']>;
  endDate: FormControl<FeedbackRequestFormRawValue['endDate']>;
  createdBy: FormControl<FeedbackRequestFormRawValue['createdBy']>;
  feedbackAboutUsers: FormControl<FeedbackRequestFormRawValue['feedbackAboutUsers']>;
  feedbackAbouts: FormControl<FeedbackRequestFormRawValue['feedbackAbouts']>;
  feedbackResponses: FormControl<FeedbackRequestFormRawValue['feedbackResponses']>;
  feedbackToUsers: FormControl<FeedbackRequestFormRawValue['feedbackToUsers']>;
  groups: FormControl<FeedbackRequestFormRawValue['groups']>;
};

export type FeedbackRequestFormGroup = FormGroup<FeedbackRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackRequestFormService {
  createFeedbackRequestFormGroup(feedbackRequest: FeedbackRequestFormGroupInput = { id: null }): FeedbackRequestFormGroup {
    const feedbackRequestRawValue = this.convertFeedbackRequestToFeedbackRequestRawValue({
      ...this.getFormDefaults(),
      ...feedbackRequest,
    });
    return new FormGroup<FeedbackRequestFormGroupContent>({
      id: new FormControl(
        { value: feedbackRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      subject: new FormControl(feedbackRequestRawValue.subject),
      discription: new FormControl(feedbackRequestRawValue.discription),
      picture: new FormControl(feedbackRequestRawValue.picture),
      pictureContentType: new FormControl(feedbackRequestRawValue.pictureContentType),
      status: new FormControl(feedbackRequestRawValue.status),
      startDate: new FormControl(feedbackRequestRawValue.startDate),
      endDate: new FormControl(feedbackRequestRawValue.endDate),
      createdBy: new FormControl(feedbackRequestRawValue.createdBy),
      feedbackAboutUsers: new FormControl(feedbackRequestRawValue.feedbackAboutUsers ?? []),
      feedbackAbouts: new FormControl(feedbackRequestRawValue.feedbackAbouts ?? []),
      feedbackResponses: new FormControl(feedbackRequestRawValue.feedbackResponses ?? []),
      feedbackToUsers: new FormControl(feedbackRequestRawValue.feedbackToUsers ?? []),
      groups: new FormControl(feedbackRequestRawValue.groups ?? []),
    });
  }

  getFeedbackRequest(form: FeedbackRequestFormGroup): IFeedbackRequest | NewFeedbackRequest {
    return this.convertFeedbackRequestRawValueToFeedbackRequest(
      form.getRawValue() as FeedbackRequestFormRawValue | NewFeedbackRequestFormRawValue
    );
  }

  resetForm(form: FeedbackRequestFormGroup, feedbackRequest: FeedbackRequestFormGroupInput): void {
    const feedbackRequestRawValue = this.convertFeedbackRequestToFeedbackRequestRawValue({ ...this.getFormDefaults(), ...feedbackRequest });
    form.reset(
      {
        ...feedbackRequestRawValue,
        id: { value: feedbackRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackRequestFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      status: false,
      startDate: currentTime,
      endDate: currentTime,
      feedbackAboutUsers: [],
      feedbackAbouts: [],
      feedbackResponses: [],
      feedbackToUsers: [],
      groups: [],
    };
  }

  private convertFeedbackRequestRawValueToFeedbackRequest(
    rawFeedbackRequest: FeedbackRequestFormRawValue | NewFeedbackRequestFormRawValue
  ): IFeedbackRequest | NewFeedbackRequest {
    return {
      ...rawFeedbackRequest,
      startDate: dayjs(rawFeedbackRequest.startDate, DATE_TIME_FORMAT),
      endDate: dayjs(rawFeedbackRequest.endDate, DATE_TIME_FORMAT),
    };
  }

  private convertFeedbackRequestToFeedbackRequestRawValue(
    feedbackRequest: IFeedbackRequest | (Partial<NewFeedbackRequest> & FeedbackRequestFormDefaults)
  ): FeedbackRequestFormRawValue | PartialWithRequiredKeyOf<NewFeedbackRequestFormRawValue> {
    return {
      ...feedbackRequest,
      startDate: feedbackRequest.startDate ? feedbackRequest.startDate.format(DATE_TIME_FORMAT) : undefined,
      endDate: feedbackRequest.endDate ? feedbackRequest.endDate.format(DATE_TIME_FORMAT) : undefined,
      feedbackAboutUsers: feedbackRequest.feedbackAboutUsers ?? [],
      feedbackAbouts: feedbackRequest.feedbackAbouts ?? [],
      feedbackResponses: feedbackRequest.feedbackResponses ?? [],
      feedbackToUsers: feedbackRequest.feedbackToUsers ?? [],
      groups: feedbackRequest.groups ?? [],
    };
  }
}
