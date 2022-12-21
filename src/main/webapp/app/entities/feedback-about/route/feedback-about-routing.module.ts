import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FeedbackAboutComponent } from '../list/feedback-about.component';
import { FeedbackAboutDetailComponent } from '../detail/feedback-about-detail.component';
import { FeedbackAboutUpdateComponent } from '../update/feedback-about-update.component';
import { FeedbackAboutRoutingResolveService } from './feedback-about-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const feedbackAboutRoute: Routes = [
  {
    path: '',
    component: FeedbackAboutComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeedbackAboutDetailComponent,
    resolve: {
      feedbackAbout: FeedbackAboutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeedbackAboutUpdateComponent,
    resolve: {
      feedbackAbout: FeedbackAboutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeedbackAboutUpdateComponent,
    resolve: {
      feedbackAbout: FeedbackAboutRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(feedbackAboutRoute)],
  exports: [RouterModule],
})
export class FeedbackAboutRoutingModule {}
