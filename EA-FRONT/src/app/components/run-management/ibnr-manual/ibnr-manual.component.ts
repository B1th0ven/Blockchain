import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-ibnr-manual',
  templateUrl: './ibnr-manual.component.html',
  styleUrls: ['./ibnr-manual.component.scss']
})
export class IbnrManualComponent implements OnInit {

  @Output() close : EventEmitter<any> = new EventEmitter<any>()
  @Output() save : EventEmitter<any> = new EventEmitter<any>()

  IBNRs = [
    {name:"IBNR",file:"",status:"None"}
  ]

  constructor() { }

  ngOnInit() {
  }

  onSave()
  {
    this.save.emit()
  }

  onClose()
  {
    this.close.emit()
  }
}
