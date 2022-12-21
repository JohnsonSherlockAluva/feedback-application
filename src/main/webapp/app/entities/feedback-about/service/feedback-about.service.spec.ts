import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeedbackAbout } from '../feedback-about.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-about.test-samples';

import { FeedbackAboutService } from './feedback-about.service';

const requireRestSample: IFeedbackAbout = {
  ...sampleWithRequiredData,
};

describe('FeedbackAbout Service', () => {
  let service: FeedbackAboutService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackAbout | IFeedbackAbout[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackAboutService);
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

    it('should create a FeedbackAbout', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const feedbackAbout = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackAbout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackAbout', () => {
      const feedbackAbout = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackAbout).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackAbout', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackAbout', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackAbout', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackAboutToCollectionIfMissing', () => {
      it('should add a FeedbackAbout to an empty array', () => {
        const feedbackAbout: IFeedbackAbout = sampleWithRequiredData;
        expectedResult = service.addFeedbackAboutToCollectionIfMissing([], feedbackAbout);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackAbout);
      });

      it('should not add a FeedbackAbout to an array that contains it', () => {
        const feedbackAbout: IFeedbackAbout = sampleWithRequiredData;
        const feedbackAboutCollection: IFeedbackAbout[] = [
          {
            ...feedbackAbout,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackAboutToCollectionIfMissing(feedbackAboutCollection, feedbackAbout);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackAbout to an array that doesn't contain it", () => {
        const feedbackAbout: IFeedbackAbout = sampleWithRequiredData;
        const feedbackAboutCollection: IFeedbackAbout[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackAboutToCollectionIfMissing(feedbackAboutCollection, feedbackAbout);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackAbout);
      });

      it('should add only unique FeedbackAbout to an array', () => {
        const feedbackAboutArray: IFeedbackAbout[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackAboutCollection: IFeedbackAbout[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackAboutToCollectionIfMissing(feedbackAboutCollection, ...feedbackAboutArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackAbout: IFeedbackAbout = sampleWithRequiredData;
        const feedbackAbout2: IFeedbackAbout = sampleWithPartialData;
        expectedResult = service.addFeedbackAboutToCollectionIfMissing([], feedbackAbout, feedbackAbout2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackAbout);
        expect(expectedResult).toContain(feedbackAbout2);
      });

      it('should accept null and undefined values', () => {
        const feedbackAbout: IFeedbackAbout = sampleWithRequiredData;
        expectedResult = service.addFeedbackAboutToCollectionIfMissing([], null, feedbackAbout, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackAbout);
      });

      it('should return initial array if no FeedbackAbout is added', () => {
        const feedbackAboutCollection: IFeedbackAbout[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackAboutToCollectionIfMissing(feedbackAboutCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackAboutCollection);
      });
    });

    describe('compareFeedbackAbout', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackAbout(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackAbout(entity1, entity2);
        const compareResult2 = service.compareFeedbackAbout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackAbout(entity1, entity2);
        const compareResult2 = service.compareFeedbackAbout(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackAbout(entity1, entity2);
        const compareResult2 = service.compareFeedbackAbout(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
