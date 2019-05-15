import { Component, OnInit, Input } from '@angular/core';
import { Control } from '../../shared/models/control';
import { DatasetDesignService } from '../../services/dataset-design.service';
import { Dataset } from '../../shared/models/dataset';

@Component({
  selector: '[app-dataset-control-management]',
  templateUrl: './dataset-control-management.component.html',
  styleUrls: ['./dataset-control-management.component.scss']
})
export class DatasetControlManagementComponent implements OnInit {
  @Input() control: Control;
  @Input() tabNbr: number;
  @Input() index;
  @Input() controls: any;
  @Input() productHeader;
  @Input() policyHeader;
  @Input() study;
  @Input() dataset: Dataset ;
  showobject: any;
  isOpen = false;
  constructor(public dds: DatasetDesignService) {
   }

  setshow(showobject: any) {
    this.showobject = showobject ;
  }
  ngOnInit() {}

}
