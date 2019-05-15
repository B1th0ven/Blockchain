import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  ViewChild,
  ChangeDetectionStrategy
} from '@angular/core';
import {
  ExpectedTable
} from '../../models/expected-table.model';
import * as options from "./table-form-values"
import {
  TablesService
} from '../../services/tables.service';
import {
  ToasterService, BodyOutputType, Toast
} from 'angular5-toaster/dist/angular5-toaster';
import {
  NgbModal, NgbTypeahead
} from '@ng-bootstrap/ng-bootstrap';
import {
  DateFormatPipe
} from '../../../../shared/pipes/date-format.pipe';
import * as validator from './table-form.validator';
import {
  HttpEventType,
  HttpResponse
} from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { map, debounceTime, distinctUntilChanged, filter } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';
import { UsersService } from '../../../../services/users.service';
import { NgxPermissionsService } from '../../../../../../node_modules/ngx-permissions';
import { FileService } from '../../services/file.service';

@Component({
  selector: 'app-table-definition',
  templateUrl: './table-definition.component.html',
  styleUrls: ['./table-definition.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export class TableDefinitionComponent implements OnInit {

  err = null
  busy = false;
  loading = false;
  options = options
  validator = validator
  countries
  editable : boolean = true
  percentDone = 0

  uploading: boolean = false;
  // isConfidential : boolean = false

  runningControls = {
    calc: false,
    comp: false,
    tech: false,
    func: false,
    calcLoading: false,
    compLoading: false,
    techLoading: false,
    funcLoading: false,
  }

  constructor(private ets: TablesService, private tsr: ToasterService, private modalService: NgbModal, private df: DateFormatPipe,private us : UsersService,private permissionsService: NgxPermissionsService,private fs: FileService) {}
  table: ExpectedTable
  get func(): ExpectedTable {
    return this.table;
}

  @Input('table') 
  set func(value: ExpectedTable) {
    this.table = value
    if(this.table){
    this.ets.checkTableAssociationToRun(this.table.id,this.table.type).subscribe(res => {
      let a : Array<any> = res;   
      if(a.length>0 ) {
        this.editable = false
      } else {
        this.editable = true
      }
    }, err => {
      this.tsr.pop("error", "Error", "Server Error")
    })
    }
  }

  @Output() onSave = new EventEmitter()
  @Output() onDelete : EventEmitter<any> = new EventEmitter<any>() 

  @ViewChild("content") modal;

  @ViewChild("modal") deleteModal;

  comp_num

  ngOnInit() {

    this.comp_num = TableDefinitionComponent.COMPONENT_NUMBER++;

    if (!this.table) {
      this.table = new ExpectedTable()
      this.table.source = "manual"
    }
    this.table.func_report = this.table.func_report || this.ets.initFuncControls()
    
    this.busy = true;
    this.ets.getCountries().subscribe(
      res => {
        this.busy = false;
        this.countries = res;
        this.countries.sort(function (a, b) {
          if (a['rcName'] < b['rcName'] ) return -1;
          else if (a['rcName']  > b['rcName'] ) return 1;
          return 0;
        });
      },
      err => {
        this.busy = false;
        this.err = err
      }
    )
    
    
  }

  

  select(event) {}

  canSave() {
    return (!this.loading && validator.isValid(this.table));
  }

  save() {
    if (!this.canSave()) return
    this.loading = true;
    if(!this.table.status) this.table.status = "active"
    if(!this.table.creator)
    this.table.creator = this.us.getStoredUser()
    this.ets.latestTable(this.table).subscribe(
      res => {
        this.loading = false;
        if (Number(res) == 0) {
          this.saveConfirmation();
        } else {
          this.modalService.open(this.modal)
        }
      },
      err => {
        this.loading = false;
        this.tsr.pop("error", "Error", "Table not saved")
      }
    )
  }

  previousCode = null;

  private saveConfirmation() {
    this.loading = true;
    if(!this.table.status) this.table.status = "active"
    if(!this.table.creator)
    this.table.creator = this.us.getStoredUser()
    this.ets.saveTable(this.table).subscribe((res: ExpectedTable) => {
      console.log("res res res res save ",res)
      this.loading = false;
      this.tsr.pop("success", "Success", "Table saved");
      this.previousCode = this.table.code
      this.table = res;
      this.us.getRoleByExpID(this.table.id,this.us.getStoredUser().ruId).subscribe(
        res => {this.getPermissions(res)},
        err => this.tsr.pop('error', "Error", "Server Error")
      )
      this.onSave.emit({
        table: this.table,
        previousCode: this.previousCode
      });
    }, err => {
      this.loading = false;
      this.tsr.pop("error", "Error", "Table not saved");
    });
  }

  onConfirmCreation() {
    this.saveConfirmation()
  }

  getHeaderInfo() {
    // console.log(this.table);
    
    return [{
      title: 'Table ID',
      value: this.table.code
    }, {
      title: 'Created By',
      value: this.getCreatorName()
    }, {
      title: 'Creation Date',
      value: this.table.creation_date ? this.table.creation_date.toString() : null
    }, {
      title: 'Confidential Status',
      value: this.getConfStatus()
    }]
  }

  getCreatorName(){
    if(this.table.creator)
    return this.concatinate(this.table.creator.ruFirstName,this.table.creator.ruLastName)
    else return ""
  }

  getConfStatus(){
    // if(this.table.origin && this.table.origin.toLocaleLowerCase().trim() == "scor")
    // return "Confidential"
    // else return "Not Confidential"
    if(this.table.isConfidential) return "Confidential"
    else return "Not Confidential"
    
  }

  concatinate(a,b) {
    if(a && b)
    return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }

  startUpload(event) {
    this.uploading = true;
  }

  failedUpload(err) {
    this.tsr.pop("error", "Error", err)
  }

  onFileUploaded(file) {
    this.tsr.pop("success", "Success", "The file has been uploaded")
    this.uploading = false;
    this.table.path = file.path;

    this.table.func_report = this.ets.initFuncControls();
    this.table.tech_report = null;
    this.table.comp_report = null;

    this.runningControls.calcLoading = true
    this.runningControls.compLoading = true
    this.runningControls.techLoading = true
    this.runningControls.funcLoading = true

    this.ets.getCalcControls(this.table.path, this.table.type).subscribe(res => {
      console.log("xxxxx----------------",res)
      //Age
      this.table.age_max = res.max_age
      this.table.age_min = res.min_age
      //Duration
      this.table.duration_max = res.max_duration
      this.table.duration_min = res.min_duration
      //Calendar
      this.table.cal_year_max = res.max_calendar
      this.table.cal_year_min = res.min_calendar

      //this two objects are defined to use them in the combination control
      let maxValues = {
        age : (this.table.age_max != -1) ? this.table.age_max : null,
        duration_year :  (this.table.duration_max != -1) ? this.table.duration_max : null,
        calendar_year :  (this.table.cal_year_max != -1 && this.validator.isTrend(this.table)) ? this.table.cal_year_max : null
      }

      let minValues = {
        age : (this.table.age_min != -1) ? this.table.age_min : null,
        duration_year : (this.table.duration_min != -1) ? this.table.duration_min : null,
        calendar_year : (this.table.cal_year_min != -1 && this.validator.isTrend(this.table)) ? this.table.cal_year_min : null
      }

      this.ets.getCompControls(this.table.path, this.table.type).subscribe(res => {
        this.table.comp_report = res;
        this.runningControls.compLoading = false;
      }, err => this.tsr.pop("error", "Error", err))
      this.ets.getTechControls(this.table.path, this.table.type).subscribe(res => {
        this.table.tech_report = res;
        this.runningControls.techLoading = false;
        this.ets.getFuncControls(this.table.path, this.table.type,maxValues,minValues).subscribe(res => {
          console.log(res);
          
          this.table.func_report = res;
          this.table.dimensions = res["header"]
          this.runningControls.funcLoading = false;
          console.log("Func Report ----> ",this.table.func_report);
        }, err => this.tsr.pop("error", "Error", err))
      }, err => this.tsr.pop("error", "Error", err))
      


    }, err => this.tsr.pop("error", "Error", err))
  }

  getColumnValue(columnName: string, header: string, line: string) {
    if (!header || !columnName || !line) return "MISSING DATA"
    let ind = header.split(";").findIndex(head => head.toLowerCase() == columnName.toLowerCase())

    return (ind >= 0) ? line.split(";")[ind] : "N/A"
  }

  getFileName(str: string) {
    if (str) {
      return str.split("/").pop()
    }
  }

  getStatus() {
    if (this.uploading) return "Uploading file"
  }

  getNumberValue(nbr) {
    return (Number(nbr) >= 0) ? nbr : "N/A"
  }


  /** ++++++++++ */
  /** TYPE AHEAD */
  /** ++++++++++ */
  @ViewChild('appYearAhead') appYearAhead: NgbTypeahead;
  focusAppYear$ = new Subject<string>();
  clickAppYear$ = new Subject<string>();
  applicationYear = (text$: Observable<string>) =>
  {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
    const clicksWithClosedPopup$ = this.clickAppYear$.pipe(filter(() => !this.appYearAhead.isPopupOpen()));
    const inputFocus$ = this.focusAppYear$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      map(term =>
       options.years(1970,2018)/*.filter(y => String(y).toLowerCase().indexOf(String(term).toLowerCase()) > -1)*/)
    );
  }

  @ViewChild('pubYearAhead') pubYearAhead: NgbTypeahead;
  focusPubYear$ = new Subject<string>();
  clickPubYear$ = new Subject<string>();
  publicationYear = (text$: Observable<string>) =>
  {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
    const clicksWithClosedPopup$ = this.clickPubYear$.pipe(filter(() => !this.pubYearAhead.isPopupOpen()));
    const inputFocus$ = this.focusPubYear$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      map(term =>
       options.years(1970,2018)/*.filter(y => String(y).toLowerCase().indexOf(String(term).toLowerCase()) > -1)*/)
    );
  }
  /** ---------- */
  /** TYPE AHEAD */
  /** ---------- */


  static COMPONENT_NUMBER = 0

  /** -------------------- */
  /**DELETE TABLE LIBRARY  */
  /** -------------------- */

  message = {}
  deleteClick(deletePopup){
    this.message = {title:"Warning",msg:"Do you really want to delete the table ?",icon:"delete",confirm : () => {this.delete()}}
    this.modalService.open(deletePopup)
  }

  generateMessage(res) {
    let message = "<div>This table is associated to : <ul> "
    res.forEach(run => {
      message = message + "<li> Run " + run.runId + " of Study " + run.study.stCode + "</li>"
    });
    return message + "</ul></div>";
  }

  delete() {
    console.log(this.table);
    this.ets.checkTableAssociationToRun(this.table.id,this.table.type).subscribe(res => {
      console.log(res); 
      let a : Array<any> = res;   
      if(a.length>0 ) {
        let message = this.generateMessage(res);

        var toast : Toast = {
          type: 'warning',
          title: 'Warning',
          body: message,
          bodyOutputType: BodyOutputType.TrustedHtml
      };
        // let msg : string = res.map(run => run.runId + ", ")
        // this.tsr.pop("warning","Warning","This table is associated to runs : " + msg[0].substring(0,msg[0].length-2))
        this.tsr.pop(toast)

      } else {
        this.ets.deleteTable(this.table.id).subscribe( res => {
          if(res && res == true) {
            this.onDelete.emit(this.table.id + " is deleted")
            this.tsr.pop("success","Success","The table has been deleted")
          } else {
            this.tsr.pop("error","Error","An Error incured while deleting table")
          }
          
        }, err => {
          console.log(err)
          this.tsr.pop("warning","Warning",err.message)
        })
      }
    }, err => {

    })
  }

  isTableActivated(){
    if(this.table.status && this.table.status.toLocaleLowerCase().trim() == "inactive")
    return false
    else return true
  }

  setTableStatus(e){
    if(e) this.table.status = "active"
    else this.table.status = "inactive"
    console.log(this.table)
  }

  setTableConfidential(e) {
    if(e) this.table.isConfidential = true
    else this.table.isConfidential = false
    
  }

  isUserEditor(){
    if(this.permissionsService.getPermission('TABLE_WRITER'))
    return true
    else return false
  }

  isUserReader() {
    if(this.permissionsService.getPermission('TABLE_READER'))
    return true
    else return false
  }

  getPermissions(res){     
    this.permissionsService.flushPermissions()
    this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
    this.permissionsService.addPermission(res.primaryRole)
    res.secondaryRoles.forEach(element => {
    this.permissionsService.addPermission(element) });   
    
  }

  canDelete(){
    
    if(this.permissionsService.getPermission('TABLE_DELETER'))
    return true
    else return false
  }

  canActivateTable(){
    
    if(this.permissionsService.getPermission('TABLE_STATUS'))
    return true
    else return false
  }

  countrychange() {
    let id = this.table.country_id
    
    let a = this.countries.filter(c => c.rcId == id);
    if(a && Array.isArray(a)){
     this.table.country = a[0]
        }

  }

  download(){
    this.fs.openFile(this.getFileName(this.table.path))
  }

  onDeleteTable(content){
    this.modalService.open(content)
  }

  confirmDeletion(c){
    c('Close click')
    this.tsr.pop("success","Success","The table has been deleted")

  }

  isFileOnServer(){
    this.fs.isFileOnServer(this.getFileName(this.table.path)).subscribe(
      res => {
        console.log(res)
        if(res["_body"] == "true") this.download()
        else this.tsr.pop("error","Error","File not found")
      } , err =>  this.tsr.pop("error","Error","Server Error")
    )
  }

  onChangeOrigin() {
    if(this.table.origin == 'SCOR') this.table.isConfidential = true
    else this.table.isConfidential = false 
  }

  formatCombinationControlExample(val){
    let res = this.replaceAll(val)
   // console.log("vaaal -------->",res)
    //if(res.substr(res.length - 1) == "-") res = res.slice(0,-1)
    if(res.length > 200) return res.slice(0,200)+"..."
    else return res
  }

 replaceAll(val) {
   let res = val.split(';').join('-')
   if(res.substr(res.length - 1) == "-") res = res.slice(0,-1)
   return res;

 }
}
