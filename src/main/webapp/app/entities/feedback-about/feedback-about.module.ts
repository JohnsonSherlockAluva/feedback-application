import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FeedbackAboutComponent } from './list/feedback-about.component';
import { FeedbackAboutDetailComponent } from './detail/feedback-about-detail.component';
import { FeedbackAboutUpdateComponent } from './update/feedback-about-update.component';
import { FeedbackAboutDeleteDialogComponent } from './delete/feedback-about-delete-dialog.component';
import { FeedbackAboutRoutingModule } from './route/feedback-about-routing.module';

@NgModule({
  imports: [SharedModule, FeedbackAboutRoutingModule],
  declarations: [FeedbackAboutComponent, FeedbackAboutDetailComponent, FeedbackAboutUpdateComponent, FeedbackAboutDeleteDialogComponent],
})
export class FeedbackAboutModule {}
