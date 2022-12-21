import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FeedbackResponseService } from '../service/feedback-response.service';

import { FeedbackResponseComponent } from './feedback-response.component';

describe('FeedbackResponse Management Component', () => {
  let comp: FeedbackResponseComponent;
  let fixture: ComponentFixture<FeedbackResponseComponent>;
  let service: FeedbackResponseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'feedback-response', component: FeedbackResponseComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [FeedbackResponseComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FeedbackResponseComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FeedbackResponseComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FeedbackResponseService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.feedbackResponses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to feedbackResponseService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFeedbackResponseIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFeedbackResponseIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
