import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDesignation } from '../designation.model';
import { DesignationService } from '../service/designation.service';

@Injectable({ providedIn: 'root' })
export class DesignationRoutingResolveService implements Resolve<IDesignation | null> {
  constructor(protected service: DesignationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDesignation | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((designation: HttpResponse<IDesignation>) => {
          if (designation.body) {
            return of(designation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
