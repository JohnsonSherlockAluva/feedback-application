import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DesignationService } from '../service/designation.service';

import { DesignationComponent } from './designation.component';

describe('Designation Management Component', () => {
  let comp: DesignationComponent;
  let fixture: ComponentFixture<DesignationComponent>;
  let service: DesignationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'designation', component: DesignationComponent }]), HttpClientTestingModule],
      declarations: [DesignationComponent],
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
      .overrideTemplate(DesignationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DesignationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DesignationService);

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
    expect(comp.designations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to designationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDesignationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDesignationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
