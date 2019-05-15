import {
  Component,
  OnInit,
  EventEmitter,
  Output,
  Input
} from '@angular/core';

@Component({
  selector: 'app-multiselect',
  templateUrl: './multiselect.component.html',
  styleUrls: ['./multiselect.component.scss']
})
export class MultiselectComponent implements OnInit {

  constructor() {}

  @Input()  model: any
  @Output() modelChange = new EventEmitter()

  @Input() btnClass = "btn btn-primary text-white"
  @Input() inputClass = "form-control form-control-sm study-search-multiselect"

  @Input() singleSelection = false
  @Input() idField = 'id'
  @Input() textField = 'text'
  @Input() selectAllText = 'All'
  @Input() unSelectAllText = 'None'
  @Input() itemsShowLimit = 1
  @Input() allowSearchFilter = false

  @Input() settings = {
    singleSelection: this.singleSelection,
    idField: this.idField,
    textField: this.textField,
    selectAllText: this.selectAllText,
    unSelectAllText: this.unSelectAllText,
    itemsShowLimit: this.itemsShowLimit,
    allowSearchFilter: this.allowSearchFilter,
  }

  @Input() items: any[]

  @Output() onDeSelect: EventEmitter < any > = new EventEmitter()
  @Output() onDeSelectAll: EventEmitter < any > = new EventEmitter()
  @Output() onSelect: EventEmitter < any > = new EventEmitter()
  @Output() onSelectAll: EventEmitter < any > = new EventEmitter()

  ngOnInit() {

    this.settings = {
      singleSelection: this.singleSelection,
      idField: this.idField,
      textField: this.textField,
      selectAllText: this.selectAllText,
      unSelectAllText: this.unSelectAllText,
      itemsShowLimit: this.itemsShowLimit,
      allowSearchFilter: this.allowSearchFilter,
    }
  }

  Select($event) {
    this.onSelect.emit($event)
  }
  SelectAll($event) {
    this.onSelectAll.emit($event)
  }
  DeSelect($event) {
    this.onDeSelect.emit($event)
  }
  DeSelectAll($event) {
    this.onDeSelectAll.emit($event)
  }
}
