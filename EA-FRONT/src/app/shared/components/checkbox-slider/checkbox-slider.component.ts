import { Component, OnInit, Input, Output , EventEmitter} from '@angular/core';

@Component({
  selector: 'app-checkbox-slider',
  templateUrl: './checkbox-slider.component.html',
  styleUrls: ['./checkbox-slider.component.scss']
})
export class CheckboxSliderComponent implements OnInit {
 @Input() checked:boolean
 @Input() disabled:boolean
 @Output() valueChange: EventEmitter<boolean> = new EventEmitter()

 comp_numb

  constructor() { }

  ngOnInit() {
    this.comp_numb = CheckboxSliderComponent.COMP_NO_slider++
  }

  checkValue(e){
    //console.log(e.currentTarget.checked)
    this.valueChange.emit(e.currentTarget.checked)
  }

   static COMP_NO_slider = 0;
}
