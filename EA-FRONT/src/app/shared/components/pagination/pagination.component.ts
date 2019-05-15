import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  constructor() { }

  @Output() onSelected      : EventEmitter<any> = new EventEmitter()
  @Output() onSizeSelected    : EventEmitter<any> = new EventEmitter()


  ngOnInit() {
  }

  @Input() selected = 1
  @Input() totalPages = 1

  @Input() bPageSize:boolean = true
  @Input() pageSize = 10

  first()
  {
    this.selected = 1;
    this.onSelected.emit(this.selected)
  }

  last()
  {
    this.selected = this.totalPages
    this.onSelected.emit(this.selected)
  }

  next()
  {
    this.selected = Math.min( this.selected + 1 , this.totalPages )
    this.onSelected.emit(this.selected)
  }

  previous()
  {
    this.selected = Math.max( this.selected - 1 , 1 )
    this.onSelected.emit(this.selected)
  }

  goTo(pageNumber)
  {
    this.selected = pageNumber
    this.onSelected.emit(this.selected)
  }

  pageSizeChange()
  {
    this.onSizeSelected.emit(this.pageSize)
  }

  getPagesArray(totalPages)
  {
    let ps = new Array()
    for(let i = 0 ; i < totalPages; i++) ps.push(i+1)
    return ps;
  }

}
