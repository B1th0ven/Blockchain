import {
  Component,
  OnInit,
  Input,
  EventEmitter,
  Output,
  ElementRef,
  ViewChild
} from '@angular/core';
import {
  DatasetService
} from '../../services/dataset.service';
import {
  ActivatedRoute,
  Router,
  ActivatedRouteSnapshot
} from '@angular/router';
import {
  FileType
} from '../../shared/models/file-type';
import {
  Dataset
} from '../../shared/models/dataset';
import {
  Study
} from '../../shared/models/study';
import {
  date
} from '../../shared/models/date';
import {
  ToasterService
} from 'angular5-toaster/dist';
import { SCREEN, HoverOverService } from '../hover-over/hover-over.service';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { UsersService } from '../../services/users.service';


@Component({
  selector: 'app-study-dataset-create',
  host: {
    '(document:click)': 'hostClick($event)',
  },
  templateUrl: './study-dataset-create.component.html',
  styleUrls: ['./study-dataset-create.component.scss']
})
export class StudyDatasetCreateComponent implements OnInit {
  @Input() study: Study
  
  @Input() dataset: Dataset
  @Input() datasets: Array < Dataset >
  @Input() isDatasetAssociatedToRun : boolean
  @Input() doneControls: boolean;
  @Output() save: EventEmitter < any > = new EventEmitter < any > ()
  @Output() excecute: EventEmitter < any > = new EventEmitter < any > ()
  @Output() snapshotsave: EventEmitter <any> = new EventEmitter < any >()
  @ViewChild("d1") d1 :any
  @ViewChild("d2") d2 :any
  @ViewChild('d1Container') d1Container: ElementRef;
  @ViewChild('d2Container') d2Container: ElementRef;
  
  metadata: any;
  screen = SCREEN.DATASET;
  saved = false ;  
  busy: boolean = false
  err
  
  
  today: date = new date(new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate())

  files: [FileType]
  selectedType
  validated : boolean = false
  constructor(private us: UsersService,private _eref: ElementRef,private tsr: ToasterService, private router: Router, private ds: DatasetService, private route: ActivatedRoute,public hs: HoverOverService,private permissionsService: NgxPermissionsService) {}

  ngOnInit() {
    console.log("datasetcreator:p",this.dataset)
    this.dataset.studyId = this.study.id
    this.validated = this.study.isValidated()
    
    
  }

  canSave() {
    if (!this.dataset.name ||
      this.dataset.name == "" ||
      !this.validName() ||
      !this.validDate(this.dataset.exposureExtractionDate, this.study.endObsDate) ||
      !this.validDate(this.dataset.eventExtractionDate, this.study.endObsDate) ||
      !this.validDate(this.today, this.dataset.eventExtractionDate) ||
      !this.validDate(this.today, this.dataset.exposureExtractionDate) ||
      !this.checkValidDateFormat(this.dataset.eventExtractionDate) || 
      !this.checkValidDateFormat(this.dataset.exposureExtractionDate) || 
      !this.checkValidDateFormat(this.dataset.files[0].privacyDataDeletion) 
    ) return false
    if (this.busy) return false
    return true
  }

  saveDataset() {
    this.busy = true;
    
    if(!this.dataset.creator)
    this.dataset.creator = this.us.getStoredUser()
    this.ds.save(this.dataset).subscribe(
      data => {
        console.log("DATASET VIEW",this.dataset)
        this.dataset.mapFromApi(data)
        this.save.emit(this.dataset)
        this.busy = false;
        this.saved = true 
        this.dataset.changed = false;

        this.tsr.pop('success', "Done", "Dataset saved!")

      },
      err => {
        this.err = "Error";
        this.busy = false;
      }
    )
  }
  checkSnapDependancy(){
    if(this.dataset.mode && this.dataset.mode == 2 && this.dataset.firstSnapshot ){
     return true
  }else return false }
  loadMetaData(event){
    this.metadata = event;

  }
  validName() {
    if (!this.dataset.name || this.dataset.name == "") return true

    let res = this.datasets.filter(d => {
      if (!d.name || d.name == "") return false
      return (d.name.trim() == this.dataset.name.trim())
    })

    if (res.length > 1) return false;
    else return true;
  }

  concatinate(a,b) {
    if(a && b)
    return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }

  validDate(supDate: date, lowDate: date) {
    if (!supDate || !lowDate) return true

    if (isNaN(supDate.year) || isNaN(supDate.day) || isNaN(supDate.month)) return true
    if (isNaN(lowDate.year) || isNaN(lowDate.day) || isNaN(lowDate.month)) return true

    let low = new Date(lowDate.year + '-' + lowDate.month + '-' + lowDate.day)
    let sup = new Date(supDate.year + '-' + supDate.month + '-' + supDate.day)

    if (isNaN(low.getTime()) || isNaN(sup.getTime())) return true


    if (low <= sup) return true;

    return false
  }

  onDateChanged(indate:string,changedate:string) {
      if ( date.valid(this.dataset[indate]) && date.validFormat(this.dataset[indate]) && !date.valid(this.dataset[changedate]) )
        this.dataset[changedate] = new date(this.dataset[indate].year, this.dataset[indate].month, this.dataset[indate].day)
  }

  onModeChanged()
  {
    this.dataset.changed=true;
    this.dataset.files = null;
    this.dataset.createDatasetFileHolders();
    
  }

  hostClick(event) {
    if(this.d1 && this.d2)
    {  if (this.d1.isOpen()) {
        if (this.d1Container && this.d1Container.nativeElement && !this.d1Container.nativeElement.contains(event.target)) {
          this.d1.close();
        }
      }
      if (this.d2.isOpen()) {
        if (this.d2Container && this.d2Container.nativeElement && !this.d2Container.nativeElement.contains(event.target)) {
          this.d2.close();
        }
      }}
  }
  addSlash(key) {
    this.dataset[key].month = 0
  }
  
  canEdit(){
    if(!this.permissionsService.getPermission('DATASET_CREATOR') && 
        !this.permissionsService.getPermission('DATASET_UPLOADER') &&
      !this.permissionsService.getPermission('DATASET_CONTROLS_EXECUTER') &&
    !this.permissionsService.getPermission('DATASET_EDITOR')) return false
    else return true
  }

  checkValidDateFormat(date : date) {
    if(!date) {
      return true
    }
    let date2 = new Date();
    date2.setFullYear(date.year, date.month - 1, date.day);
    return date.valid
  }
  dataInjectionChange(event) {
    this.dataset.dataInjectionTableau = event
    console.log(this.dataset);
    
  }

  isStudyValidated() {
    return this.study.status.toLowerCase().trim() == "validated"
  }

}
