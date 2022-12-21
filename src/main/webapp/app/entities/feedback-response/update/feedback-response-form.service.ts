import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackResponse, NewFeedbackResponse } from '../feedback-response.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackResponse for edit and NewFeedbackResponseFormGroupInput for create.
 */
type FeedbackResponseFormGroupInput = IFeedbackResponse | PartialWithRequiredKeyOf<NewFeedbackResponse>;

type FeedbackResponseFormDefaults = Pick<NewFeedbackResponse, 'id' | 'status' | 'applicationUsers' | 'feedbackRequests'>;

type FeedbackResponseFormGroupContent = {
  id: FormControl<IFeedbackResponse['id'] | NewFeedbackResponse['id']>;
  response: FormControl<IFeedbackResponse['response']>;
  discription: FormControl<IFeedbackResponse['discription']>;
  picture: FormControl<IFeedbackResponse['picture']>;
  pictureContentType: FormControl<IFeedbackResponse['pictureContentType']>;
  status: FormControl<IFeedbackResponse['status']>;
  applicationUsers: FormControl<IFeedbackResponse['applicationUsers']>;
  feedbackRequests: FormControl<IFeedbackResponse['feedbackRequests']>;
};

export type FeedbackResponseFormGroup = FormGroup<FeedbackResponseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackResponseFormService {
  createFeedbackResponseFormGroup(feedbackResponse: FeedbackResponseFormGroupInput = { id: null }): FeedbackResponseFormGroup {
    const feedbackResponseRawValue = {
      ...this.getFormDefaults(),
      ...feedbackResponse,
    };
    return new FormGroup<FeedbackResponseFormGroupContent>({
      id: new FormControl(
        { value: feedbackResponseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      response: new FormControl(feedbackResponseRawValue.response),
      discription: new FormControl(feedbackResponseRawValue.discription),
      picture: new FormControl(feedbackResponseRawValue.picture),
      pictureContentType: new FormControl(feedbackResponseRawValue.pictureContentType),
      status: new FormControl(feedbackResponseRawValue.status),
      applicationUsers: new FormControl(feedbackResponseRawValue.applicationUsers ?? []),
      feedbackRequests: new FormControl(feedbackResponseRawValue.feedbackRequests ?? []),
    });
  }

  getFeedbackResponse(form: FeedbackResponseFormGroup): IFeedbackResponse | NewFeedbackResponse {
    return form.getRawValue() as IFeedbackResponse | NewFeedbackResponse;
  }

  resetForm(form: FeedbackResponseFormGroup, feedbackResponse: FeedbackResponseFormGroupInput): void {
    const feedbackResponseRawValue = { ...this.getFormDefaults(), ...feedbackResponse };
    form.reset(
      {
        ...feedbackResponseRawValue,
        id: { value: feedbackResponseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackResponseFormDefaults {
    return {
      id: null,
      status: false,
      applicationUsers: [],
      feedbackRequests: [],
    };
  }
}
