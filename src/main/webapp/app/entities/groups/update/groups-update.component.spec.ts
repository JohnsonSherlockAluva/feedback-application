import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GroupsFormService } from './groups-form.service';
import { GroupsService } from '../service/groups.service';
import { IGroups } from '../groups.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';

import { GroupsUpdateComponent } from './groups-update.component';

describe('Groups Management Update Component', () => {
  let comp: GroupsUpdateComponent;
  let fixture: ComponentFixture<GroupsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let groupsFormService: GroupsFormService;
  let groupsService: GroupsService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GroupsUpdateComponent],
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
      .overrideTemplate(GroupsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GroupsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    groupsFormService = TestBed.inject(GroupsFormService);
    groupsService = TestBed.inject(GroupsService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const groups: IGroups = { id: 456 };
      const applicationUsers: IApplicationUser[] = [{ id: 33027 }];
      groups.applicationUsers = applicationUsers;

      const applicationUserCollection: IApplicationUser[] = [{ id: 20676 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [...applicationUsers];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ groups });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers.map(expect.objectContaining)
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const groups: IGroups = { id: 456 };
      const applicationUser: IApplicationUser = { id: 5290 };
      groups.applicationUsers = [applicationUser];

      activatedRoute.data = of({ groups });
      comp.ngOnInit();

      expect(comp.applicationUsersSharedCollection).toContain(applicationUser);
      expect(comp.groups).toEqual(groups);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroups>>();
      const groups = { id: 123 };
      jest.spyOn(groupsFormService, 'getGroups').mockReturnValue(groups);
      jest.spyOn(groupsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groups });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groups }));
      saveSubject.complete();

      // THEN
      expect(groupsFormService.getGroups).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(groupsService.update).toHaveBeenCalledWith(expect.objectContaining(groups));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroups>>();
      const groups = { id: 123 };
      jest.spyOn(groupsFormService, 'getGroups').mockReturnValue({ id: null });
      jest.spyOn(groupsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groups: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: groups }));
      saveSubject.complete();

      // THEN
      expect(groupsFormService.getGroups).toHaveBeenCalled();
      expect(groupsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGroups>>();
      const groups = { id: 123 };
      jest.spyOn(groupsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ groups });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(groupsService.update).toHaveBeenCalled();
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
  });
});
