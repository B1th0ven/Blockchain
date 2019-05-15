import {
  Component,
  OnInit,
  EventEmitter,
  Output,
  Input
} from '@angular/core';
import {
  IbnrControlService
} from '../../../services/ibnr-control.service';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';
import { FileService } from '../../../services/file.service';
import { Run } from '../../../shared/models/run';

@Component({
  selector: 'app-ibnr-amount',
  templateUrl: './ibnr-amount.component.html',
  styleUrls: ['./ibnr-amount.component.scss']
})
export class IbnrAmountComponent implements OnInit {

  @Output() close: EventEmitter < any > = new EventEmitter < any > ()
  @Output() save: EventEmitter < any > = new EventEmitter < any > ()

  @Input() ibnrType = "amount"
  @Input() run:Run = new Run(null,null)
  @Input() canUpload: boolean
  @Input() validated: boolean

  IBNRs: Array < any >

    constructor(private ic: IbnrControlService, private tsr: ToasterService, private fs: FileService ) {}

  ngOnInit() {

    switch (this.ibnrType) {
      case "amount":
        this.IBNRs = [{
            name: "IBNR Amount",
            file: (this.run.ibnr_amount_path)? {name:this.run.ibnr_amount_path.split(/[\/\\]/).pop(),path:this.run.ibnr_amount_path}: null,
            status: (this.run.ibnr_amount_path)?"ok":"None",
            message: (this.run.ibnr_amount_path)?"Saved":"",
            type: "amount",
            report: Array<String>()
          },
          {
            name: "IBNR Allocation",
            file: (this.run.ibnr_allocation_path)? {name:this.run.ibnr_allocation_path.split(/[\/\\]/).pop(),path:this.run.ibnr_allocation_path}: null,
            status: (this.run.ibnr_allocation_path)?"ok":"None",
            type: "allocation",
            message: (this.run.ibnr_allocation_path)?"Saved":"",
            report: Array<String>()
          }
        ];
        break;
      case "manual":
        this.IBNRs = [{
          name: "IBNR UDF",
          file: (this.run.ibnr_udf_path)? {name:this.run.ibnr_udf_path.split(/[\/\\]/).pop(),path:this.run.ibnr_udf_path}: null,
          status: (this.run.ibnr_udf_path)?"ok":"None",
          type: "udf",
          message: (this.run.ibnr_udf_path)?"Saved":"",
          report: Array<String>()
        } ]
    }
  }

  onSave() {
    let event = []
    this.tsr.pop("success","Success","File uploaded")
    this.save.emit(this.IBNRs)
  }

  onClose() {
    // this.IBNRs.forEach(ibnr => {
    //   if (ibnr.file && ibnr.file.path)
    //     this.fs.delete(ibnr.file.path).subscribe()
    // });
    this.close.emit()
  }
  onCloseCheck() {
    this.close.emit()
  }

  onFileUploaded(ibnrInd, file) {
    let amountPath : string
    let allocationPath : string
    if(this.ibnrType == "amount") {
      if(ibnrInd == 0) {
        if(this.IBNRs[1].file) {
          allocationPath = this.IBNRs[1].file.path
          amountPath = file.path
        }
      } else if ( ibnrInd == 1) {
        if(this.IBNRs[0].file) {
          amountPath = this.IBNRs[0].file.path
          allocationPath = file.path
        }
      }
    }
    let ibnr = this.IBNRs[ibnrInd]
    ibnr.file = file
    ibnr.status = "load"
    ibnr.message = "File Loaded"

    ibnr.report = new Array < String > ()
    this.ic.compulsoryCheck(ibnr.file.path, ibnr.type).subscribe(
      res => {
        ibnr.report = ibnr.report.concat(res)
        ibnr.message = "Compulsory Check Executed"
        this.ic.technicalCheck(ibnr.file.path, ibnr.type).subscribe(
          res => {
            ibnr.report = ibnr.report.concat(res)
            ibnr.message = "Thecnical Check Executed"
            this.ic.functionalCheck(ibnr.file.path, ibnr.type,  this.run.id, this.run.dimensions, this.run.study).subscribe( res => {
              ibnr.report = ibnr.report.concat(res);
              
              if(this.ibnrType == "amount") {
                if(amountPath && allocationPath) {
                  this.ic.amountAllocationCheck(amountPath,allocationPath).subscribe( res => {
                    let ibnrAllocation = this.IBNRs[1]
                    ibnrAllocation.report = ibnrAllocation.report.concat(res)
                    
                    if(ibnrAllocation.report.length > 0 ) {
                      ibnrAllocation.message = "The File is not Valid"
                      ibnrAllocation.status = "bad"
                    }
                    if(ibnrAllocation.report.length >0 || ibnr.report.length > 0) {
                      this.fs.delete(ibnr.file.path).subscribe(
                        res=> this.tsr.pop("warning","Warning","File not uploaded"),
                        err=>
                        {
                          this.tsr.pop("error","Error","Internal Error!")
                        }
                      )
                    }
                  })
                }
              }
              if(ibnr.report.length > 0) {
                ibnr.message = "The File is not Valid"
                ibnr.status = "bad"
                if(this.ibnrType != "amount"){
                  this.fs.delete(ibnr.file.path).subscribe(
                    res=> this.tsr.pop("warning","Warning","File not uploaded"),
                    err=>
                    {
                      this.tsr.pop("error","Error","Internal Error!")
                    }
                  )
                }
              } else {
                ibnr.message = "Validated"
                ibnr.status = "ok"
              }
            })
          }
        )
      },
      err => {
        ibnr.status = "bad"
        ibnr.message = "Error Encounterd"
      }
    )

  }

  onFileUploadFailed(ibnrInd, error) {
    this.tsr.pop("error","error",error)
    let ibnr = this.IBNRs[ibnrInd]
    ibnr.file = null
    ibnr.status = error
  }

  onFileStarted(ibnrInd, file:File) {
    let ibnr = this.IBNRs[ibnrInd]
    ibnr.file = file
    ibnr.status = "None"
  }

  canSave()
  {
    for (let ibnr of this.IBNRs)
    {
      if ( ibnr.status != "ok") return false;
    }
    return true;
  }

  isFileOnServer(path){
      this.fs.isFileOnServerIbnr(this.getFileName(path)).subscribe(
    res => {
      console.log(res)
      if(res["_body"] == "true") this.download(path)
      else this.tsr.pop("error","Error","File not found")
    } , err =>  this.tsr.pop("error","Error","Server Error")
  )
  }
  download(path){
    this.fs.openFileIbnr(this.getFileName(path))
  }
  getFileName(str: string) {
    if (str) {
      return str.split("/").pop()
    }
  }
    
}
