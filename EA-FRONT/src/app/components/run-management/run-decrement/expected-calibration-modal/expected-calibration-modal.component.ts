import { Component, OnInit, Output , EventEmitter, Input} from '@angular/core';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';
import { FileService } from '../../../../services/file.service';
import { RunService } from '../../../../services/run.service';
import { Decrement } from '../../../../shared/models/decrement';
 
@Component({
  selector: 'app-expected-calibration-modal',
  templateUrl: './expected-calibration-modal.component.html',
  styleUrls: ['./expected-calibration-modal.component.scss']
})
export class ExpectedCalibrationModalComponent implements OnInit {
 
  @Output() close: EventEmitter< any > = new EventEmitter < any >()
  @Input() decrement : Decrement
  @Input() editable : boolean
  calibration:any
  canUpload : boolean = true
  canSave : boolean = false
  constructor(private runService : RunService, private tsr: ToasterService, private fs: FileService) { }
 
  ngOnInit() {
    console.log(this.editable);
    
    this.canUpload = this.editable
    console.log(this.decrement.ExpCalibrationUrl);
    console.log(true);
    
    
    if(this.decrement.ExpCalibrationUrl) {
      this.calibration = {
        name:"name",
        file:{
          path : this.decrement.ExpCalibrationUrl,
          name:this.decrement.ExpCalibrationUrl.split("/")[this.decrement.ExpCalibrationUrl.split("/").length - 1]
        },
        message:"Ok",
        status:"Ok",
        report:[]
      }
      this.canSave = true
    } else {
      this.calibration = {
        name:"name",
        file:{name:"filename"},
        message:"None",
        status:"",
        report:[]
      }
    }
    
  }
 
  onClose(){
    this.close.emit("close")
  }



  onFileUploaded(file) {
    console.log(file);
    this.calibration.status = "load"
    this.calibration.message = "Checking file"
    this.calibration.report = []
    this.runService.getCalibrationControls(file.path).subscribe(res => {
      this.canSave = true
      if(res && res.length>0) {

        res.forEach(r => {
          this.calibration.report.push(r)
          if(r.split("/")[1] == "Blocking") {
            this.canSave = false
          }
        });
      } 
      //this.calibration.report = res;
      console.log(res);
      
      if(this.calibration.report.length > 0 ) {
        this.calibration.status ="bad"
        this.calibration.message = "The file in not valid"
      } else {
        this.calibration.status = "Ok"
        this.calibration.message = "Ok"
      }
      this.calibration.file.path = file.path

    })
  }

  onFileUploadFailed(error) {
    this.tsr.pop("error","error",error)
    
    this.calibration.file = null
    this.calibration.status = error
  }

  onFileStarted(file:File) {
    this.calibration.file = file
    this.calibration.status = "None"
  }

  saveCal() {
    console.log( this.calibration.file.path);
    
    this.decrement.ExpCalibrationUrl = this.calibration.file.path
    this.close.emit("close")
  }
 
}