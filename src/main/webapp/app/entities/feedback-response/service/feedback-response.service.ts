import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackResponse, NewFeedbackResponse } from '../feedback-response.model';

export type PartialUpdateFeedbackResponse = Partial<IFeedbackResponse> & Pick<IFeedbackResponse, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackResponse>;
export type EntityArrayResponseType = HttpResponse<IFeedbackResponse[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackResponseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-responses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(feedbackResponse: NewFeedbackResponse): Observable<EntityResponseType> {
    return this.http.post<IFeedbackResponse>(this.resourceUrl, feedbackResponse, { observe: 'response' });
  }

  update(feedbackResponse: IFeedbackResponse): Observable<EntityResponseType> {
    return this.http.put<IFeedbackResponse>(
      `${this.resourceUrl}/${this.getFeedbackResponseIdentifier(feedbackResponse)}`,
      feedbackResponse,
      { observe: 'response' }
    );
  }

  partialUpdate(feedbackResponse: PartialUpdateFeedbackResponse): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackResponse>(
      `${this.resourceUrl}/${this.getFeedbackResponseIdentifier(feedbackResponse)}`,
      feedbackResponse,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackResponse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackResponse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackResponseIdentifier(feedbackResponse: Pick<IFeedbackResponse, 'id'>): number {
    return feedbackResponse.id;
  }

  compareFeedbackResponse(o1: Pick<IFeedbackResponse, 'id'> | null, o2: Pick<IFeedbackResponse, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackResponseIdentifier(o1) === this.getFeedbackResponseIdentifier(o2) : o1 === o2;
  }

  addFeedbackResponseToCollectionIfMissing<Type extends Pick<IFeedbackResponse, 'id'>>(
    feedbackResponseCollection: Type[],
    ...feedbackResponsesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackResponses: Type[] = feedbackResponsesToCheck.filter(isPresent);
    if (feedbackResponses.length > 0) {
      const feedbackResponseCollectionIdentifiers = feedbackResponseCollection.map(
        feedbackResponseItem => this.getFeedbackResponseIdentifier(feedbackResponseItem)!
      );
      const feedbackResponsesToAdd = feedbackResponses.filter(feedbackResponseItem => {
        const feedbackResponseIdentifier = this.getFeedbackResponseIdentifier(feedbackResponseItem);
        if (feedbackResponseCollectionIdentifiers.includes(feedbackResponseIdentifier)) {
          return false;
        }
        feedbackResponseCollectionIdentifiers.push(feedbackResponseIdentifier);
        return true;
      });
      return [...feedbackResponsesToAdd, ...feedbackResponseCollection];
    }
    return feedbackResponseCollection;
  }
}
