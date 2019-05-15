import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-details-header',
  templateUrl: './details-header.component.html',
  styleUrls: ['./details-header.component.scss']
})
export class DetailsHeaderComponent implements OnInit {

  constructor() { }

  @Input() data:any[]

  ngOnInit() {
  }

}
