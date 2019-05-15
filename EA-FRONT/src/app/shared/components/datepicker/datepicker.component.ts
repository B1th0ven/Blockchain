import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.scss']
})
export class DatepickerComponent implements OnInit {

  @Input()  model: any
  @Output() modelChange = new EventEmitter()

  @Input() btnClass = "btn btn-primary text-white"
  @Input() inputClass = "form-control form-control-sm study-search-multiselect"

  constructor() { }

  ngOnInit() {
  }

}
