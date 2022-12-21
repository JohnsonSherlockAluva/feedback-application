import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedbackRequest } from '../feedback-request.model';
import { FeedbackRequestService } from '../service/feedback-request.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './feedback-request-delete-dialog.component.html',
})
export class FeedbackRequestDeleteDialogComponent {
  feedbackRequest?: IFeedbackRequest;

  constructor(protected feedbackRequestService: FeedbackRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackRequestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
