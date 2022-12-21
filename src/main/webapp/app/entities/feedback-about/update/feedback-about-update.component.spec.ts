import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FeedbackAboutFormService } from './feedback-about-form.service';
import { FeedbackAboutService } from '../service/feedback-about.service';
import { IFeedbackAbout } from '../feedback-about.model';

import { FeedbackAboutUpdateComponent } from './feedback-about-update.component';

describe('FeedbackAbout Management Update Component', () => {
  let comp: FeedbackAboutUpdateComponent;
  let fixture: ComponentFixture<FeedbackAboutUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let feedbackAboutFormService: FeedbackAboutFormService;
  let feedbackAboutService: FeedbackAboutService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FeedbackAboutUpdateComponent],
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
      .overrideTemplate(FeedbackAboutUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackAboutUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    feedbackAboutFormService = TestBed.inject(FeedbackAboutFormService);
    feedbackAboutService = TestBed.inject(FeedbackAboutService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const feedbackAbout: IFeedbackAbout = { id: 456 };

      activatedRoute.data = of({ feedbackAbout });
      comp.ngOnInit();

      expect(comp.feedbackAbout).toEqual(feedbackAbout);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackAbout>>();
      const feedbackAbout = { id: 123 };
      jest.spyOn(feedbackAboutFormService, 'getFeedbackAbout').mockReturnValue(feedbackAbout);
      jest.spyOn(feedbackAboutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackAbout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackAbout }));
      saveSubject.complete();

      // THEN
      expect(feedbackAboutFormService.getFeedbackAbout).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(feedbackAboutService.update).toHaveBeenCalledWith(expect.objectContaining(feedbackAbout));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackAbout>>();
      const feedbackAbout = { id: 123 };
      jest.spyOn(feedbackAboutFormService, 'getFeedbackAbout').mockReturnValue({ id: null });
      jest.spyOn(feedbackAboutService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackAbout: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: feedbackAbout }));
      saveSubject.complete();

      // THEN
      expect(feedbackAboutFormService.getFeedbackAbout).toHaveBeenCalled();
      expect(feedbackAboutService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFeedbackAbout>>();
      const feedbackAbout = { id: 123 };
      jest.spyOn(feedbackAboutService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ feedbackAbout });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(feedbackAboutService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
