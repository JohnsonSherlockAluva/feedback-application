import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FeedbackResponseComponent } from './list/feedback-response.component';
import { FeedbackResponseDetailComponent } from './detail/feedback-response-detail.component';
import { FeedbackResponseUpdateComponent } from './update/feedback-response-update.component';
import { FeedbackResponseDeleteDialogComponent } from './delete/feedback-response-delete-dialog.component';
import { FeedbackResponseRoutingModule } from './route/feedback-response-routing.module';

@NgModule({
  imports: [SharedModule, FeedbackResponseRoutingModule],
  declarations: [
    FeedbackResponseComponent,
    FeedbackResponseDetailComponent,
    FeedbackResponseUpdateComponent,
    FeedbackResponseDeleteDialogComponent,
  ],
})
export class FeedbackResponseModule {}
