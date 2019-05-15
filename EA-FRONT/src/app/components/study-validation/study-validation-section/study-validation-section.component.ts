import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Run } from '../../../shared/models/run';
import { Study } from '../../../shared/models/study';
import { SCREEN, HoverOverService } from '../../hover-over/hover-over.service';


@Component({
  selector: 'app-study-validation-section',
  templateUrl: './study-validation-section.component.html',
  styleUrls: ['./study-validation-section.component.scss'],
})
export class StudyValidationSectionComponent implements OnInit {

  @Input() runs: Run[]
  @Input() study:Study
  @Input()editable: boolean
  @Input() validated : boolean

  @Output() canSaveButton : EventEmitter<boolean> = new EventEmitter<boolean>()
  @Output() isStudyValidated : EventEmitter<boolean> = new EventEmitter<boolean>()

  comp_num

  screen = SCREEN.ACCESS_RIGHT

  selectedQx:string
  selectedIx:string
  selectedIxQx:string
  selectedWx:string
  selectedRetained:Run[]

  qxL:Run[]
  ixL:Run[]
  ixqxL:Run[]
  wxL:Run[]
  retainedL:Run[]

  deleteUnused:boolean=true;


  warnings = {
    qx:null,
    ix:null,
    wx:null,
    ixqx:null
  }

  constructor(public hs:HoverOverService) { }

  ngOnInit() {
    this.comp_num = StudyValidationSectionComponent.COMPONENTS_NUMBER++ ;
    
    
    this.initOptions();

    this.initMasterRunsAndRetained()
    //this.isValidated = this.study.status.trim().toLocaleLowerCase() == "validated"
    this.canSaveButton.emit(this.canSave())
    if(this.selectedIx || this.selectedQx) {
      this.isIxouQxSelected = true
    }
    if(this.selectedIxQx) {
      this.isIxQxSelected = true
    }
  }

  private initMasterRunsAndRetained() {
    this.runs.forEach(run => {
      if(run.masterRunQX) {
        this.selectedQx = String(run.id);
      }
      if(run.masterRunIX) {
        this.selectedIx = String(run.id);
      }
      if(run.masterRunWX) {
        this.selectedWx = String(run.id);
      }
      if(run.masterRunIXQX) {
        this.selectedIxQx = String(run.id);
      }
      if(run.retainedRun) {
        this.selectedRetained = this.selectedRetained ? this.selectedRetained : []
        this.selectedRetained.push(run)
      }
    })
  }

  public initOptions() {
    this.qxL = this.getRunByDecrement("qx");
    this.ixL = this.getRunByDecrement("ix");
    this.ixqxL = this.getRunByDecrement("ix+qx");
    this.wxL = this.getRunByDecrement("wx");
    this.retainedL = []
    this.runs.filter(run => (!run.masterRunQX && !run.masterRunIX && !run.masterRunWX && !run.masterRunIXQX)).forEach(s => this.retainedL.push(s))

  }
  isIxQxSelected : boolean = false;
  isIxouQxSelected : boolean = false;
  manageQxIx(decrement) {
    console.log(decrement);    
    if(decrement == "qx" || decrement == "ix") {
      this.isIxQxSelected = false
      this.isIxouQxSelected = false      
      if((this.selectedIx && this.selectedIx != "" ) || (this.selectedQx &&  this.selectedQx != "")) {
        this.selectedIxQx = null;
        this.isIxouQxSelected = true 
      } 
    } else if (decrement == "ix+qx") {
      this.isIxQxSelected = false
      this.isIxouQxSelected = false 
      if(this.selectedIxQx && this.selectedIxQx != "") {
        this.selectedIx = null
        this.selectedQx = null
        this.isIxQxSelected = true
      }
    }
  }

  public updateOptions(idDecrement) {
    let decrement = idDecrement==1 ? "qx" : idDecrement==2 ? "ix" : idDecrement==3 ? "wx" : "ix+qx";

    this.retainedL = []
    this.runs.forEach(s => this.retainedL.push(s))

    let selectedDecrementWx = [];
    let selectedDecrementQx = [];
    let selectedDecrementIx = [];
    let selectedDecrementIxQx = [];

    if(this.selectedWx) {
      this.retainedL = this.retainedL.filter(run => run.id != Number(this.selectedWx));
      selectedDecrementWx = this.runs.find(run=>run.id==Number(this.selectedWx)).decrements.map(decrement => decrement.decrement).filter(decrement => decrement != "wx");
      
    }
    if(this.selectedQx) {
      this.retainedL = this.retainedL.filter(run => run.id != Number(this.selectedQx));
      selectedDecrementQx = this.runs.find(run=>run.id==Number(this.selectedQx)).decrements.map(decrement => decrement.decrement).filter(decrement => decrement != "qx");
      
    }
    if(this.selectedIx) {
      this.retainedL = this.retainedL.filter(run => run.id != Number(this.selectedIx));
      selectedDecrementIx = this.runs.find(run=>run.id==Number(this.selectedIx)).decrements.map(decrement => decrement.decrement).filter(decrement => decrement != "ix");
      
    }
    if(this.selectedIxQx) {
      this.retainedL = this.retainedL.filter(run => run.id != Number(this.selectedIxQx));
      selectedDecrementIxQx = this.runs.find(run=>run.id==Number(this.selectedIxQx)).decrements.map(decrement => decrement.decrement).filter(decrement => decrement != "ix+qx");
      
    }

    this.warnings.qx = null;
    if(this.selectedQx && this.selectedIx && selectedDecrementQx.find(decrement => decrement == "ix") && this.selectedQx != this.selectedIx) {
      this.warnings.qx = []
      this.warnings.qx.push("Please delete decrement ix from Run" +this.runs.find(run=>run.id==Number(this.selectedQx)).name+" and update the run before validation");
    }
    if(this.selectedQx && this.selectedWx && selectedDecrementQx.find(decrement => decrement == "wx") && this.selectedQx != this.selectedWx) {
      this.warnings.qx = this.warnings.qx ? this.warnings.qx : []
      this.warnings.qx.push("Please delete decrement wx from Run" +this.runs.find(run=>run.id==Number(this.selectedQx)).name+" and update the run before validation");
    }

    this.warnings.ix = null;
    if(this.selectedIx && this.selectedQx && selectedDecrementIx.find(decrement => decrement == "qx") && this.selectedIx != this.selectedQx) {
      this.warnings.ix = []
      this.warnings.ix.push("Please delete decrement qx from Run" +this.runs.find(run=>run.id==Number(this.selectedIx)).name+" and update the run before validation");
    }
    if(this.selectedIx && this.selectedWx && selectedDecrementIx.find(decrement => decrement == "wx") && this.selectedIx != this.selectedWx) {
      this.warnings.ix = this.warnings.ix ? this.warnings.ix : []
      this.warnings.ix.push("Please delete decrement wx from Run" +this.runs.find(run=>run.id==Number(this.selectedIx)).name+" and update the run before validation");
    }

    this.warnings.wx = null;
    if(this.selectedWx && this.selectedIx && selectedDecrementWx.find(decrement => decrement == "ix") && this.selectedWx != this.selectedIx) {
      this.warnings.wx = []
      this.warnings.wx.push("Please delete decrement ix from Run" +this.runs.find(run=>run.id==Number(this.selectedWx)).name+" and update the run before validation");
    }
    if(this.selectedWx && this.selectedQx && selectedDecrementWx.find(decrement => decrement == "qx") && this.selectedWx != this.selectedQx) {
      this.warnings.wx = this.warnings.wx ? this.warnings.wx : []
      this.warnings.wx.push("Please delete decrement qx from Run" +this.runs.find(run=>run.id==Number(this.selectedWx)).name+" and update the run before validation");
    }
    if(this.selectedWx && this.selectedIxQx && selectedDecrementWx.find(decrement => decrement == "ix+qx") && this.selectedWx != this.selectedIxQx) {
      this.warnings.wx = this.warnings.wx ? this.warnings.wx : []
      this.warnings.wx.push("Please delete decrement ix+qx from Run" +this.runs.find(run=>run.id==Number(this.selectedWx)).name+" and update the run before validation");
    }

    this.warnings.ixqx = null;
    if(this.selectedIxQx && this.selectedQx && selectedDecrementIxQx.find(decrement => decrement == "qx") && this.selectedIxQx != this.selectedQx) {
      this.warnings.ixqx = []
      this.warnings.ixqx.push("Please delete decrement qx from Run" +this.runs.find(run=>run.id==Number(this.selectedIxQx)).name+" and update the run before validation");
    }
    if(this.selectedIxQx && this.selectedWx && selectedDecrementIxQx.find(decrement => decrement == "wx") && this.selectedIxQx != this.selectedWx) {
      this.warnings.ixqx = this.warnings.ixqx ? this.warnings.ixqx : []
      this.warnings.ixqx.push("Please delete decrement wx from Run" +this.runs.find(run=>run.id==Number(this.selectedIxQx)).name+" and update the run before validation");
    }

    this.canSaveButton.emit(this.canSave())
  }

  getRunByDecrement(decrement)
  {
    let t = this.runs.filter(run=> run.run_calc && run.run_calc.rclcStatus == "DONE" && run.decrements.find(d=>d.decrement == decrement))
    console.log("RETURNING " ,t,"FROM RUNS",this.runs)
    return t
  }

  canSave() // cant save à corriger
  {
    if( ((!this.selectedIx) || this.selectedIx=="") && ((!this.selectedQx) || this.selectedQx=="") && ((!this.selectedIxQx) || this.selectedIxQx=="") && ((!this.selectedWx) || this.selectedWx=="") || this.warnings.qx || this.warnings.ix ||this.warnings.ixqx || this.warnings.wx ) return true;
    return false
  }

  datasetDeleteChange() {
    this.deleteUnused = !this.deleteUnused
  }

  static COMPONENTS_NUMBER = 0 ;
}
