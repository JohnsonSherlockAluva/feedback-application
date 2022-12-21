import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackResponse } from '../feedback-response.model';
import { FeedbackResponseService } from '../service/feedback-response.service';

@Injectable({ providedIn: 'root' })
export class FeedbackResponseRoutingResolveService implements Resolve<IFeedbackResponse | null> {
  constructor(protected service: FeedbackResponseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedbackResponse | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedbackResponse: HttpResponse<IFeedbackResponse>) => {
          if (feedbackResponse.body) {
            return of(feedbackResponse.body);
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
