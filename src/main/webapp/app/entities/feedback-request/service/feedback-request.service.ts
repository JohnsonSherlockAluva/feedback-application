import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackRequest, NewFeedbackRequest } from '../feedback-request.model';

export type PartialUpdateFeedbackRequest = Partial<IFeedbackRequest> & Pick<IFeedbackRequest, 'id'>;

type RestOf<T extends IFeedbackRequest | NewFeedbackRequest> = Omit<T, 'startDate' | 'endDate'> & {
  startDate?: string | null;
  endDate?: string | null;
};

export type RestFeedbackRequest = RestOf<IFeedbackRequest>;

export type NewRestFeedbackRequest = RestOf<NewFeedbackRequest>;

export type PartialUpdateRestFeedbackRequest = RestOf<PartialUpdateFeedbackRequest>;

export type EntityResponseType = HttpResponse<IFeedbackRequest>;
export type EntityArrayResponseType = HttpResponse<IFeedbackRequest[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(feedbackRequest: NewFeedbackRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedbackRequest);
    return this.http
      .post<RestFeedbackRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(feedbackRequest: IFeedbackRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedbackRequest);
    return this.http
      .put<RestFeedbackRequest>(`${this.resourceUrl}/${this.getFeedbackRequestIdentifier(feedbackRequest)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(feedbackRequest: PartialUpdateFeedbackRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedbackRequest);
    return this.http
      .patch<RestFeedbackRequest>(`${this.resourceUrl}/${this.getFeedbackRequestIdentifier(feedbackRequest)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeedbackRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeedbackRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackRequestIdentifier(feedbackRequest: Pick<IFeedbackRequest, 'id'>): number {
    return feedbackRequest.id;
  }

  compareFeedbackRequest(o1: Pick<IFeedbackRequest, 'id'> | null, o2: Pick<IFeedbackRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackRequestIdentifier(o1) === this.getFeedbackRequestIdentifier(o2) : o1 === o2;
  }

  addFeedbackRequestToCollectionIfMissing<Type extends Pick<IFeedbackRequest, 'id'>>(
    feedbackRequestCollection: Type[],
    ...feedbackRequestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackRequests: Type[] = feedbackRequestsToCheck.filter(isPresent);
    if (feedbackRequests.length > 0) {
      const feedbackRequestCollectionIdentifiers = feedbackRequestCollection.map(
        feedbackRequestItem => this.getFeedbackRequestIdentifier(feedbackRequestItem)!
      );
      const feedbackRequestsToAdd = feedbackRequests.filter(feedbackRequestItem => {
        const feedbackRequestIdentifier = this.getFeedbackRequestIdentifier(feedbackRequestItem);
        if (feedbackRequestCollectionIdentifiers.includes(feedbackRequestIdentifier)) {
          return false;
        }
        feedbackRequestCollectionIdentifiers.push(feedbackRequestIdentifier);
        return true;
      });
      return [...feedbackRequestsToAdd, ...feedbackRequestCollection];
    }
    return feedbackRequestCollection;
  }

  protected convertDateFromClient<T extends IFeedbackRequest | NewFeedbackRequest | PartialUpdateFeedbackRequest>(
    feedbackRequest: T
  ): RestOf<T> {
    return {
      ...feedbackRequest,
      startDate: feedbackRequest.startDate?.toJSON() ?? null,
      endDate: feedbackRequest.endDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFeedbackRequest: RestFeedbackRequest): IFeedbackRequest {
    return {
      ...restFeedbackRequest,
      startDate: restFeedbackRequest.startDate ? dayjs(restFeedbackRequest.startDate) : undefined,
      endDate: restFeedbackRequest.endDate ? dayjs(restFeedbackRequest.endDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeedbackRequest>): HttpResponse<IFeedbackRequest> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeedbackRequest[]>): HttpResponse<IFeedbackRequest[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
