import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeedbackAbout } from '../feedback-about.model';
import { FeedbackAboutService } from '../service/feedback-about.service';

@Injectable({ providedIn: 'root' })
export class FeedbackAboutRoutingResolveService implements Resolve<IFeedbackAbout | null> {
  constructor(protected service: FeedbackAboutService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFeedbackAbout | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((feedbackAbout: HttpResponse<IFeedbackAbout>) => {
          if (feedbackAbout.body) {
            return of(feedbackAbout.body);
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
