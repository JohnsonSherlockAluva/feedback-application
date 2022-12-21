import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDesignation, NewDesignation } from '../designation.model';

export type PartialUpdateDesignation = Partial<IDesignation> & Pick<IDesignation, 'id'>;

export type EntityResponseType = HttpResponse<IDesignation>;
export type EntityArrayResponseType = HttpResponse<IDesignation[]>;

@Injectable({ providedIn: 'root' })
export class DesignationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/designations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(designation: NewDesignation): Observable<EntityResponseType> {
    return this.http.post<IDesignation>(this.resourceUrl, designation, { observe: 'response' });
  }

  update(designation: IDesignation): Observable<EntityResponseType> {
    return this.http.put<IDesignation>(`${this.resourceUrl}/${this.getDesignationIdentifier(designation)}`, designation, {
      observe: 'response',
    });
  }

  partialUpdate(designation: PartialUpdateDesignation): Observable<EntityResponseType> {
    return this.http.patch<IDesignation>(`${this.resourceUrl}/${this.getDesignationIdentifier(designation)}`, designation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDesignation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDesignation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDesignationIdentifier(designation: Pick<IDesignation, 'id'>): number {
    return designation.id;
  }

  compareDesignation(o1: Pick<IDesignation, 'id'> | null, o2: Pick<IDesignation, 'id'> | null): boolean {
    return o1 && o2 ? this.getDesignationIdentifier(o1) === this.getDesignationIdentifier(o2) : o1 === o2;
  }

  addDesignationToCollectionIfMissing<Type extends Pick<IDesignation, 'id'>>(
    designationCollection: Type[],
    ...designationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const designations: Type[] = designationsToCheck.filter(isPresent);
    if (designations.length > 0) {
      const designationCollectionIdentifiers = designationCollection.map(
        designationItem => this.getDesignationIdentifier(designationItem)!
      );
      const designationsToAdd = designations.filter(designationItem => {
        const designationIdentifier = this.getDesignationIdentifier(designationItem);
        if (designationCollectionIdentifiers.includes(designationIdentifier)) {
          return false;
        }
        designationCollectionIdentifiers.push(designationIdentifier);
        return true;
      });
      return [...designationsToAdd, ...designationCollection];
    }
    return designationCollection;
  }
}
