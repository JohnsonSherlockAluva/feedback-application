import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackResponseComponent } from '../list/feedback-response.component';
import { FeedbackResponseDetailComponent } from '../detail/feedback-response-detail.component';
import { FeedbackResponseUpdateComponent } from '../update/feedback-response-update.component';
import { FeedbackResponseRoutingResolveService } from './feedback-response-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const feedbackResponseRoute: Routes = [
  {
    path: '',
    component: FeedbackResponseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackResponseDetailComponent,
    resolve: {
      feedbackResponse: FeedbackResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackResponseUpdateComponent,
    resolve: {
      feedbackResponse: FeedbackResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackResponseUpdateComponent,
    resolve: {
      feedbackResponse: FeedbackResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackResponseRoute)],
  exports: [RouterModule],
})
export class FeedbackResponseRoutingModule {}
