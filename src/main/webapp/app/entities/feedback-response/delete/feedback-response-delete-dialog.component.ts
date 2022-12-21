import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedbackResponse } from '../feedback-response.model';
import { FeedbackResponseService } from '../service/feedback-response.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './feedback-response-delete-dialog.component.html',
})
export class FeedbackResponseDeleteDialogComponent {
  feedbackResponse?: IFeedbackResponse;

  constructor(protected feedbackResponseService: FeedbackResponseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackResponseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
