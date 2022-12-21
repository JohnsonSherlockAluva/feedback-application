import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDesignation } from '../designation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../designation.test-samples';

import { DesignationService } from './designation.service';

const requireRestSample: IDesignation = {
  ...sampleWithRequiredData,
};

describe('Designation Service', () => {
  let service: DesignationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDesignation | IDesignation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DesignationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Designation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const designation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(designation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Designation', () => {
      const designation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(designation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Designation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Designation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Designation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDesignationToCollectionIfMissing', () => {
      it('should add a Designation to an empty array', () => {
        const designation: IDesignation = sampleWithRequiredData;
        expectedResult = service.addDesignationToCollectionIfMissing([], designation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(designation);
      });

      it('should not add a Designation to an array that contains it', () => {
        const designation: IDesignation = sampleWithRequiredData;
        const designationCollection: IDesignation[] = [
          {
            ...designation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, designation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Designation to an array that doesn't contain it", () => {
        const designation: IDesignation = sampleWithRequiredData;
        const designationCollection: IDesignation[] = [sampleWithPartialData];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, designation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(designation);
      });

      it('should add only unique Designation to an array', () => {
        const designationArray: IDesignation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const designationCollection: IDesignation[] = [sampleWithRequiredData];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, ...designationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const designation: IDesignation = sampleWithRequiredData;
        const designation2: IDesignation = sampleWithPartialData;
        expectedResult = service.addDesignationToCollectionIfMissing([], designation, designation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(designation);
        expect(expectedResult).toContain(designation2);
      });

      it('should accept null and undefined values', () => {
        const designation: IDesignation = sampleWithRequiredData;
        expectedResult = service.addDesignationToCollectionIfMissing([], null, designation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(designation);
      });

      it('should return initial array if no Designation is added', () => {
        const designationCollection: IDesignation[] = [sampleWithRequiredData];
        expectedResult = service.addDesignationToCollectionIfMissing(designationCollection, undefined, null);
        expect(expectedResult).toEqual(designationCollection);
      });
    });

    describe('compareDesignation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDesignation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDesignation(entity1, entity2);
        const compareResult2 = service.compareDesignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDesignation(entity1, entity2);
        const compareResult2 = service.compareDesignation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDesignation(entity1, entity2);
        const compareResult2 = service.compareDesignation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
