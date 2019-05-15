import {
  Component,
  OnInit,
  Input,
  SimpleChanges,
  SimpleChange,
  Output
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

  busy: boolean = false
  error
  step = 1
  policyPivot : string[] 
  productPivot : string[] 

  //Dataset Properties
  @Input() dataset: Dataset
  @Input() tab: Number
  @Input() mode: Number
  @Input() study: Study
  @Input() disabled: boolean
  //Dataset files
  @Output() metadata: EventEmitter < any > = new EventEmitter();  
  selectedType

  constructor(
    private ds: DatasetService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: NgbModal,
    private fs: FileService) {}

  ngOnInit() {
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

  onFileLoading(event, content: NgbModalRef) {
    var loadingFile = this.dataset.files.find(f => f.type == event.type)

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
          type: event.type
        })
      })
      .catch(error => {

        this.onFileLoadFail({
          error: error,
          type: event.type
        })
      })

    this.modalRef.close()
  }

  onFileLoadFail(event) {

    var loadingFile = this.dataset.files.find(f => f.type == event.type)
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
  onFileLoaded(file) {

    var loadingFile = this.dataset.files.find(f => f.type == file.type)

    loadingFile["missing"] = file.missing
    loadingFile["columns"] = file.columns
    loadingFile["ignored"] = file.ignored
    loadingFile["duplicated"] = file.duplicated

    if (loadingFile["missing"].length > 0 || loadingFile["duplicated"].length > 0|| loadingFile["ignored"].length > 0) loadingFile.status = "bad"
    else loadingFile.status = "ok"

    this.modalRef.close()
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
    return (mode == 0) ? "split" : "combine"
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
