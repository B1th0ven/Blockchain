import {
  Component,
  OnInit,
  Input,
  Output
} from '@angular/core';
import {
  FileService
} from '../../services/file.service';
import {
  EventEmitter
} from '@angular/core';
import {
  Observable
} from 'rxjs/Observable';
import {
  log
} from 'util';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';
import { Study } from '../../shared/models/study';
import { User } from '../../shared/models/user';
import { UsersService } from '../../services/users.service';
import { date } from '../../shared/models/date';

@Component({
  selector: 'app-filebrowser',
  templateUrl: './filebrowser.component.html',
  styleUrls: ['./filebrowser.component.scss']
})
export class FilebrowserComponent implements OnInit {
  status: string;
  private data: Observable < number > ;
  private values: Array < number > = [];
  private anyErrors: boolean;
  private finished: boolean;

  constructor(private fs: FileService, private tsr: ToasterService, private userService : UsersService) {}

  loading
  errMsg
  loadingFileInfo = false;
  isPrivacyChecked:boolean = false

  @Input() study:Study

  @Output() fileLoaded: EventEmitter < any > = new EventEmitter();
  @Output() fileLoadFail: EventEmitter < any > = new EventEmitter();
  @Output() fileLoading: EventEmitter < any > = new EventEmitter();
  @Output() metadata: EventEmitter < any > = new EventEmitter();

  @Input() type
  @Input() struct
  privacySubmitter : User = new User
  privacyDate: Date
  files
  selectedFile
  selectedSheet
  directory

  query

  ngOnInit() {
    //console.log("----------- check study input",this.study.id)
    console.log("Studyyyyy ====>"+this.study);
    this.privacySubmitter.mapFromApi(this.userService.getStoredUser()); 
    this.loadFiles();
  } 

  reLoadFiles(e: Event) {
    e.preventDefault()
    this.loadFiles()
  }

  loadFiles() {
    this.loading = true;
    this.errMsg = null;
    this.fs.getFiles(this.study.id,this.userService.getStoredUser().ruLogin).then(res => {

      this.directory = res["directory"];
      this.files = res;
      console.log(this.files);
      
      this.loading = false;
    }).catch(err => {

      this.errMsg = err;
      this.loading = false;
    });
  }

  selectFile(f, dir, index) {

    this.errMsg = null
    this.loadingFileInfo = true;
    this.unselect()

    this.loadingFileInfo = false;
    this.selectedFile = f
    this.selectedFile["worksheets"] = []
    this.selectedSheet = 0 //res["worksheets"][0]
    //this.selectedFile.name = this.selectedFile.path.split("/").pop()
  }

  isSelected(path)
  {
    return (this.selectedFile && this.selectedFile.path == path)
  }

  selectSheet(sheet) {

  }

  unselect() {
    this.selectedFile = null;
    this.selectedSheet = null;
  }

  confirm(event) {
    if (this.selectedFile) {
      this.fileLoading.emit({
        file: this.selectedFile,
        type: this.type,
        sheet: this.selectedSheet,
        struct: this.struct,
        path: this.selectedFile.path,
        privacySubmitter : this.privacySubmitter,
        privacyDate: this.privacyDate
      })
    } else {
      event.preventDefault()
    }
  }

  onFileUploaded(file) {
    if ( !this.files.find(f=> f.name == file.name) )
      this.files.push(file)

    this.selectFile(file,null,null)

  }

  fileType(fileName) {
    return String(fileName.split(".").pop()).toLowerCase()
  }

  shortName(str: String, limit) {
    if (str.length >= limit && limit > 3)
      return str.substr(0, limit - 3 ) + "..."
    else
      return str
  }

  deleteFile(file_name:string,file_path:string){
    let ind = this.files.findIndex(f=>f.name==file_name)
    if (ind>=0){
      this.fs.findDatasetByFileId(file_path).subscribe(res=>{
        if(res && Array.isArray(res) && res.length>0) {
          this.tsr.pop("error","Erros","File cannot be deleted because it is used in another dataset")
        } else {
          this.fs.delete(file_path.replace(/\\/gm,"/")).subscribe(
            res=>{
              this.files.splice(ind,1)
              this.tsr.pop("success","Success","File deleted")
              this.selectedFile = undefined
            },
            err=>{
              this.tsr.pop("error","Error","Internal error")
            }
          )
        }
      })
      
    }
  }
  copying = false;
  attachUserFileToStudy(){
    this.copying = true
    this.fs.copyFileToStudy(this.study.id,this.selectedFile.path).subscribe(
      res => {this.onFileUploaded(res);this.copying = false},
      err => {this.tsr.pop("error","Error","Internal error");this.copying=false}  
    )
  }

  getColsNumber(){
    if(this.selectedFile && this.selectedFile.userFile) return 9
    else return 12
  }
 /**
  * PRIVACY DATA
  */
  date : String;
  time : String
  

  privacyCheckChanged() {
    console.log("Privacy checked : " + this.isPrivacyChecked);
    
    if(this.isPrivacyChecked) {
      this.privacyDate = new Date();
      this.date= date.getCurrentDate(this.privacyDate);
      this.time= date.getCurrentTime(this.privacyDate);
      this.metadata.emit({
        user: this.privacySubmitter.name+' '+this.privacySubmitter.lastname, 
        date: this.date,
        time: this.time,
      })

      this.privacySubmitter.mapFromApi(this.userService.getStoredUser());      
    } else {
      this.date = null;
      this.time = null;
      this.privacyDate = null;
      this.privacySubmitter = new User();
      this.metadata.emit({
        user: null, 
        date: null,
        time: null,
      })
    }
  }
  CompNO=0;
}
