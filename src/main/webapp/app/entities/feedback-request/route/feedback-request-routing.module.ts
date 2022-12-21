import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackRequestComponent } from '../list/feedback-request.component';
import { FeedbackRequestDetailComponent } from '../detail/feedback-request-detail.component';
import { FeedbackRequestUpdateComponent } from '../update/feedback-request-update.component';
import { FeedbackRequestRoutingResolveService } from './feedback-request-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const feedbackRequestRoute: Routes = [
  {
    path: '',
    component: FeedbackRequestComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackRequestDetailComponent,
    resolve: {
      feedbackRequest: FeedbackRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackRequestUpdateComponent,
    resolve: {
      feedbackRequest: FeedbackRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackRequestUpdateComponent,
    resolve: {
      feedbackRequest: FeedbackRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackRequestRoute)],
  exports: [RouterModule],
})
export class FeedbackRequestRoutingModule {}
