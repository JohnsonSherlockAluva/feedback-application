import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackAbout, NewFeedbackAbout } from '../feedback-about.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackAbout for edit and NewFeedbackAboutFormGroupInput for create.
 */
type FeedbackAboutFormGroupInput = IFeedbackAbout | PartialWithRequiredKeyOf<NewFeedbackAbout>;

type FeedbackAboutFormDefaults = Pick<NewFeedbackAbout, 'id' | 'status' | 'feedbackRequests'>;

type FeedbackAboutFormGroupContent = {
  id: FormControl<IFeedbackAbout['id'] | NewFeedbackAbout['id']>;
  feedbackabout: FormControl<IFeedbackAbout['feedbackabout']>;
  discription: FormControl<IFeedbackAbout['discription']>;
  picture: FormControl<IFeedbackAbout['picture']>;
  pictureContentType: FormControl<IFeedbackAbout['pictureContentType']>;
  status: FormControl<IFeedbackAbout['status']>;
  feedbackRequests: FormControl<IFeedbackAbout['feedbackRequests']>;
};

export type FeedbackAboutFormGroup = FormGroup<FeedbackAboutFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackAboutFormService {
  createFeedbackAboutFormGroup(feedbackAbout: FeedbackAboutFormGroupInput = { id: null }): FeedbackAboutFormGroup {
    const feedbackAboutRawValue = {
      ...this.getFormDefaults(),
      ...feedbackAbout,
    };
    return new FormGroup<FeedbackAboutFormGroupContent>({
      id: new FormControl(
        { value: feedbackAboutRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      feedbackabout: new FormControl(feedbackAboutRawValue.feedbackabout),
      discription: new FormControl(feedbackAboutRawValue.discription),
      picture: new FormControl(feedbackAboutRawValue.picture),
      pictureContentType: new FormControl(feedbackAboutRawValue.pictureContentType),
      status: new FormControl(feedbackAboutRawValue.status),
      feedbackRequests: new FormControl(feedbackAboutRawValue.feedbackRequests ?? []),
    });
  }

  getFeedbackAbout(form: FeedbackAboutFormGroup): IFeedbackAbout | NewFeedbackAbout {
    return form.getRawValue() as IFeedbackAbout | NewFeedbackAbout;
  }

  resetForm(form: FeedbackAboutFormGroup, feedbackAbout: FeedbackAboutFormGroupInput): void {
    const feedbackAboutRawValue = { ...this.getFormDefaults(), ...feedbackAbout };
    form.reset(
      {
        ...feedbackAboutRawValue,
        id: { value: feedbackAboutRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FeedbackAboutFormDefaults {
    return {
      id: null,
      status: false,
      feedbackRequests: [],
    };
  }
}
