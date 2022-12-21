import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IApplicationUser, NewApplicationUser } from '../application-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IApplicationUser for edit and NewApplicationUserFormGroupInput for create.
 */
type ApplicationUserFormGroupInput = IApplicationUser | PartialWithRequiredKeyOf<NewApplicationUser>;

type ApplicationUserFormDefaults = Pick<
  NewApplicationUser,
  'id' | 'status' | 'designations' | 'feedbackRequest1s' | 'groups' | 'feedbackRequests' | 'feedbackResponses'
>;

type ApplicationUserFormGroupContent = {
  id: FormControl<IApplicationUser['id'] | NewApplicationUser['id']>;
  firstname: FormControl<IApplicationUser['firstname']>;
  lastname: FormControl<IApplicationUser['lastname']>;
  emailid: FormControl<IApplicationUser['emailid']>;
  phonenumber: FormControl<IApplicationUser['phonenumber']>;
  location: FormControl<IApplicationUser['location']>;
  profilepic: FormControl<IApplicationUser['profilepic']>;
  profilepicContentType: FormControl<IApplicationUser['profilepicContentType']>;
  status: FormControl<IApplicationUser['status']>;
  internalUser: FormControl<IApplicationUser['internalUser']>;
  designations: FormControl<IApplicationUser['designations']>;
  feedbackRequest1s: FormControl<IApplicationUser['feedbackRequest1s']>;
  groups: FormControl<IApplicationUser['groups']>;
  feedbackRequests: FormControl<IApplicationUser['feedbackRequests']>;
  feedbackResponses: FormControl<IApplicationUser['feedbackResponses']>;
};

export type ApplicationUserFormGroup = FormGroup<ApplicationUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserFormService {
  createApplicationUserFormGroup(applicationUser: ApplicationUserFormGroupInput = { id: null }): ApplicationUserFormGroup {
    const applicationUserRawValue = {
      ...this.getFormDefaults(),
      ...applicationUser,
    };
    return new FormGroup<ApplicationUserFormGroupContent>({
      id: new FormControl(
        { value: applicationUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstname: new FormControl(applicationUserRawValue.firstname),
      lastname: new FormControl(applicationUserRawValue.lastname),
      emailid: new FormControl(applicationUserRawValue.emailid),
      phonenumber: new FormControl(applicationUserRawValue.phonenumber),
      location: new FormControl(applicationUserRawValue.location),
      profilepic: new FormControl(applicationUserRawValue.profilepic),
      profilepicContentType: new FormControl(applicationUserRawValue.profilepicContentType),
      status: new FormControl(applicationUserRawValue.status),
      internalUser: new FormControl(applicationUserRawValue.internalUser),
      designations: new FormControl(applicationUserRawValue.designations ?? []),
      feedbackRequest1s: new FormControl(applicationUserRawValue.feedbackRequest1s ?? []),
      groups: new FormControl(applicationUserRawValue.groups ?? []),
      feedbackRequests: new FormControl(applicationUserRawValue.feedbackRequests ?? []),
      feedbackResponses: new FormControl(applicationUserRawValue.feedbackResponses ?? []),
    });
  }

  getApplicationUser(form: ApplicationUserFormGroup): IApplicationUser | NewApplicationUser {
    return form.getRawValue() as IApplicationUser | NewApplicationUser;
  }

  resetForm(form: ApplicationUserFormGroup, applicationUser: ApplicationUserFormGroupInput): void {
    const applicationUserRawValue = { ...this.getFormDefaults(), ...applicationUser };
    form.reset(
      {
        ...applicationUserRawValue,
        id: { value: applicationUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ApplicationUserFormDefaults {
    return {
      id: null,
      status: false,
      designations: [],
      feedbackRequest1s: [],
      groups: [],
      feedbackRequests: [],
      feedbackResponses: [],
    };
  }
}
