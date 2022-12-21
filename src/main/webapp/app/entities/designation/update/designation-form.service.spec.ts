import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../designation.test-samples';

import { DesignationFormService } from './designation-form.service';

describe('Designation Form Service', () => {
  let service: DesignationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DesignationFormService);
  });

  describe('Service methods', () => {
    describe('createDesignationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDesignationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designationName: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            applicationUsers: expect.any(Object),
          })
        );
      });

      it('passing IDesignation should create a new form with FormGroup', () => {
        const formGroup = service.createDesignationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designationName: expect.any(Object),
            discription: expect.any(Object),
            picture: expect.any(Object),
            status: expect.any(Object),
            applicationUsers: expect.any(Object),
          })
        );
      });
    });

    describe('getDesignation', () => {
      it('should return NewDesignation for default Designation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDesignationFormGroup(sampleWithNewData);

        const designation = service.getDesignation(formGroup) as any;

        expect(designation).toMatchObject(sampleWithNewData);
      });

      it('should return NewDesignation for empty Designation initial value', () => {
        const formGroup = service.createDesignationFormGroup();

        const designation = service.getDesignation(formGroup) as any;

        expect(designation).toMatchObject({});
      });

      it('should return IDesignation', () => {
        const formGroup = service.createDesignationFormGroup(sampleWithRequiredData);

        const designation = service.getDesignation(formGroup) as any;

        expect(designation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDesignation should not enable id FormControl', () => {
        const formGroup = service.createDesignationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDesignation should disable id FormControl', () => {
        const formGroup = service.createDesignationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
