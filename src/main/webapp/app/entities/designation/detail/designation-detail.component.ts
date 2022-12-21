import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDesignation } from '../designation.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-designation-detail',
  templateUrl: './designation-detail.component.html',
})
export class DesignationDetailComponent implements OnInit {
  designation: IDesignation | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ designation }) => {
      this.designation = designation;
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
