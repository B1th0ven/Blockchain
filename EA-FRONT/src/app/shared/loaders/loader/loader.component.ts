import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit {

  @Input() type:number=1
  @Input() color:string="primary"
  
  constructor() { }

  ngOnInit() {
  }

}
