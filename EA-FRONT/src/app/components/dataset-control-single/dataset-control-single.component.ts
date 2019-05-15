import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Control } from '../../shared/models/control';
import { DatasetDesignService } from '../../services/dataset-design.service';


@Component({
  selector: '[app-dataset-control-single]',
  templateUrl: './dataset-control-single.component.html',
  styleUrls: ['./dataset-control-single.component.scss']
})


export class DatasetControlSingleComponent implements OnInit {
  @Input() control: Control;
  @Input() tabNbr: number;
  @Input() nameControl: string;
  @Input() study;
  @Output() show = new EventEmitter<any>() ;
  isOpned = false;

  showerror() {
    if ((this.control.errors > 0   || this.control.notExecuted ) && this.isOpned == false) {
      this.show.emit(true);
      this.isOpned = true;
    } else if (this.isOpned == true ) {
      this.show.emit(false);
      this.isOpned = false;
    }
  }
  
  constructor(private ds: DatasetDesignService) {}
  ngOnInit() {
  }
}
