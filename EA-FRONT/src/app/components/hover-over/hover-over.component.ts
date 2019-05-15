import { Component, OnInit, Input } from '@angular/core';
import { Label, SCREEN, HoverOverService } from './hover-over.service';

@Component({
  selector: 'app-hover-over',
  templateUrl: './hover-over.component.html',
  styleUrls: ['./hover-over.component.scss']
})
export class HoverOverComponent implements OnInit {

  constructor(private hs:HoverOverService) { }

  @Input() labelClass = "form-control-label from-control-label-sm font-weight-bold"
  @Input() screen = SCREEN.STUDY
  @Input() name= "demo"
  @Input() required= false

  ngOnInit() {
  }

  getDetail()
  {
    return this.hs.getDetail(this.screen,this.name)
  }
}
