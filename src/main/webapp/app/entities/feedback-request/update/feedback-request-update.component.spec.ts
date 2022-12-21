import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeedbackRequestFormService } from './feedback-request-form.service';
import { FeedbackRequestService } from '../service/feedback-request.service';
import { IFeedbackRequest } from '../feedback-request.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { IFeedbackAbout } from 'app/entities/feedback-about/feedback-about.model';
import { FeedbackAboutService } from 'app/entities/feedback-about/service/feedback-about.service';
import { IFeedbackResponse } from 'app/entities/feedback-response/feedback-response.model';
import { FeedbackResponseService } from 'app/entities/feedback-response/service/feedback-response.service';
import { IGroups } from 'app/entities/groups/groups.model';
import { GroupsService } from 'app/entities/groups/service/groups.service';

import { FeedbackRequestUpdateComponent } from './feedback-request-update.component';

describe('FeedbackRequest Management Update Component', () => {
  let comp: FeedbackRequestUpdateComponent;
  let fixture: ComponentFixture<FeedbackRequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackRequestFormService: FeedbackRequestFormService;
  let feedbackRequestService: FeedbackRequestService;
  let applicationUserService: ApplicationUserService;
  let feedbackAboutService: FeedbackAboutService;
  let feedbackResponseService: FeedbackResponseService;
  let groupsService: GroupsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeedbackRequestUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FeedbackRequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackRequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackRequestFormService = TestBed.inject(FeedbackRequestFormService);
    feedbackRequestService = TestBed.inject(FeedbackRequestService);
    applicationUserService = TestBed.inject(ApplicationUserService);
    feedbackAboutService = TestBed.inject(FeedbackAboutService);
    feedbackResponseService = TestBed.inject(FeedbackResponseService);
    groupsService = TestBed.inject(GroupsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const feedbackRequest: IFeedbackRequest = { id: 456 };
      const feedbackAboutUsers: IApplicationUser[] = [{ id: 38347 }];
      feedbackRequest.feedbackAboutUsers = feedbackAboutUsers;
      const feedbackToUsers: IApplicationUser[] = [{ id: 6536 }];
      feedbackRequest.feedbackToUsers = feedbackToUsers;

      const applicationUserCollection: IApplicationUser[] = [{ id: 20764 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [...feedbackAboutUsers, ...feedbackToUsers];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining)
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FeedbackAbout query and add missing value', () => {
      const feedbackRequest: IFeedbackRequest = { id: 456 };
      const feedbackAbouts: IFeedbackAbout[] = [{ id: 55418 }];
      feedbackRequest.feedbackAbouts = feedbackAbouts;

      const feedbackAboutCollection: IFeedbackAbout[] = [{ id: 25585 }];
      jest.spyOn(feedbackAboutService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackAboutCollection })));
      const additionalFeedbackAbouts = [...feedbackAbouts];
      const expectedCollection: IFeedbackAbout[] = [...additionalFeedbackAbouts, ...feedbackAboutCollection];
      jest.spyOn(feedbackAboutService, 'addFeedbackAboutToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      expect(feedbackAboutService.query).toHaveBeenCalled();
      expect(feedbackAboutService.addFeedbackAboutToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackAboutCollection,
        ...additionalFeedbackAbouts.map(expect.objectContaining)
      );
      expect(comp.feedbackAboutsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FeedbackResponse query and add missing value', () => {
      const feedbackRequest: IFeedbackRequest = { id: 456 };
      const feedbackResponses: IFeedbackResponse[] = [{ id: 36598 }];
      feedbackRequest.feedbackResponses = feedbackResponses;

      const feedbackResponseCollection: IFeedbackResponse[] = [{ id: 99313 }];
      jest.spyOn(feedbackResponseService, 'query').mockReturnValue(of(new HttpResponse({ body: feedbackResponseCollection })));
      const additionalFeedbackResponses = [...feedbackResponses];
      const expectedCollection: IFeedbackResponse[] = [...additionalFeedbackResponses, ...feedbackResponseCollection];
      jest.spyOn(feedbackResponseService, 'addFeedbackResponseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      expect(feedbackResponseService.query).toHaveBeenCalled();
      expect(feedbackResponseService.addFeedbackResponseToCollectionIfMissing).toHaveBeenCalledWith(
        feedbackResponseCollection,
        ...additionalFeedbackResponses.map(expect.objectContaining)
      );
      expect(comp.feedbackResponsesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Groups query and add missing value', () => {
      const feedbackRequest: IFeedbackRequest = { id: 456 };
      const groups: IGroups[] = [{ id: 4380 }];
      feedbackRequest.groups = groups;

      const groupsCollection: IGroups[] = [{ id: 17526 }];
      jest.spyOn(groupsService, 'query').mockReturnValue(of(new HttpResponse({ body: groupsCollection })));
      const additionalGroups = [...groups];
      const expectedCollection: IGroups[] = [...additionalGroups, ...groupsCollection];
      jest.spyOn(groupsService, 'addGroupsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      expect(groupsService.query).toHaveBeenCalled();
      expect(groupsService.addGroupsToCollectionIfMissing).toHaveBeenCalledWith(
        groupsCollection,
        ...additionalGroups.map(expect.objectContaining)
      );
      expect(comp.groupsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const feedbackRequest: IFeedbackRequest = { id: 456 };
      const feedbackAboutUsers: IApplicationUser = { id: 57268 };
      feedbackRequest.feedbackAboutUsers = [feedbackAboutUsers];
      const feedbackToUsers: IApplicationUser = { id: 90540 };
      feedbackRequest.feedbackToUsers = [feedbackToUsers];
      const feedbackAbout: IFeedbackAbout = { id: 883 };
      feedbackRequest.feedbackAbouts = [feedbackAbout];
      const feedbackResponse: IFeedbackResponse = { id: 2266 };
      feedbackRequest.feedbackResponses = [feedbackResponse];
      const groups: IGroups = { id: 91635 };
      feedbackRequest.groups = [groups];

      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(feedbackAboutUsers);
      expect(comp.applicationUsersSharedCollection).toContain(feedbackToUsers);
      expect(comp.feedbackAboutsSharedCollection).toContain(feedbackAbout);
      expect(comp.feedbackResponsesSharedCollection).toContain(feedbackResponse);
      expect(comp.groupsSharedCollection).toContain(groups);
      expect(comp.feedbackRequest).toEqual(feedbackRequest);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackRequest>>();
      const feedbackRequest = { id: 123 };
      jest.spyOn(feedbackRequestFormService, 'getFeedbackRequest').mockReturnValue(feedbackRequest);
      jest.spyOn(feedbackRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackRequest }));
      saveSubject.complete();

      // THEN
      expect(feedbackRequestFormService.getFeedbackRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackRequestService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackRequest));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackRequest>>();
      const feedbackRequest = { id: 123 };
      jest.spyOn(feedbackRequestFormService, 'getFeedbackRequest').mockReturnValue({ id: null });
      jest.spyOn(feedbackRequestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackRequest: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackRequest }));
      saveSubject.complete();

      // THEN
      expect(feedbackRequestFormService.getFeedbackRequest).toHaveBeenCalled();
      expect(feedbackRequestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackRequest>>();
      const feedbackRequest = { id: 123 };
      jest.spyOn(feedbackRequestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackRequest });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackRequestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareApplicationUser', () => {
      it('Should forward to applicationUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(applicationUserService, 'compareApplicationUser');
        comp.compareApplicationUser(entity, entity2);
        expect(applicationUserService.compareApplicationUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFeedbackAbout', () => {
      it('Should forward to feedbackAboutService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackAboutService, 'compareFeedbackAbout');
        comp.compareFeedbackAbout(entity, entity2);
        expect(feedbackAboutService.compareFeedbackAbout).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFeedbackResponse', () => {
      it('Should forward to feedbackResponseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(feedbackResponseService, 'compareFeedbackResponse');
        comp.compareFeedbackResponse(entity, entity2);
        expect(feedbackResponseService.compareFeedbackResponse).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGroups', () => {
      it('Should forward to groupsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(groupsService, 'compareGroups');
        comp.compareGroups(entity, entity2);
        expect(groupsService.compareGroups).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
