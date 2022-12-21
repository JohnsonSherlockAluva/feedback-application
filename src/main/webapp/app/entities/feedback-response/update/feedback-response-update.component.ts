import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FeedbackResponseFormService, FeedbackResponseFormGroup } from './feedback-response-form.service';
import { IFeedbackResponse } from '../feedback-response.model';
import { FeedbackResponseService } from '../service/feedback-response.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

@Component({
  selector: 'jhi-feedback-response-update',
  templateUrl: './feedback-response-update.component.html',
})
export class FeedbackResponseUpdateComponent implements OnInit {
  isSaving = false;
  feedbackResponse: IFeedbackResponse | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  editForm: FeedbackResponseFormGroup = this.feedbackResponseFormService.createFeedbackResponseFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected feedbackResponseService: FeedbackResponseService,
    protected feedbackResponseFormService: FeedbackResponseFormService,
    protected applicationUserService: ApplicationUserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackResponse }) => {
      this.feedbackResponse = feedbackResponse;
      if (feedbackResponse) {
        this.updateForm(feedbackResponse);
      }

      this.loadRelationshipsOptions();
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
    const feedbackResponse = this.feedbackResponseFormService.getFeedbackResponse(this.editForm);
    if (feedbackResponse.id !== null) {
      this.subscribeToSaveResponse(this.feedbackResponseService.update(feedbackResponse));
    } else {
      this.subscribeToSaveResponse(this.feedbackResponseService.create(feedbackResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackResponse>>): void {
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

  protected updateForm(feedbackResponse: IFeedbackResponse): void {
    this.feedbackResponse = feedbackResponse;
    this.feedbackResponseFormService.resetForm(this.editForm, feedbackResponse);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      ...(feedbackResponse.applicationUsers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            ...(this.feedbackResponse?.applicationUsers ?? [])
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
