import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeedbackResponse } from '../feedback-response.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../feedback-response.test-samples';

import { FeedbackResponseService } from './feedback-response.service';

const requireRestSample: IFeedbackResponse = {
  ...sampleWithRequiredData,
};

describe('FeedbackResponse Service', () => {
  let service: FeedbackResponseService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeedbackResponse | IFeedbackResponse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeedbackResponseService);
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

    it('should create a FeedbackResponse', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const feedbackResponse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feedbackResponse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeedbackResponse', () => {
      const feedbackResponse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feedbackResponse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeedbackResponse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeedbackResponse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeedbackResponse', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeedbackResponseToCollectionIfMissing', () => {
      it('should add a FeedbackResponse to an empty array', () => {
        const feedbackResponse: IFeedbackResponse = sampleWithRequiredData;
        expectedResult = service.addFeedbackResponseToCollectionIfMissing([], feedbackResponse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackResponse);
      });

      it('should not add a FeedbackResponse to an array that contains it', () => {
        const feedbackResponse: IFeedbackResponse = sampleWithRequiredData;
        const feedbackResponseCollection: IFeedbackResponse[] = [
          {
            ...feedbackResponse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeedbackResponseToCollectionIfMissing(feedbackResponseCollection, feedbackResponse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeedbackResponse to an array that doesn't contain it", () => {
        const feedbackResponse: IFeedbackResponse = sampleWithRequiredData;
        const feedbackResponseCollection: IFeedbackResponse[] = [sampleWithPartialData];
        expectedResult = service.addFeedbackResponseToCollectionIfMissing(feedbackResponseCollection, feedbackResponse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackResponse);
      });

      it('should add only unique FeedbackResponse to an array', () => {
        const feedbackResponseArray: IFeedbackResponse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feedbackResponseCollection: IFeedbackResponse[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackResponseToCollectionIfMissing(feedbackResponseCollection, ...feedbackResponseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feedbackResponse: IFeedbackResponse = sampleWithRequiredData;
        const feedbackResponse2: IFeedbackResponse = sampleWithPartialData;
        expectedResult = service.addFeedbackResponseToCollectionIfMissing([], feedbackResponse, feedbackResponse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feedbackResponse);
        expect(expectedResult).toContain(feedbackResponse2);
      });

      it('should accept null and undefined values', () => {
        const feedbackResponse: IFeedbackResponse = sampleWithRequiredData;
        expectedResult = service.addFeedbackResponseToCollectionIfMissing([], null, feedbackResponse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feedbackResponse);
      });

      it('should return initial array if no FeedbackResponse is added', () => {
        const feedbackResponseCollection: IFeedbackResponse[] = [sampleWithRequiredData];
        expectedResult = service.addFeedbackResponseToCollectionIfMissing(feedbackResponseCollection, undefined, null);
        expect(expectedResult).toEqual(feedbackResponseCollection);
      });
    });

    describe('compareFeedbackResponse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeedbackResponse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeedbackResponse(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeedbackResponse(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeedbackResponse(entity1, entity2);
        const compareResult2 = service.compareFeedbackResponse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
