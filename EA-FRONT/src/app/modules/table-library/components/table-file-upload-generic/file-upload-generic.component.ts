import {
  Component,
  OnInit,
  Output,
  Input,
  OnDestroy
} from '@angular/core';
import {
  FileService
} from '../../services/file.service';
import {
  EventEmitter
} from '@angular/core';
import {
  HttpEventType,
  HttpResponse
} from '@angular/common/http';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';

@Component({
  selector: 'app-table-file-upload-generic',
  templateUrl: './file-upload-generic.component.html',
  styleUrls: ['./file-upload-generic.component.scss']
})
export class TableFileUploadGenericComponent implements OnInit, OnDestroy {


  @Output() fileUploadStarted: EventEmitter < any > = new EventEmitter();
  @Output() fileUploaded: EventEmitter < any > = new EventEmitter();
  @Output() fileUploadFailed: EventEmitter < any > = new EventEmitter();


  @Input() btnClass   = "btn btn-primary btn-sm mb-0"
  @Input() uplaodPath = "uploadEXP"

  @Input() disabled:boolean = false

  fileModel = null

  public static UPLOADER_ID = 0

  uploaderId:number

  constructor(private fs: FileService, private tsr:ToasterService) {
    this.uploaderId = TableFileUploadGenericComponent.UPLOADER_ID++;
  }

  percentDone = 0

  uploading = false;
  error: {
    icon,
    message
  }

  file: {
    name,
    type,
    size
  }

  ngOnInit() {}

  ngOnDestroy() {
    this.cancelUpload()
  }

  request = null;

  cancelUpload()
  {
    if ( this.request )
      this.request.unsubscribe()
  }

  onFileChange(event) {
    this.error = null;
    let myFile = event.target.files[0];
    let extensioncheck = true;
    let allowedExtensions = /(\.csv)$/i;
    if (!allowedExtensions.exec(myFile.name)) {
      extensioncheck = false;

      //this.tsr.pop("warning","Warning","The file has not been uploaded. Expected Table file supported format is csv.")

      this.fileUploadFailed.emit("The file has not been uploaded.  Expected Table file supported format is csv.")

      this.error = {
        icon: "exclamation",
        message: "Invalid file extension"
      }


    } else {
      this.uploading = true;
      let size;
      let _formData = new FormData();
      _formData.append("MyFile", myFile);
      let body = _formData;
      this.fileUploadStarted.emit(myFile);
      this.request =  this.fs.upload(body,this.uplaodPath)
        .subscribe(
          event => {
            // Via this API, you get access to the raw event stream.
            // Look for upload progress events.
            if (event.type === HttpEventType.UploadProgress) {
              // This is an upload progress event. Compute and show the % done:
              this.percentDone = Math.round(100 * event.loaded / event.total);
              size = event.total;

            } else if (event instanceof HttpResponse) {

              //this.tsr.pop("success","Success","The file is uploaded")

              let file = event.body;
              file["size"] = size;
              //
              this.fileUploaded.emit(file);
              this.uploading = false;
            }
          },
          error => {
            this.uploading = false;
            this.fileUploadFailed.emit("File not uploaded")

            this.error = {
              icon: "exclamation",
              message: "File not uploaded"
            }

          }
        );
    }
    this.fileModel = null;
  }
}
