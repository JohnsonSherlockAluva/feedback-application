import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedbackAbout } from '../feedback-about.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-feedback-about-detail',
  templateUrl: './feedback-about-detail.component.html',
})
export class FeedbackAboutDetailComponent implements OnInit {
  feedbackAbout: IFeedbackAbout | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackAbout }) => {
      this.feedbackAbout = feedbackAbout;
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
