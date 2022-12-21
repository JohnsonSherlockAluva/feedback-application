import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FeedbackRequestComponent } from './list/feedback-request.component';
import { FeedbackRequestDetailComponent } from './detail/feedback-request-detail.component';
import { FeedbackRequestUpdateComponent } from './update/feedback-request-update.component';
import { FeedbackRequestDeleteDialogComponent } from './delete/feedback-request-delete-dialog.component';
import { FeedbackRequestRoutingModule } from './route/feedback-request-routing.module';

@NgModule({
  imports: [SharedModule, FeedbackRequestRoutingModule],
  declarations: [
    FeedbackRequestComponent,
    FeedbackRequestDetailComponent,
    FeedbackRequestUpdateComponent,
    FeedbackRequestDeleteDialogComponent,
  ],
})
export class FeedbackRequestModule {}
