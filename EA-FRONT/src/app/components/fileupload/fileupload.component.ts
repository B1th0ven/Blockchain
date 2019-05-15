import {
  Component,
  OnInit,
  Output,
  Input
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
  selector: 'app-fileupload',
  templateUrl: './fileupload.component.html',
  styleUrls: ['./fileupload.component.scss']
})
export class FileuploadComponent implements OnInit {

  constructor(private fs: FileService, private tsr:ToasterService) {}

  percentDone = 0

  fileModel = null

  uploading = false;
  error: {
    icon,
    message
  }

  @Output() fileUploaded: EventEmitter < any > = new EventEmitter();
  @Output() fileUploading: EventEmitter < any > = new EventEmitter();
  @Input() fileType: string = "policy"
  @Input() studyID: any

  file: {
    name,
    type,
    size
  }

  ngOnInit() {}

  onFileChange(event) {
    this.fileUploading.emit(true)
    this.error = null;
    let myFile = event.target.files[0];
    let extensioncheck = true;
    let allowedExtensions = /(\.csv)$/i;
    if (!allowedExtensions.exec(myFile.name)) {
      extensioncheck = false;

      this.tsr.pop("warning","Warning","The file has not been uploaded, "+ this.fileType +" file supported format is csv.")

      /*
      this.error = {
        icon: "exclamation",
        message: "Invalid file extension"
      }
      */
    } else {
      this.uploading = true;
      let size;
      let _formData = new FormData();
      _formData.append("MyFile", myFile);
      let body = _formData;
      this.fs.uploadFile(body,this.studyID)
        .subscribe(
          event => {
            // Via this API, you get access to the raw event stream.
            // Look for upload progress events.
            if (event.type === HttpEventType.UploadProgress) {
              // This is an upload progress event. Compute and show the % done:
              this.percentDone = Math.round(100 * event.loaded / event.total);
              size = event.total;

            } else if (event instanceof HttpResponse) {
              let file = event.body;
              file["size"] = size;
              //
              this.fileUploaded.emit(file);
              this.uploading = false;
            }
          },
          error => {
            this.uploading = false;
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
