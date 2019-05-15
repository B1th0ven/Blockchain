import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Control } from '../shared/models/control';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subject, BehaviorSubject } from 'rxjs';
import { controlNameBinding } from '@angular/forms/src/directives/reactive_directives/form_control_name';
import { DatasetService } from './dataset.service';
import { date } from '../shared/models/date';
import { Study } from '../shared/models/study';


@Injectable()
export class ControlService {

    constructor(
    private http:Http,
    private ds: DatasetService
  ){}

  //NEW CODE ++++
  funcControls(policyPath,productPath,type,startDate,endDate,stdid,user)
  {
    let body={
      path_policy:policyPath,
      path_product:productPath,
      stdid:stdid,
      type:type,
      op_start: date.formatDate(startDate,"dd/mm/yyyy"),
      op_end:   date.formatDate(endDate,"dd/mm/yyyy"),
      userId: user.ruLogin.toString(),
      studyId: stdid.toString()

    }
    return this.http.post(environment.api_endpint+"FuncControls",body).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  notExecutedControls(policyPath,productPath,type,startDate,endDate,study,user)
  {
    let body={
      path_policy:policyPath,
      path_product:productPath,
      type:type,
      op_start: date.formatDate(startDate,"dd/mm/yyyy"),
      op_end:   date.formatDate(endDate,"dd/mm/yyyy"),
      studyId: study.id.toString(),
      userId: user.ruLogin.toString()
    }
    return this.http.post(environment.api_endpint+"NotExecutedControls",body).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  //NEW CODE ----
  sendCancelRequest(study, user)
  {
    let body = {
      studyId: study.toString(),
      userId: user.toString(),
      type: 'cancel'
    }
    return this.http.post(environment.api_endpint+"CancelJobControls",body).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
  //OLD CODE +++++
  controlUpdates:Subject<{}>
  jobID:Number
  fileTypes=[]
  typeControls:Array<{type,controls}>
  controls:Array<Control>
  mode: Number
  updateTimeoutValue = 5000
  updateInterval

  initControls(mode){

    this.mode = mode

    Control.numberOfInstances = 0

    this.controlUpdates =new Subject<{}>()

    //if (this.mode == 0) {
      this.fileTypes=['policy','product'];
      this.controls=[
      ]

    this.controls.forEach(control => {
      control.status = "start"
      control.running = true
    });

    this.setControlFiles()

    return this.controls
  }

  doTechControls(policyPath,productPath,type,study, user)
  {
    let body={
      path_policy:policyPath,
      path_product:productPath,
      type:type,
      studyId: study.id.toString(),
      userId: user.ruLogin.toString()
    }

    return this.http.post(environment.api_endpint+"TechControls",body).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  resetControls(){
    this.controls.forEach(control => {
      control.done = false
      control.message = null
      control.status = "restart"
      control.running = true
      control.valid = null
      control.time = null
      control.errorsDetailed = null
      control.errors = null
    });
    this.controlUpdates.next(this.controls)
  }

  startNewJob(files,dataset){

    this.resetControls()

    clearInterval(this.updateInterval)

    let body = {}
    this.fileTypes.forEach(type => {
      let file = files.find(e=>e.type==type)
      if (file){
        let typeName = (type!=="exposition")?type:"expo";
        body[typeName+"FilePath"] = file.file.path
        body[typeName+"Columns"] = file.newcolumns
      }
    });

    /*
    this.http.post(environment.api_endpint+"validator/startjob",body).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
      .do(res=>{
        this.jobID=res.id
        dataset.jobID = this.jobID
        this.ds.saveDatasetJob(dataset,this.jobID)
        this.controlUpdates.next(this.mapAllControlUpdates(res,this.jobID))
        this.getJobUpdates(this.updateTimeoutValue,res.id)
      }).subscribe(
        (res)=>{},
        (err)=>{ this.controlUpdates.error(err)}
      )
    */
  }

  startJob(files,dataset){

    if(!dataset.jobID){
      this.startNewJob(files,dataset)
    }else{
      this.getJobUpdates(this.updateTimeoutValue,dataset.jobID)
    }
  }

  getJobUpdates(timeout,jobID){

    clearInterval(this.updateInterval)
    //Get Immediate Update
    this.getJobUpdate(jobID);

    //Get regular update
    this.updateInterval = setInterval(() => {
      this.getJobUpdate(jobID);
    }, timeout);
  }

  getControlErrorDetails = (control ,jobID)=>{
    if (control.status == "done") {
      this.http.get(environment.api_endpint+"validator/job/errors?jobId="+jobID+"&controlType="+control.identifier).retry(5).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
      .subscribe(
        (res)=>{

            control.errorsDetailed = res;
      },
      (err)=>  console.log(err)
      )
    }
  }

  private getJobUpdate(jobID: any) {
    this.http.get(environment.api_endpint + "validator/job?id=" + jobID).retry(5).map(res => res.json())
      .subscribe((res) => {

        this.controlUpdates.next(this.mapAllControlUpdates(res, jobID));
        if (res.status == 'done') {


          clearInterval(this.updateInterval);
        }
      }, (err) => { clearInterval(this.updateInterval); this.controlUpdates.error(err); });
  }

  private mapAllControlUpdates(res: any,jobID): Array<Control>{
    this.controls.forEach(control => {
      this.mapControlUpdate(res,control)
      //this.getControlErrorDetails(control ,jobID)
    });

    return this.controls
  }

  private mapControlUpdate = (res: any,control) =>{

    control.status = "done"
    control.time = 0
    control.errorsDetailed = res[control.identifier].affectedColumns
    control.error = res[control.identifier].number_of_errors
  }

  setControlFiles(){

    this.typeControls=[]

    for (var type of this.fileTypes)
      this.typeControls.push({controls:[],type:type})

    this.controls

    .forEach(c=>{
        for (let f of c.files) {

          let type = this.typeControls.find(e=>e.type==f)
            if (type) type.controls.push(c.number)
        }
      })
  }

  calculateProgressPerc(typeControls){
    if (typeControls.length <= 0) return 100
    let count = 0
    typeControls.forEach( controlNbr=> {
      let control = this.controls.find(e=>e.number==controlNbr)
        if (control.progress){
          count = count + control.progress
        }else if(control.valid){
          ++count
        }
    });

    return Math.floor((count/typeControls.length)*100)
  }

  progressPerc(type){
    let controls = this.typeControls.find(e=>e.type==type).controls

    if ( controls ) return this.calculateProgressPerc(controls)
    else return 0
  }

  excecutionInfo = (fileType: string) : string =>{
    if (!this.controls) return "Not running"

    if (this.progressPerc(fileType)==100) return "Done!"

    for (var c of this.controls) {
      if ( c.running ){
        if ( c.files.find(f=>f==fileType)){
          return "Executing " + c.name +"..."
        }
      }
    }
    return "Stopped"
  }

}
