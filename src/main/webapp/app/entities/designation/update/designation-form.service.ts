import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDesignation, NewDesignation } from '../designation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDesignation for edit and NewDesignationFormGroupInput for create.
 */
type DesignationFormGroupInput = IDesignation | PartialWithRequiredKeyOf<NewDesignation>;

type DesignationFormDefaults = Pick<NewDesignation, 'id' | 'status' | 'applicationUsers'>;

type DesignationFormGroupContent = {
  id: FormControl<IDesignation['id'] | NewDesignation['id']>;
  designationName: FormControl<IDesignation['designationName']>;
  discription: FormControl<IDesignation['discription']>;
  picture: FormControl<IDesignation['picture']>;
  pictureContentType: FormControl<IDesignation['pictureContentType']>;
  status: FormControl<IDesignation['status']>;
  applicationUsers: FormControl<IDesignation['applicationUsers']>;
};

export type DesignationFormGroup = FormGroup<DesignationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DesignationFormService {
  createDesignationFormGroup(designation: DesignationFormGroupInput = { id: null }): DesignationFormGroup {
    const designationRawValue = {
      ...this.getFormDefaults(),
      ...designation,
    };
    return new FormGroup<DesignationFormGroupContent>({
      id: new FormControl(
        { value: designationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      designationName: new FormControl(designationRawValue.designationName),
      discription: new FormControl(designationRawValue.discription),
      picture: new FormControl(designationRawValue.picture),
      pictureContentType: new FormControl(designationRawValue.pictureContentType),
      status: new FormControl(designationRawValue.status),
      applicationUsers: new FormControl(designationRawValue.applicationUsers ?? []),
    });
  }

  getDesignation(form: DesignationFormGroup): IDesignation | NewDesignation {
    return form.getRawValue() as IDesignation | NewDesignation;
  }

  resetForm(form: DesignationFormGroup, designation: DesignationFormGroupInput): void {
    const designationRawValue = { ...this.getFormDefaults(), ...designation };
    form.reset(
      {
        ...designationRawValue,
        id: { value: designationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DesignationFormDefaults {
    return {
      id: null,
      status: false,
      applicationUsers: [],
    };
  }
}
