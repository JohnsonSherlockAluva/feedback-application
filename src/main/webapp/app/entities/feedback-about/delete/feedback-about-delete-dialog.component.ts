import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFeedbackAbout } from '../feedback-about.model';
import { FeedbackAboutService } from '../service/feedback-about.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './feedback-about-delete-dialog.component.html',
})
export class FeedbackAboutDeleteDialogComponent {
  feedbackAbout?: IFeedbackAbout;

  constructor(protected feedbackAboutService: FeedbackAboutService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackAboutService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
