import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeedbackRequest } from '../feedback-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-request.test-samples';

import { FeedbackRequestService, RestFeedbackRequest } from './feedback-request.service';

const requireRestSample: RestFeedbackRequest = {
  ...sampleWithRequiredData,
  startDate: sampleWithRequiredData.startDate?.toJSON(),
  endDate: sampleWithRequiredData.endDate?.toJSON(),
};

describe('FeedbackRequest Service', () => {
  let service: FeedbackRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackRequest | IFeedbackRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackRequestService);
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

    it('should create a FeedbackRequest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const feedbackRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackRequest', () => {
      const feedbackRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackRequestToCollectionIfMissing', () => {
      it('should add a FeedbackRequest to an empty array', () => {
        const feedbackRequest: IFeedbackRequest = sampleWithRequiredData;
        expectedResult = service.addFeedbackRequestToCollectionIfMissing([], feedbackRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackRequest);
      });

      it('should not add a FeedbackRequest to an array that contains it', () => {
        const feedbackRequest: IFeedbackRequest = sampleWithRequiredData;
        const feedbackRequestCollection: IFeedbackRequest[] = [
          {
            ...feedbackRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackRequestToCollectionIfMissing(feedbackRequestCollection, feedbackRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackRequest to an array that doesn't contain it", () => {
        const feedbackRequest: IFeedbackRequest = sampleWithRequiredData;
        const feedbackRequestCollection: IFeedbackRequest[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackRequestToCollectionIfMissing(feedbackRequestCollection, feedbackRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackRequest);
      });

      it('should add only unique FeedbackRequest to an array', () => {
        const feedbackRequestArray: IFeedbackRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackRequestCollection: IFeedbackRequest[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackRequestToCollectionIfMissing(feedbackRequestCollection, ...feedbackRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackRequest: IFeedbackRequest = sampleWithRequiredData;
        const feedbackRequest2: IFeedbackRequest = sampleWithPartialData;
        expectedResult = service.addFeedbackRequestToCollectionIfMissing([], feedbackRequest, feedbackRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackRequest);
        expect(expectedResult).toContain(feedbackRequest2);
      });

      it('should accept null and undefined values', () => {
        const feedbackRequest: IFeedbackRequest = sampleWithRequiredData;
        expectedResult = service.addFeedbackRequestToCollectionIfMissing([], null, feedbackRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackRequest);
      });

      it('should return initial array if no FeedbackRequest is added', () => {
        const feedbackRequestCollection: IFeedbackRequest[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackRequestToCollectionIfMissing(feedbackRequestCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackRequestCollection);
      });
    });

    describe('compareFeedbackRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackRequest(entity1, entity2);
        const compareResult2 = service.compareFeedbackRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackRequest(entity1, entity2);
        const compareResult2 = service.compareFeedbackRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackRequest(entity1, entity2);
        const compareResult2 = service.compareFeedbackRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
