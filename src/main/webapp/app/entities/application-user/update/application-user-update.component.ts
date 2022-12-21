import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ApplicationUserFormService, ApplicationUserFormGroup } from './application-user-form.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';

@Component({
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;
  applicationUser: IApplicationUser | null = null;

  usersSharedCollection: IUser[] = [];
  designationsSharedCollection: IDesignation[] = [];

  editForm: ApplicationUserFormGroup = this.applicationUserFormService.createApplicationUserFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected applicationUserService: ApplicationUserService,
    protected applicationUserFormService: ApplicationUserFormService,
    protected userService: UserService,
    protected designationService: DesignationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareDesignation = (o1: IDesignation | null, o2: IDesignation | null): boolean => this.designationService.compareDesignation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.applicationUser = applicationUser;
      if (applicationUser) {
        this.updateForm(applicationUser);
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
    const applicationUser = this.applicationUserFormService.getApplicationUser(this.editForm);
    if (applicationUser.id !== null) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
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

  protected updateForm(applicationUser: IApplicationUser): void {
    this.applicationUser = applicationUser;
    this.applicationUserFormService.resetForm(this.editForm, applicationUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      applicationUser.internalUser
    );
    this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
      this.designationsSharedCollection,
      ...(applicationUser.designations ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.applicationUser?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.designationService
      .query()
      .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
      .pipe(
        map((designations: IDesignation[]) =>
          this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
            designations,
            ...(this.applicationUser?.designations ?? [])
          )
        )
      )
      .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));
  }
}
