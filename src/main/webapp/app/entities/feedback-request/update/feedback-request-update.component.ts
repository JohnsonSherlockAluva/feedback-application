import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FeedbackRequestFormService, FeedbackRequestFormGroup } from './feedback-request-form.service';
import { IFeedbackRequest } from '../feedback-request.model';
import { FeedbackRequestService } from '../service/feedback-request.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IFeedbackAbout } from 'app/entities/feedback-about/feedback-about.model';
import { FeedbackAboutService } from 'app/entities/feedback-about/service/feedback-about.service';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';
import { FeedbackResponseService } from 'app/entities/feedback-response/service/feedback-response.service';
import { IGroups } from 'app/entities/groups/groups.model';
import { GroupsService } from 'app/entities/groups/service/groups.service';

@Component({
  selector: 'jhi-feedback-request-update',
  templateUrl: './feedback-request-update.component.html',
})
export class FeedbackRequestUpdateComponent implements OnInit {
  isSaving = false;
  feedbackRequest: IFeedbackRequest | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];
  feedbackAboutsSharedCollection: IFeedbackAbout[] = [];
  feedbackResponsesSharedCollection: IFeedbackResponse[] = [];
  groupsSharedCollection: IGroups[] = [];

  editForm: FeedbackRequestFormGroup = this.feedbackRequestFormService.createFeedbackRequestFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected feedbackRequestService: FeedbackRequestService,
    protected feedbackRequestFormService: FeedbackRequestFormService,
    protected applicationUserService: ApplicationUserService,
    protected feedbackAboutService: FeedbackAboutService,
    protected feedbackResponseService: FeedbackResponseService,
    protected groupsService: GroupsService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  compareFeedbackAbout = (o1: IFeedbackAbout | null, o2: IFeedbackAbout | null): boolean =>
    this.feedbackAboutService.compareFeedbackAbout(o1, o2);

  compareFeedbackResponse = (o1: IFeedbackResponse | null, o2: IFeedbackResponse | null): boolean =>
    this.feedbackResponseService.compareFeedbackResponse(o1, o2);

  compareGroups = (o1: IGroups | null, o2: IGroups | null): boolean => this.groupsService.compareGroups(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackRequest }) => {
      this.feedbackRequest = feedbackRequest;
      if (feedbackRequest) {
        this.updateForm(feedbackRequest);
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
    const feedbackRequest = this.feedbackRequestFormService.getFeedbackRequest(this.editForm);
    if (feedbackRequest.id !== null) {
      this.subscribeToSaveResponse(this.feedbackRequestService.update(feedbackRequest));
    } else {
      this.subscribeToSaveResponse(this.feedbackRequestService.create(feedbackRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackRequest>>): void {
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

  protected updateForm(feedbackRequest: IFeedbackRequest): void {
    this.feedbackRequest = feedbackRequest;
    this.feedbackRequestFormService.resetForm(this.editForm, feedbackRequest);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      ...(feedbackRequest.feedbackAboutUsers ?? []),
      ...(feedbackRequest.feedbackToUsers ?? [])
    );
    this.feedbackAboutsSharedCollection = this.feedbackAboutService.addFeedbackAboutToCollectionIfMissing<IFeedbackAbout>(
      this.feedbackAboutsSharedCollection,
      ...(feedbackRequest.feedbackAbouts ?? [])
    );
    this.feedbackResponsesSharedCollection = this.feedbackResponseService.addFeedbackResponseToCollectionIfMissing<IFeedbackResponse>(
      this.feedbackResponsesSharedCollection,
      ...(feedbackRequest.feedbackResponses ?? [])
    );
    this.groupsSharedCollection = this.groupsService.addGroupsToCollectionIfMissing<IGroups>(
      this.groupsSharedCollection,
      ...(feedbackRequest.groups ?? [])
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
            ...(this.feedbackRequest?.feedbackAboutUsers ?? []),
            ...(this.feedbackRequest?.feedbackToUsers ?? [])
          )
        )
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));

    this.feedbackAboutService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackAbout[]>) => res.body ?? []))
      .pipe(
        map((feedbackAbouts: IFeedbackAbout[]) =>
          this.feedbackAboutService.addFeedbackAboutToCollectionIfMissing<IFeedbackAbout>(
            feedbackAbouts,
            ...(this.feedbackRequest?.feedbackAbouts ?? [])
          )
        )
      )
      .subscribe((feedbackAbouts: IFeedbackAbout[]) => (this.feedbackAboutsSharedCollection = feedbackAbouts));

    this.feedbackResponseService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackResponse[]>) => res.body ?? []))
      .pipe(
        map((feedbackResponses: IFeedbackResponse[]) =>
          this.feedbackResponseService.addFeedbackResponseToCollectionIfMissing<IFeedbackResponse>(
            feedbackResponses,
            ...(this.feedbackRequest?.feedbackResponses ?? [])
          )
        )
      )
      .subscribe((feedbackResponses: IFeedbackResponse[]) => (this.feedbackResponsesSharedCollection = feedbackResponses));

    this.groupsService
      .query()
      .pipe(map((res: HttpResponse<IGroups[]>) => res.body ?? []))
      .pipe(
        map((groups: IGroups[]) =>
          this.groupsService.addGroupsToCollectionIfMissing<IGroups>(groups, ...(this.feedbackRequest?.groups ?? []))
        )
      )
      .subscribe((groups: IGroups[]) => (this.groupsSharedCollection = groups));
  }
}
