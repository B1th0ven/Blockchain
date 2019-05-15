import { Component, OnInit, Output, EventEmitter, Input, ViewChild } from '@angular/core';
import { Study } from '../../shared/models/study';
import { Run } from '../../shared/models/run';
import { DatasetService } from '../../services/dataset.service';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap/datepicker/datepicker-config';
import { RunManagementComponent } from '../run-management/run-management.component';
import { environment } from '../../../environments/environment';
import { StudyValidationComponent } from '../study-validation/study-validation.component';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { DatasetManagementComponent } from '../dataset-management/dataset-management.component';

@Component({
  selector: 'app-study-create',
  templateUrl: './study-create.component.html',
  styleUrls: ['./study-create.component.scss']
})
export class StudyCreateComponent implements OnInit {

  @Input() study:Study

  @Output() save:EventEmitter<any>= new EventEmitter<any>()
  @Output() continue:EventEmitter<any>= new EventEmitter<any>()
  @Output() delete:EventEmitter<any> = new EventEmitter<any>()
  @Output() statusChange:EventEmitter<any> = new EventEmitter<any>()

  @ViewChild(RunManagementComponent)    rmc:RunManagementComponent
  @ViewChild(StudyValidationComponent)  svc:StudyValidationComponent
  @ViewChild(DatasetManagementComponent)  datasetManagementComponent : DatasetManagementComponent

  activeTab:number = 0;
  tabs=[
    {name:"Study Definition",type:"def"},
    {name:" Dateset Mangement",type:"dat"},
    {name:"Run Management",type:"run"},
    {name:"Study Validation",type:"val"}
  ]

  load = true
  err = null

  runs:Run[]

  constructor(private ds :DatasetService , config: NgbDatepickerConfig,private permissionsService: NgxPermissionsService) {
    config.minDate = {year: 1970, month: 1, day: 1};
    let today = new Date()
    config.maxDate = {year: today.getFullYear(), month: today.getMonth()+1, day: today.getDate()};
    config.outsideDays = 'hidden';
  }

  ngOnInit() {
    if ( !this.study )
    {
      this.study = new Study()
      this.load = false
    }
    else
    {
      let temp = this.study
      this.study = new Study()
      this.study.mapFromApiStudy(temp)
      this.load = true
      this.ds.getRunValidation(this.study).subscribe(
        res=>{ this.validControl = res["valid"] ; this.study.count = res["count"] ; this.load = false},
        err=>{ this.err = "Could not retrieve data"; this.load = false}
      )

    }

    if (!this.runs)
    {
      this.runs= new Array<Run>()
    }

    //this.updateIndex()

  }

  updateIndex(){
    if(!this.permissionsService.getPermission('STUDY_READER') && !this.permissionsService.getPermission('STUDY_WRITER') && this.study.id)
    this.activeTab++
    if(!this.permissionsService.getPermission('DATASET_READER') && this.study.id)
    this.activeTab++
  }

  openTab(ind){
    if ( ind == this.activeTab ) return

    if ( ind == 2 && this.rmc ) this.rmc.onInit()
    if ( ind == 3 && this.svc ) this.svc.init()

    this.activeTab = ind
  }

  onContinue(study)
  {
    this.openTab(1)
    this.continue.emit(study)
  }
  onSave(study){
    this.save.emit(study)
  }

  validControl=false
  onContinueControl(event)
  {
    this.validControl = true

    console.log("ContinueControl",event)
    if (this.rmc)
    {
      console.log("About to push Data",event)
      this.rmc.pushDataset(event)
      console.log(this.rmc);
      
    }

    if ( event.goToRun == true )
      this.openTab(2)
  }

  onDatasetNameChanged(event)
  {
    if (this.rmc) this.rmc.changeDatasetname(event.dataset)
  }

  onDatasetDelete(event){
    console.log("study create ---------->",event)
    if(this.rmc) this.rmc.deleteDataset(event)
    if(this.study.count <= 0) this.validControl = false
  }

  prod = environment.production;
  stepper = JSON.parse(localStorage.getItem("stepper")) || false;
  onStepperChanged()
  {
    this.stepper = !this.stepper;
    localStorage.setItem("stepper",this.stepper)
  }

  onRunsSaved(runs:Run[])
  {
    if (this.svc) this.svc.updateRuns(runs)
    this.datasetManagementComponent.studyDatasetComponents.forEach(component=>{
      component.checkRunAssociatedToDataset()
    })
  }

  onRunDelete(run:Run){
    let ind = this.runs.indexOf(run)
    this.runs.splice(ind,1)
    if (this.svc) this.svc.deleteRun(run)
    this.datasetManagementComponent.studyDatasetComponents.forEach(component=>{
      component.checkRunAssociatedToDataset()
    })
  }

  onRunStatusChange(runs:Run[]){
    if (this.svc) this.svc.onRunStatusChange(runs)
  }

  onDeleteStudy(e){
    this.delete.emit(e+"deleted")
  }

  onStatusChange(e){
    this.study.status = e;
    this.statusChange.emit(e)
  }

  updateDatasets(event) {
    if(event == true) {
      this.datasetManagementComponent.getDataSets()
      console.log(this.rmc.runGeneralComponent);
      this.datasetManagementComponent.studyDatasetComponents.forEach(sd=> {        
        sd.studyDatasetCreateComponent.validated = true
      })
      console.log(this.datasetManagementComponent);
      
      this.rmc.runGeneralComponent.forEach(r=> {
        r.dropdownSettings = {
          singleSelection: false,
          text: "",
          selectAllText: 'Select All',
          unSelectAllText: 'Unselect All',
          enableSearchFilter: true,
          classes: "myclass custom-class",
          badgeShowLimit: 0,
          disabled:true
        }
      })
    }
  }
 

}
