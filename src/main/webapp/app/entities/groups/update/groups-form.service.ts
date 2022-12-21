import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGroups, NewGroups } from '../groups.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGroups for edit and NewGroupsFormGroupInput for create.
 */
type GroupsFormGroupInput = IGroups | PartialWithRequiredKeyOf<NewGroups>;

type GroupsFormDefaults = Pick<NewGroups, 'id' | 'status' | 'applicationUsers' | 'feedbackRequests'>;

type GroupsFormGroupContent = {
  id: FormControl<IGroups['id'] | NewGroups['id']>;
  groupName: FormControl<IGroups['groupName']>;
  discription: FormControl<IGroups['discription']>;
  picture: FormControl<IGroups['picture']>;
  pictureContentType: FormControl<IGroups['pictureContentType']>;
  status: FormControl<IGroups['status']>;
  applicationUsers: FormControl<IGroups['applicationUsers']>;
  feedbackRequests: FormControl<IGroups['feedbackRequests']>;
};

export type GroupsFormGroup = FormGroup<GroupsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GroupsFormService {
  createGroupsFormGroup(groups: GroupsFormGroupInput = { id: null }): GroupsFormGroup {
    const groupsRawValue = {
      ...this.getFormDefaults(),
      ...groups,
    };
    return new FormGroup<GroupsFormGroupContent>({
      id: new FormControl(
        { value: groupsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      groupName: new FormControl(groupsRawValue.groupName),
      discription: new FormControl(groupsRawValue.discription),
      picture: new FormControl(groupsRawValue.picture),
      pictureContentType: new FormControl(groupsRawValue.pictureContentType),
      status: new FormControl(groupsRawValue.status),
      applicationUsers: new FormControl(groupsRawValue.applicationUsers ?? []),
      feedbackRequests: new FormControl(groupsRawValue.feedbackRequests ?? []),
    });
  }

  getGroups(form: GroupsFormGroup): IGroups | NewGroups {
    return form.getRawValue() as IGroups | NewGroups;
  }

  resetForm(form: GroupsFormGroup, groups: GroupsFormGroupInput): void {
    const groupsRawValue = { ...this.getFormDefaults(), ...groups };
    form.reset(
      {
        ...groupsRawValue,
        id: { value: groupsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GroupsFormDefaults {
    return {
      id: null,
      status: false,
      applicationUsers: [],
      feedbackRequests: [],
    };
  }
}
