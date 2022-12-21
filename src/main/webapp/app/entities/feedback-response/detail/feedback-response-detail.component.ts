import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackResponse } from '../feedback-response.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-feedback-response-detail',
  templateUrl: './feedback-response-detail.component.html',
})
export class FeedbackResponseDetailComponent implements OnInit {
  feedbackResponse: IFeedbackResponse | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackResponse }) => {
      this.feedbackResponse = feedbackResponse;
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
