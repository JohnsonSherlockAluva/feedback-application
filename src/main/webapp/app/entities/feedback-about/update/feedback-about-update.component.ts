import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FeedbackAboutFormService, FeedbackAboutFormGroup } from './feedback-about-form.service';
import { IFeedbackAbout } from '../feedback-about.model';
import { FeedbackAboutService } from '../service/feedback-about.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-feedback-about-update',
  templateUrl: './feedback-about-update.component.html',
})
export class FeedbackAboutUpdateComponent implements OnInit {
  isSaving = false;
  feedbackAbout: IFeedbackAbout | null = null;

  editForm: FeedbackAboutFormGroup = this.feedbackAboutFormService.createFeedbackAboutFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected feedbackAboutService: FeedbackAboutService,
    protected feedbackAboutFormService: FeedbackAboutFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackAbout }) => {
      this.feedbackAbout = feedbackAbout;
      if (feedbackAbout) {
        this.updateForm(feedbackAbout);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('feedbackApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackAbout = this.feedbackAboutFormService.getFeedbackAbout(this.editForm);
    if (feedbackAbout.id !== null) {
      this.subscribeToSaveResponse(this.feedbackAboutService.update(feedbackAbout));
    } else {
      this.subscribeToSaveResponse(this.feedbackAboutService.create(feedbackAbout));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackAbout>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(feedbackAbout: IFeedbackAbout): void {
    this.feedbackAbout = feedbackAbout;
    this.feedbackAboutFormService.resetForm(this.editForm, feedbackAbout);
  }
}
