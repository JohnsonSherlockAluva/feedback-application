import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGroups } from '../groups.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-groups-detail',
  templateUrl: './groups-detail.component.html',
})
export class GroupsDetailComponent implements OnInit {
  groups: IGroups | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ groups }) => {
      this.groups = groups;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
