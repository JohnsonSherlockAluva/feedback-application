import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'application-user',
        data: { pageTitle: 'feedbackApp.applicationUser.home.title' },
        loadChildren: () => import('./application-user/application-user.module').then(m => m.ApplicationUserModule),
      },
      {
        path: 'designation',
        data: { pageTitle: 'feedbackApp.designation.home.title' },
        loadChildren: () => import('./designation/designation.module').then(m => m.DesignationModule),
      },
      {
        path: 'feedback-about',
        data: { pageTitle: 'feedbackApp.feedbackAbout.home.title' },
        loadChildren: () => import('./feedback-about/feedback-about.module').then(m => m.FeedbackAboutModule),
      },
      {
        path: 'feedback-request',
        data: { pageTitle: 'feedbackApp.feedbackRequest.home.title' },
        loadChildren: () => import('./feedback-request/feedback-request.module').then(m => m.FeedbackRequestModule),
      },
      {
        path: 'feedback-response',
        data: { pageTitle: 'feedbackApp.feedbackResponse.home.title' },
        loadChildren: () => import('./feedback-response/feedback-response.module').then(m => m.FeedbackResponseModule),
      },
      {
        path: 'groups',
        data: { pageTitle: 'feedbackApp.groups.home.title' },
        loadChildren: () => import('./groups/groups.module').then(m => m.GroupsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
