import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from '@angular/core';
import { Run } from '../../shared/models/run';
import { Study } from '../../shared/models/study';
import { RunService } from '../../services/run.service';
import { StudyValidationSectionComponent } from './study-validation-section/study-validation-section.component';
import { ToasterService } from '../../../../node_modules/angular5-toaster/dist/angular5-toaster';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { UsersService } from '../../services/users.service';

@Component({
  selector: 'app-study-validation',
  templateUrl: './study-validation.component.html',
  styleUrls: ['./study-validation.component.scss']
})
export class StudyValidationComponent implements OnInit {

  constructor(private rs:RunService, private tsr: ToasterService,private permissionsService: NgxPermissionsService, private userService : UsersService) { }

  runs: Run[]
  @Input() study:Study

  disableSaveButton : boolean

  isStudyValidated : boolean = false
  @Output() studyValidated : EventEmitter<boolean> = new EventEmitter<boolean>()
  loading:boolean=false;
  initialized:boolean=false;

  @ViewChild(StudyValidationSectionComponent)    svsc:StudyValidationSectionComponent

  ngOnInit() {
    if(this.study.status)
    this.isStudyValidated = this.study.status.trim().toLocaleLowerCase() == "validated"
    //this.init();
  }

  canSaveEmiter(disableValidate) {
    this.disableSaveButton = disableValidate
  }

  public init() {
    if ( !this.initialized )
    {
      this.initialized = true;
      if(this.study.id)
      this.rs.getByStudyId(this.study.id).subscribe((res) => { this.updateRuns(res); this.loading=false}, err => { console.log("ERROR LOADING RUNS", err); this.loading=false});
    }
  }

  public updateRuns(runs)
  {
    this.runs = runs
    this.runs.forEach((run,i) => {
      run.name = "Run " + (i+1)
    });
    if ( this.svsc ){
      this.svsc.runs = runs
      this.svsc.initOptions()
    }

  }

  public onRunStatusChange(runs:Run[]){
    if ( this.svsc ){
      this.svsc.runs = runs
      this.svsc.initOptions()
    }

  }

  canSave()
  {
    return (this.svsc && this.svsc.canSave())
  }

  public deleteRun(run:Run){

    if(this.runs){
    let ind = this.runs.indexOf(run)
    this.runs.splice(ind,1)
    }

    if(this.svsc){
    this.svsc.runs = []
    this.runs.forEach(s => this.svsc.runs.push(s))
    this.svsc.initOptions()
    }
    
  }

  validateStudy() {
    this.rs.validateStudy(this.svsc,this.userService.getStoredUser().ruId).subscribe(res => {
      this.study.mapFromApiStudy(res)
      this.isStudyValidated = this.study.status.trim().toLocaleLowerCase() == "validated"
      this.study.lastStatusModifiedBy = this.userService.getStoredUser()
      this.studyValidated.emit(true)
      this.tsr.pop("success","Success","This Study is validated")
    })
  }
  
  canEdit(){
    if(this.permissionsService.getPermission("STUDY_WRITER"))
    return true
    return false
  }


}
