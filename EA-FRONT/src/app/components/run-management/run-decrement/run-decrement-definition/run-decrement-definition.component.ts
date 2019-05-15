import {
  Component,
  OnInit,
  Input,
  ViewChild,
  ElementRef,
  Output,
  EventEmitter
} from '@angular/core';
import {
  Decrement
} from '../../../../shared/models/decrement';
import {
  Study
} from '../../../../shared/models/study';
import {
  date
} from '../../../../shared/models/date';
import {
  FormValidatorService
} from '../../../../services/form-validator.service';
import {
  Run
} from '../../../../shared/models/run';
import {
  Dataset
} from '../../../../shared/models/dataset';
import * as options from '../../run-options'
import { NgbModal } from '../../../../../../node_modules/@ng-bootstrap/ng-bootstrap';
import { RunService } from '../../../../services/run.service';
import { ToasterService } from '../../../../../../node_modules/angular5-toaster/dist/angular5-toaster';
import { SCREEN,HoverOverService } from '../../../hover-over/hover-over.service';


const D_SLICE_AGE = {
  value: "age",
  name: "Age",
  isDisabled: true,
  index : 0
}
const D_SLICE_MONTH = {
  value: "duration",
  name: "Duration",
  isDisabled: false,
  index : 1

}
const D_SLICE_CALENDAR = {
  value: "calendar",
  name: "Calendar Year",
  isDisabled: false,
  index : 2

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
  selector: 'app-run-decrement-definition',
  templateUrl: './run-decrement-definition.component.html',
  host: {
    '(document:click)': 'hostClick($event)',
  },
  styleUrls: ['./run-decrement-definition.component.scss']
})
export class RunDecrementDefinitionComponent implements OnInit {

  @Input() decrement: Decrement
  @Input() run: Run
  @Input() study: Study
  @Input() tab: number = 0
  @Input() ind: number = 0
  @Input() datasets: Dataset[]
  // @Input() editable: boolean
  @Output() saveRun: EventEmitter<any> = new EventEmitter<any>()
  @Input() dimensions: string[]
  @Input() pduration: boolean

  @Input() canEditRun : boolean;
  @Input() canEditPerm : boolean;


  @ViewChild("d1") d1: any
  @ViewChild("d2") d2: any
  @ViewChild('d1Container') d1Container: ElementRef;
  @ViewChild('d2Container') d2Container: ElementRef;

  slicingDimensions = [
    D_SLICE_AGE, D_SLICE_MONTH, D_SLICE_CALENDAR
  ]

  slicingGranularity = [
    G_SLICE_AGE
  ]

  attainedAgeInExpected = false
  InsuranceAgeAttainedInExpected = false
  durationYearInExpected = false

  decrementOptions:Array<any>

  monthlyLeader = []

  screen_name = SCREEN.RUN

  component_number:number
  constructor(private modalService: NgbModal,private rs: RunService, private tsr: ToasterService,public hs:HoverOverService) {}

  ngOnInit() {
    this.component_number = RunDecrementDefinitionComponent.NUMBER++;
    if (!this.decrement) this.decrement = new Decrement(this.study,this.run)
    this.updateOptions()

    this.rs.getValidatedDecrement(this.run.dataset,options.getEngineDecrementsById(1)).subscribe(res => {
      this.decrementOptions = res
    }, err => {
      this.decrementOptions = options.getEngineDecrementsById(1)
    })
    //console.log("/////////////////////decdec", this.pduration)
   // console.log("dec ------------------->",this.decrement)
    this.isAgeAttainedInExpTable()
   }

   editable() {
    return this.canEditPerm && this.canEditRun
  }

   isAgeAttainedInExpTable () {
    this.rs.isAttainedAgeCompulsory(this.ExpectedTableToListOfIds()).subscribe(
      res=>{console.log("checking expected table",res);
        this.attainedAgeInExpected = res.age_attained
        this.InsuranceAgeAttainedInExpected = res.insurance_age_attained
        this.durationYearInExpected = res.duration_year
      },
      err => console.log(err)
    )
   }

   sliceDinmension(value) {
    if(value == D_SLICE_AGE.value) return D_SLICE_AGE
    if(value == D_SLICE_CALENDAR.value) return D_SLICE_CALENDAR
    if(value == D_SLICE_MONTH.value) return D_SLICE_MONTH
   }

  onSlicingChange(value, checked) {
    if (checked)
    {
      this.decrement.slicingDimension.push(value)
      this.decrement.slicingDimension.sort((a,b) : number => {        
        let A = this.sliceDinmension(a)
        let B = this.sliceDinmension(b)
        return A.index - B.index;
      })
      if ( value == D_SLICE_AGE.value ) this.decrement.attainedAgeDefinition =  this.decrement.attainedAgeDefinition || "Age Last Birthday"
      
      if ( !this.decrement.leadingSlicingDimension ) this.decrement.leadingSlicingDimension = ( this.decrement.slicingDimension.length > 0)?this.decrement.slicingDimension[0]:null;
    }
    else
    {
      this.decrement.slicingDimension.splice(this.decrement.slicingDimension.indexOf(value), 1)

      if ( this.decrement.leadingSlicingDimension == value )
      {
        this.decrement.leadingSlicingDimension = ( this.decrement.slicingDimension.length > 0)?this.decrement.slicingDimension[0]:null;
      }

      if ( value == D_SLICE_AGE.value ) this.decrement.attainedAgeDefinition =  null;
    }

    this.updateOptions()
  }

  canSelectMonthly = () => {
    return (this.decrement.slicingDimension.indexOf("duration") > -1 || this.decrement.slicingDimension.indexOf("calendar") > -1)
  }

  updateOptions() {
    if(this.run.is_date_of_commencement) {
          // this.decrement.slicingDimension.splice(this.decrement.slicingDimension.indexOf("duration"))
          this.decrement.slicingDimension = this.decrement.slicingDimension.filter(value => value!=D_SLICE_MONTH.value)

    }
    if(this.run.age_of_birth_check) {
      // this.decrement.slicingDimension.splice(this.decrement.slicingDimension.indexOf("age"))
      this.decrement.slicingDimension = this.decrement.slicingDimension.filter(value => value!=D_SLICE_AGE.value)
}

    if (!this.canSelectMonthly()) {
      this.slicingGranularity = [G_SLICE_AGE]
      this.decrement.slicingGranularity = G_SLICE_AGE.value
    } else {
      this.slicingGranularity = [
        G_SLICE_AGE, G_SLICE_MONTH
      ]

      this.monthlyLeader = []
      if (this.decrement.slicingDimension.indexOf("duration") > -1) this.monthlyLeader.push(D_SLICE_MONTH)
      if (this.decrement.slicingDimension.indexOf("calendar") > -1) this.monthlyLeader.push(D_SLICE_CALENDAR)
      if (this.monthlyLeader.length > 0) this.decrement.monthlyDurationLeader = this.monthlyLeader[0].value
    }
    if(this.run.age_of_birth_check) {
      this.decrement.slicingDimension = this.decrement.slicingDimension.filter(value => value!=D_SLICE_AGE.value)
    }
    if ( !this.decrement.leadingSlicingDimension ) this.decrement.leadingSlicingDimension = ( this.decrement.slicingDimension.length > 0)?this.decrement.slicingDimension[0]:null;
  }

  onLeadingSlicingChange(value) {
    this.decrement.leadingSlicingDimension = value
  }

  onGranularityChange(value) {
    this.decrement.slicingGranularity = value
  }

  MonthlyDurationEnabled = () => this.decrement.slicingGranularity == 'monthly'

  validDate(endDate: date) {
    let d = this.datasets.find(d => d.id == this.run.dataset);
    if(!d) return false

    if (this.isDateNull(d.exposureExtractionDate) && this.isDateNull(d.eventExtractionDate))
      return true;
    else if (this.isDateNull(d.exposureExtractionDate) && !this.isDateNull(d.eventExtractionDate))
      return this.transformDate(endDate) < this.transformDate(d.eventExtractionDate)
    else if (!this.isDateNull(d.exposureExtractionDate) && this.isDateNull(d.eventExtractionDate))
      return this.transformDate(endDate) < this.transformDate(d.exposureExtractionDate)
    else {
      let minDate;
      if (this.transformDate(d.exposureExtractionDate) < this.transformDate(d.eventExtractionDate))
        minDate = this.transformDate(d.exposureExtractionDate)
      else
        minDate = this.transformDate(d.eventExtractionDate)

      if (this.transformDate(endDate) < minDate)
        return true
      else
        return false;
    }

  }

  transformDate(val: date): Date {
    if (!val) return null;
    return new Date(val.year + '-' + val.month + '-' + val.day)
  }

  isDateNull(supDate: date) {
    if (isNaN(supDate.year) || isNaN(supDate.day) || isNaN(supDate.month)) return true
  }

  validDateLowSup(lowDate: date, supDate: date) {
    if (!lowDate || !supDate) return false;
    if (isNaN(supDate.year) || isNaN(supDate.day) || isNaN(supDate.month)) return true
    if (isNaN(lowDate.year) || isNaN(lowDate.day) || isNaN(lowDate.month)) return true

    let low = new Date(lowDate.year + '-' + lowDate.month + '-' + lowDate.day)
    let sup = new Date(supDate.year + '-' + supDate.month + '-' + supDate.day)

    if (!isNaN(low.getTime()) &&
      !isNaN(sup.getTime()) &&
      low <= sup
    ) return true;
    else return false
  }

  enablePartial = (): boolean => {
    if (this.decrement.leadingSlicingDimension == D_SLICE_MONTH.value) {
      this.decrement.partialDuration = false
      return false
    }
    return true
  }

  checkedSlicing(val: boolean) {
    if (!val)
      return true
    else
      return !this.run.age_of_birth_check
  }

  checkedLSlicing(check: boolean, val: string) {
    if ( this.decrement.leadingSlicingDimension == val ) return true; else return false;
  }

  hostClick(event) {
    if (this.d1.isOpen()) {
      if (this.d1Container && this.d1Container.nativeElement && !this.d1Container.nativeElement.contains(event.target)) {
        this.d1.close();
      }
    }
    if (this.d2.isOpen()) {
      if (this.d2Container && this.d2Container.nativeElement && !this.d2Container.nativeElement.contains(event.target)) {
        this.d2.close();
      }
    }
  }


  getUnselected(decrements: string[], run: Run,dec:string): any {
   return  (decrements)? decrements.filter(decrement=> run && run.decrements && ( decrement == dec || decrement != dec && this.checkRunDecrements(decrement,run)) ):null;
  }

  checkRunDecrements(dec,run:Run){
    if(dec == "wx") return run.decrements.findIndex(d=>d.decrement == dec) < 0
    if(dec == "ix" || dec == "qx") 
      return run.decrements.findIndex(d=>d.decrement == dec) < 0 && run.decrements.findIndex(d=>d.decrement == "ix+qx") < 0
    if(dec == "ix+qx") return run.decrements.findIndex(d=>d.decrement == dec) < 0 && run.decrements.findIndex(d=>d.decrement == "ix") < 0 && run.decrements.findIndex(d=>d.decrement == "qx") < 0
    
  }

  open(Expected,Calibration) {
    switch(this.decrement.ExpMethod){
      case "default_method":
      {
        if(this.run.id || (this.study.status && this.study.status.toLowerCase().trim() == "validated")) {
          this.modalService.open(Expected, {
            size: "lg",
            backdrop: 'static',
            keyboard  : false
          });
          break;
        }
         this.rs.saveExp(this.run).subscribe(
          res => {
            this.run.id = res.id
            this.run.decrements.forEach((decrement, i) => {
             decrement.id = res.decrements[i].id
             this.saveRun.emit(this.run)
             });
              this.modalService.open(Expected, {
                size: "lg",
                backdrop: 'static',
                keyboard  : false
              });
             this.tsr.pop("success", "Success", "Run saved")

          },err => {this.tsr.pop("error", "Error", "Run not saved")
        }
        )
         break;
      }
      case "us_method":
      {
        this.modalService.open(Calibration, {
          size: "lg",
          backdrop: 'static',
          keyboard  : false
        }); break;
      }
      default: break;
    }    

  }

  isExpDisabled(){
    return this.run.canSaveRun &&  this.decrement.ExpMethod && this.decrement.ExpMethod != ""

    // return this.decrement.ExpMethod == "" || this.decrement.ExpMethod == undefined || this.decrement.decrement == "" || this.decrement.decrement == undefined || this.run.id == undefined
    
  }

  isExpSelectDisabled(){
  return this.decrement.decrement == "" || this.decrement.decrement == undefined
  }

  
  disableAgeAttained()
  {
    let b = this.run.age_of_birth_check || ( this.decrement.slicingDimension && this.decrement.slicingDimension.indexOf('age') < 0 )
    if ( b ) this.decrement.attainedAgeDefinition = null;
    return b
  }

  isSlcicingDimension(value)
  {
    if ( value == D_SLICE_AGE.value ) return ( !this.run.age_of_birth_check &&  (this.decrement.slicingDimension && this.decrement.slicingDimension.indexOf(value) >= 0 ))
    return ( this.decrement.slicingDimension && this.decrement.slicingDimension.indexOf(value) >= 0 );
  }

  onMonthlyChanged(value)
  {
    if (value != 'duration')
      this.decrement.monthlyDuration = null;
  }

  isPolicyDurationCompulsory(){
    let b = this.dimensions.find(d => d.trim().toLocaleLowerCase() == "duration_year")
    if(b) return true
    else false
  }

  isAttainedAgeCompulsory(){
    let b = this.dimensions.find(d => d.trim().toLocaleLowerCase() == "attained_age")
    if(b) return true
    else false 
  }


  isSlicingDemensionChecked(slice,pduration) {
    // if(slice.value == "calendar") return true
    if(slice.value == "duration" && ((this.run.isDateOfCommencement && this.decrement.ExpCalibrationUrl) || pduration || this.durationYearInExpected)) return true
    /*if(slice.value == "duration" && this.run.is_date_of_commencement){
      // this.decrement.slicingDimension.splice(this.decrement.slicingDimension.indexOf("duration"))
      return false
    } */
    if(slice.value == "duration" && this.run.automatic_risk_amount) return true
    if(slice.value == "age" && this.InsuranceAgeAttainedInExpected) return false
    return this.decrement.slicingDimension.indexOf(slice.value)>=0 && this.checkedSlicing(slice.isDisabled) || (slice.value == "duration" && pduration)
  }

  ExpectedTableToListOfIds(){
    let list:any[] = []
    this.decrement.ExpectedTable.forEach(s=>{
        if(s.id_base) list.push(s.id_base)
        if(s.id_adjustment) list.push(s.id_adjustment)
        if(s.id_trend) list.push(s.id_trend)

    })
    return list
  }

  onExpMethChange(e){
    //console.log("Expected Method---------->",e.target.value)
    if( e.target.value != "default_method") {
      this.InsuranceAgeAttainedInExpected = false
      this.decrement.ExpectedTable = []
    }
  }

  isSlicingDimensionDisabled(slice){
    if(slice.isDisabled && this.run.age_of_birth_check)
      return true
    switch(slice.value) {
      case 'duration':
        return this.run.automatic_risk_amount || this.decrement.ExpCalibrationUrl || this.run.is_date_of_commencement || this.pduration || this.isPolicyDurationCompulsory() || this.InsuranceAgeAttainedInExpected || this.durationYearInExpected
      case 'age':
        return this.isAttainedAgeCompulsory() || this.attainedAgeInExpected || this.InsuranceAgeAttainedInExpected
      case 'calendar':
        return true
    }
  }

  static NUMBER: any = 0;

}
