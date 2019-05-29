import {
  Component,
  OnInit,
  Input,
  SimpleChanges,
  SimpleChange,
  Output,
  OnChanges
} from '@angular/core';
import {
  Dataset
} from '../../shared/models/dataset';
import {
  DatasetService
} from '../../services/dataset.service';
import {
  Router,
  ActivatedRoute
} from '@angular/router';
import {
  FileType
} from '../../shared/models/file-type';
import {
  NgbModal,
  ModalDismissReasons,
  NgbActiveModal,
  NgbModalRef
} from '@ng-bootstrap/ng-bootstrap';
import {
  FileService
} from '../../services/file.service';
import {
  EventEmitter
} from '@angular/core';
import { Study } from '../../shared/models/study';

@Component({
  selector: 'app-dataset-files',
  templateUrl: './dataset-files.component.html',
  styleUrls: ['./dataset-files.component.scss']
})
export class DatasetFilesComponent implements OnInit {


  fileTypes = ["product", "policy"];
  selectedRank : number;
  busy: boolean = false
  error
  step = 1
  policyPivot : string[] 
  productPivot : string[] 
  files: any[]
  //Dataset Properties
  @Input() dataset: Dataset
  @Input() tab: Number
  @Input() mode: Number
  @Input() study: Study
  @Input() disabled: boolean
  //Dataset files
  @Output() metadata: EventEmitter < any > = new EventEmitter();  
  selectedType
  fileDisplay
  constructor(
    private ds: DatasetService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private fs: FileService) {}

  ngOnInit() {
    if( this.dataset.mode == 2) {
      // this.ds.getSnapshotByDatasetId(this.dataset.id).subscribe(
      //   {
      //     next: res => {
      //       res.forEach(element => {
      //         this.dataset.temporaryFile = []
      //         res = []
      //         element["fileHeader"].split(",").forEach(header => {
      //           res.push(header)
      //         }),
      //         this.dataset.temporaryFile.push(
      //           {
      //             name: element["fileName"],
      //             status: element["status"],
      //             type: element["type"],
      //             typename:element["typename"],
                  
      //             columns: res,
      //             path: element["fileLink"],
      //             inconsistent: element["inconsistentColumns"],
      //             reportingYear: element["reportingYear"]
      //           }
      //         )
      //       });
      //     },error: err => {
      //       console.log(err)
      //     }
      //   }
      // )
      this.fileDisplay = this.dataset.temporaryFile
    }
    else{
      this.fileDisplay = this.dataset.files
    }
    if (!this.dataset.files)
      this.dataset.createDatasetFileHolders();

      this.ds.getPivots("policy").subscribe(res => {
        this.policyPivot = res;
      })
      this.ds.getPivots("product").subscribe(res => {
        this.productPivot = res;
      })
  }

  ngAfterViewInit() {
    /*
    let modal = document.querySelector(`#fileBrowserModal${this.tab}`)
    if (modal) document.body.appendChild(modal)

    */
  }
  updatefiles(){
    if(this.dataset.mode == 2){
      this.dataset.temporaryFile = this.fileDisplay
    }else {
      this.dataset.files = this.fileDisplay
    }
    console.log("File Display -->",this.fileDisplay)
    console.log("File Temporary -->",this.dataset.temporaryFile)
    console.log("File Temporary -->",this.dataset.temporaryFile)
  }
  removeFile(index){
    if(this.fileDisplay[index].type != 'product')
    {
    this.fileDisplay.splice(index , 1)
    listColums = this.allPolicyHeader(this.fileDisplay)
    this.updateInconsistante(this.fileDisplay,listColums)
  }
    if(this.dataset.temporaryFile.length> 2) {
      var listColums = []
      listColums = this.allPolicyHeader(this.fileDisplay)
      this.updateInconsistante(this.fileDisplay,listColums)
    }else {
      this.fileDisplay[0]["inconsistent"] = []
    }
    this.updatefiles()
  }
  ngOnChanges(changes: {
    [propName: string]: SimpleChange
  }) {

    if (changes['mode'] && changes['mode'].previousValue != changes['mode'].currentValue) {
      //this.dataset.files=null;
      //this.dataset.createDatasetFileHolders();
    }
  }

  canBrowse() {
    if (!Array.isArray(this.dataset.files)) {
      return false;
    } else {
      let file = this.dataset.files.find(f => f.type == this.selectedType)
      return (this.selectedType && file && file.status != "load")
    }
  }

  canBrowseSpec(file)
  {
    if (!Array.isArray(this.dataset.files)) {
      return false;
    } else {
      return (file && file.status != "load")
    }
  }

  onSelectType(type) {
    this.selectedType = type;
  }
  onSelectrank(rank){
    this.selectedRank = rank
  }
  addUpdatePolicyFile(){
    this.fileDisplay.splice(-1,0,new FileType({
      type: "policy"
    }))
    this.updatefiles()
  }
  onFileLoading(event, content: NgbModalRef) {
    if (this.selectedType == 'product'){

    var loadingFile = this.fileDisplay.find(el => el.type == 'product')
    loadingFile.name = event.file.name
    loadingFile.path = event.file.path
    loadingFile.sheet = ""
    loadingFile.status = "load"
    loadingFile.privacySubmitter = event.privacySubmitter
    loadingFile.privacyDate = event.privacyDate

    this.fs.validateFile(event.path, event.struct,0)
      .then(res => {
          
        this.onFileLoaded({
          columns: res[0],
          missing: res[1],
          ignored: res[2],
          duplicated: res[3],
          maxyear: res[5],
          minyear: res[4],
          type: event.type
        })
      })
      .catch(error => {

        this.onFileLoadFail({
          error: error,
          type: event.type
        })
      })

    this.modalRef.close()}else {
      if(this.dataset.mode == 2){var loadingFile = this.fileDisplay[this.selectedRank]}else {
      var loadingFile = this.fileDisplay[this.selectedRank]}
      loadingFile.type = 'policy'
      loadingFile.name = event.file.name
      loadingFile.path = event.file.path
      loadingFile.sheet = ""
      loadingFile.status = "load"
      loadingFile.privacySubmitter = event.privacySubmitter
      loadingFile.privacyDate = event.privacyDate
  
      this.fs.validateFile(event.path, event.struct,0)
        .then(res => {
            
          this.onFileLoaded({
            columns: res[0],
            missing: res[1],
            ignored: res[2],
            duplicated: res[3],
            maxyear: res[5],
            minyear: res[4],
            type: event.type
          })
        })
        .catch(error => {
  
          this.onFileLoadFail({
            error: error,
            type: event.type
          })
        })

    }
  }

  onFileLoadFail(event) {
    if(this.dataset.mode == 2){var loadingFile = this.dataset.temporaryFile[this.selectedRank]}else {
      var loadingFile = this.dataset.files[this.selectedRank]}
    if( !loadingFile ) return
    loadingFile.sheet = null
    loadingFile.status = "bad"
    loadingFile.columns = null
    loadingFile.missing = null

    //this.modalRef.close()
  }
  sendMetaData(event){
    this.metadata.emit(event);

  }
  allPolicyHeader(files): Array<string>{
    var listColums = []
    files.filter(
      el => el.type == 'policy' && el.columns 
      ).forEach(
        el => el.columns.forEach(
          element => {if(!(listColums.find(sr => sr == element)))
            listColums.push(element)
            } 
          )
        )
        this.dataset.header = listColums
        return listColums
  }
  onFileLoaded(file) {
    if(this.dataset.mode == 2){var loadingFile = this.fileDisplay[this.selectedRank]
    var loadingFile1 = this.dataset.temporaryFile[this.selectedRank]
    }else {
      var loadingFile = this.fileDisplay[this.selectedRank]
      var loadingFile1 = this.dataset.files[this.selectedRank]}

    loadingFile["missing"] = file.missing
    loadingFile["columns"] = file.columns
    loadingFile["ignored"] = file.ignored
    loadingFile["duplicated"] = file.duplicated
    loadingFile["maxyear"] = file.maxyear
    loadingFile["minyear"] = file.minyear
    loadingFile["inconsistent"] = []
    loadingFile1["missing"] = file.missing
    loadingFile1["columns"] = file.columns
    loadingFile1["ignored"] = file.ignored
    loadingFile1["duplicated"] = file.duplicated
    loadingFile["maxyear"] = file.maxyear
    loadingFile["minyear"] = file.minyear
    loadingFile1["inconsistent"] = []
    if(this.fileDisplay.length> 2){
      var listColums = []
      listColums = this.allPolicyHeader(this.fileDisplay)
      listColums = this.allPolicyHeader(this.dataset.temporaryFile)
      this.updateInconsistante(this.fileDisplay,listColums)
      this.updateInconsistante(this.dataset.temporaryFile,listColums)
      
          
      // listColums.forEach(ele => ele.array.forEach(element => {
      //   if(! loadingFile["columns"].find(elem => elem == element ) ){
      //     loadingFile["Inconsistent"].push(element)
      //   }
      // }))

    }

    if (loadingFile["missing"].length > 0 || loadingFile["duplicated"].length > 0|| loadingFile["ignored"].length > 0) loadingFile.status = "bad"
    else loadingFile.status = "ok"

    this.modalRef.close()
  }
  updateInconsistante(files, list){
    
    if(list){
    files.forEach(element => { if(element.status != 'na' && element.type == 'policy'){
      element["inconsistent"] = []
      list.forEach(header => { 

        if(!(element["columns"].find(el => el == header)) ){
          element["inconsistent"].push(header)
          
        }
        
      });
    }
    });
  }
    }
      
  fileIndex(files: any[], type) {
    return files.findIndex(f => f.type == type)
  }

  goBack() {
    this.step--;
  }

  icon(status) {
    switch (status) {
      case "ok":
        return "check-circle";
      case "bad":
        return "exclamation-triangle";
      case "na":
        return " ";
      case "load":
        return "circle-o-notch";
      default:
        return "exclamation";
    }
  }

  color(status) {
    switch (status) {
      case "ok":
        return "success";
      case "bad":
        return "danger";
      case "na":
        return "warning";
      case "load":
        return "info";
      default:
        return "dark";
    }
  }

  modeName = (mode) => {
    if (this.selectedType == "product") return "product"
    switch(mode){
      case 0:
        return "split"
      case 1:
       return "combine"
      case 2:
       return "snapshot"
      
    }
  }

  closeResult: string;


  modalRef: NgbModalRef

  popUpCols

  open(content, size, cols?) {
    if (cols) this.popUpCols = cols;

    this.modalRef = this.modalService.open(content, {
      //backdrop: false,
      size: size
    });
  }

  openInfo(content, size, type:string) {
    if (type.toLowerCase() == "policy") this.popUpCols = this.policyPivot;
    if (type.toLowerCase() == "product") this.popUpCols = this.productPivot;

    this.modalRef = this.modalService.open(content, {
      //backdrop: false,
      size: size
    });
  }

  download(type:string) {
    let fileName : string =""
    if (type.toLowerCase() == "policy") fileName = "policy.xlsx"
    if (type.toLowerCase() == "product") fileName = "product.xlsx"

    this.fs.openFilePivots(fileName);
  }


}
