import {
  Component,
  OnInit,
  EventEmitter,
  Output,
  Input,
  ChangeDetectionStrategy,
  OnChanges,
  ElementRef
} from '@angular/core';

@Component({
  selector: 'app-dropdown',
  templateUrl: './dropdown.component.html',
  changeDetection: ChangeDetectionStrategy.Default
})
export class DropdownComponent implements OnInit{

  constructor(private elementRef:ElementRef) {}

  @Input("model") set model(m: any) {
    // TODO CHANGE TO CECH FOR EACH ELEMENT IN MIDDLE MAN IS EQUAL TO THE MODEL
    // AND REMOVE THE BOOLEAN VALUE
    if ( this.gotSelection )
    {
      this.gotSelection = false;
      return;
    }

    if (m)
    {
      console.log("CHANGING STUFF")
      if (this.singleSelection)
      {
        this.middleMan = [m];
      }
      else
      {
        this.middleMan = m;
      }

    }
  };
  @Output() modelChange = new EventEmitter()

  middleMan:any
  middleItems:any[]

  @Input("items") set items(items:any[])
  {
    if (this.middleMan && items )
    {
      this.middleMan = this.middleMan.filter(m => items.find( i=> i[this.idField] == m[this.idField] ))
    }
    this.middleItems = items
  };

  @Input() btnClass = "btn btn-primary text-white"
  @Input() inputClass = "form-control form-control-sm study-search-multiselect"

  @Input() singleSelection = true
  @Input() disabled:boolean = false;
  @Input() idField = 'key'
  @Input() textField = 'value'
  @Input() selectAllText = 'All'
  @Input() unSelectAllText = 'None'
  @Input() itemsShowLimit = 0
  @Input() allowSearchFilter = true
  @Input() colorBorder: boolean = false;


  @Input() settings = {

    singleSelection: this.singleSelection,
    idField: this.idField,
    textField: this.textField,
    selectAllText: this.selectAllText,
    unSelectAllText: this.unSelectAllText,
    itemsShowLimit: this.itemsShowLimit,
    allowSearchFilter: this.allowSearchFilter,
  }

  @Output() onDeSelect: EventEmitter < any > = new EventEmitter()
  @Output() onDeSelectAll: EventEmitter < any > = new EventEmitter()
  @Output() onSelect: EventEmitter < any > = new EventEmitter()
  @Output() onSelectAll: EventEmitter < any > = new EventEmitter()
  @Output() change: EventEmitter < any > = new EventEmitter()


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

  Select(event:any) {
    this.onSelect.emit(event);
  }
  SelectAll(event:any) {
    this.onSelectAll.emit(event)
  }
  DeSelect(event:any) {
    this.onDeSelect.emit(event)
  }
  DeSelectAll(event:any) {
    this.onDeSelectAll.emit(event)
  }

  gotSelection = false
  getSelection()
  {
    this.gotSelection = true
    //SINGLE SELECT
    if ( this.singleSelection ){
      if (this.middleMan && this.middleMan.length >=1){
        return (this.middleItems)? this.middleItems.find(i=> i[this.idField] == this.middleMan[0][this.idField] ):null
      }else return null;
    }
    //MULTISELEDCT
    else{
      console.log("SELECTED",this.middleMan)
      if (this.middleMan && this.middleItems)
      {
        return this.middleItems.filter(i => (this.middleMan)? this.middleMan.find( m=> i[this.idField] == m[this.idField] ):false)
      }else{
        return []
      }
    }
  }

  onNgModelChange(event: any){
    this.modelChange.emit(this.getSelection());
    this.change.emit(this.getSelection())
  }

  getTitle(model: any){ return (model)?model.value:null }

  onDropdownShown() {
    let focus = this.elementRef.nativeElement.getElementsByClassName('filter-textbox')
    if ( focus ) focus[0].childNodes[1].focus()
  }
  
  
}
