import {
  Component,
  OnInit,
  Input,
  EventEmitter,Output
} from '@angular/core';
import {
  Run
} from '../../../shared/models/run';
import {
  Decrement
} from '../../../shared/models/decrement';
import {
  Study
} from '../../../shared/models/study';
import { FormValidatorService } from '../../../services/form-validator.service';
import { Dataset } from '../../../shared/models/dataset';


const D_SLICE_AGE = {
  value: "age",
  name: "Age"
}
const D_SLICE_MONTH = {
  value: "duration",
  name: "Duration"
}
const D_SLICE_CALENDAR = {
  value: "calendar",
  name: "Calendar Year"
}

const G_SLICE_AGE = {
  value: "annual",
  name: "Annual"
}
const G_SLICE_MONTH = {
  value: "monthly",
  name: "Monthly"
}

@Component({
  selector: 'app-run-decrement',
  templateUrl: './run-decrement.component.html',
  styleUrls: ['./run-decrement.component.scss']
})
export class RunDecrementComponent implements OnInit {

  constructor(public formValidation : FormValidatorService) {}

  @Input() study: Study
  @Input() run: Run
  @Input() tab: number = 0
  @Input() datasets: Dataset[]
  @Input() screen:number
  // @Input() editable: boolean;
  @Input() isPolicyDuration: boolean
  @Output() saveRun: EventEmitter<any> = new EventEmitter<any>()
  @Input() dimensions: string[]

  @Input() canEditRun : boolean;
  @Input() canEditPerm : boolean;




  decrements: Decrement[]
  component_number:number

  ngOnInit() {
    this.component_number  = RunDecrementComponent.NUMBER++;
    this.decrements = this.run.decrements || []
  }

  editable() {
    return this.canEditPerm && this.canEditRun
  }
 
  newDecrement() {
    if (this.canCreateDec())
      this.run.decrements.push(new Decrement(this.study))
  }

  deleteDecrement(index) {
    this.run.decrements.splice(index, 1)
  }

  canCreateDec(){
    if ( this.run.decrements.length >= this.run.decrementLimit )
      return false
    return true
  }

  saveRunFunction(){
    this.saveRun.emit(this.run)
  }

  static NUMBER = 0
}
