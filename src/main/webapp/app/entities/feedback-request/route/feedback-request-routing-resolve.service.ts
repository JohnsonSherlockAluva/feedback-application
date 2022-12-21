import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackRequest } from '../feedback-request.model';
import { FeedbackRequestService } from '../service/feedback-request.service';

@Injectable({ providedIn: 'root' })
export class FeedbackRequestRoutingResolveService implements Resolve<IFeedbackRequest | null> {
  constructor(protected service: FeedbackRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedbackRequest | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedbackRequest: HttpResponse<IFeedbackRequest>) => {
          if (feedbackRequest.body) {
            return of(feedbackRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
