import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackRequest } from '../feedback-request.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-feedback-request-detail',
  templateUrl: './feedback-request-detail.component.html',
})
export class FeedbackRequestDetailComponent implements OnInit {
  feedbackRequest: IFeedbackRequest | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackRequest }) => {
      this.feedbackRequest = feedbackRequest;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
