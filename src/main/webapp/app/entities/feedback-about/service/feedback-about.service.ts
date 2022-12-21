import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackAbout, NewFeedbackAbout } from '../feedback-about.model';

export type PartialUpdateFeedbackAbout = Partial<IFeedbackAbout> & Pick<IFeedbackAbout, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackAbout>;
export type EntityArrayResponseType = HttpResponse<IFeedbackAbout[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackAboutService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-abouts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(feedbackAbout: NewFeedbackAbout): Observable<EntityResponseType> {
    return this.http.post<IFeedbackAbout>(this.resourceUrl, feedbackAbout, { observe: 'response' });
  }

  update(feedbackAbout: IFeedbackAbout): Observable<EntityResponseType> {
    return this.http.put<IFeedbackAbout>(`${this.resourceUrl}/${this.getFeedbackAboutIdentifier(feedbackAbout)}`, feedbackAbout, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackAbout: PartialUpdateFeedbackAbout): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackAbout>(`${this.resourceUrl}/${this.getFeedbackAboutIdentifier(feedbackAbout)}`, feedbackAbout, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackAbout>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackAbout[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackAboutIdentifier(feedbackAbout: Pick<IFeedbackAbout, 'id'>): number {
    return feedbackAbout.id;
  }

  compareFeedbackAbout(o1: Pick<IFeedbackAbout, 'id'> | null, o2: Pick<IFeedbackAbout, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackAboutIdentifier(o1) === this.getFeedbackAboutIdentifier(o2) : o1 === o2;
  }

  addFeedbackAboutToCollectionIfMissing<Type extends Pick<IFeedbackAbout, 'id'>>(
    feedbackAboutCollection: Type[],
    ...feedbackAboutsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackAbouts: Type[] = feedbackAboutsToCheck.filter(isPresent);
    if (feedbackAbouts.length > 0) {
      const feedbackAboutCollectionIdentifiers = feedbackAboutCollection.map(
        feedbackAboutItem => this.getFeedbackAboutIdentifier(feedbackAboutItem)!
      );
      const feedbackAboutsToAdd = feedbackAbouts.filter(feedbackAboutItem => {
        const feedbackAboutIdentifier = this.getFeedbackAboutIdentifier(feedbackAboutItem);
        if (feedbackAboutCollectionIdentifiers.includes(feedbackAboutIdentifier)) {
          return false;
        }
        feedbackAboutCollectionIdentifiers.push(feedbackAboutIdentifier);
        return true;
      });
      return [...feedbackAboutsToAdd, ...feedbackAboutCollection];
    }
    return feedbackAboutCollection;
  }
}
