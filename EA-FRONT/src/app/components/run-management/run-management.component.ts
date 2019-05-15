import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  ChangeDetectorRef,
  OnChanges,
  SimpleChanges,
  ViewChild,
  ViewChildren,
  QueryList
} from '@angular/core';
import {
  Study
} from '../../shared/models/study';
import {
  Run
} from '../../shared/models/run';
import {
  Dataset
} from '../../shared/models/dataset';
import {
  FormValidatorService
} from '../../services/form-validator.service';
import {
  DatasetService
} from '../../services/dataset.service';
import {
  forkJoin
} from 'rxjs/observable/forkJoin';
import {
  RunService
} from '../../services/run.service';
import {
  ToasterService
} from 'angular5-toaster/dist/angular5-toaster';
import {
  FileService
} from '../../services/file.service';
import {
  NgbModal,
  ModalDismissReasons
} from '@ng-bootstrap/ng-bootstrap';
import {
  Decrement
} from '../../shared/models/decrement';
import {
  Subject
} from 'rxjs';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { UsersService } from '../../services/users.service';
import { RunGeneralComponent } from './run-general/run-general.component';



@Component({
  selector: 'app-run-management',
  templateUrl: './run-management.component.html',
  styleUrls: ['./run-management.component.scss']
})
export class RunManagementComponent implements OnInit {

  @Input() study: Study
  @Input() runs: Run[]

  dimensions: Array < string >= new Array < string > ()
  datasets: Dataset[]

  @Output() continueControl: EventEmitter < any > = new EventEmitter < any > ()
  @Output() runsSaved: EventEmitter < any > = new EventEmitter < any > ()
  @Output() runDeleted: EventEmitter < any > = new EventEmitter < any >()
  @Output() runStatusChanged: EventEmitter < any > = new EventEmitter < any >()

  @ViewChildren(RunGeneralComponent)    runGeneralComponent:QueryList<RunGeneralComponent>
  tabs: tab[] = []
  activeTab: number

  busy: boolean = false;
  checkAdjust:boolean = false
  err: string;

  initialized: boolean = false

  tab_number: number;

  modalMsg: any = {};

  constructor(private us: UsersService,private permissionsService: NgxPermissionsService,private cdr: ChangeDetectorRef, private fv: FormValidatorService, private ds: DatasetService, private rs: RunService, private tsr: ToasterService, private modalService: NgbModal) {}

  ngOnInit() {
    this.tab_number = RunManagementComponent.RUN_MANAGEMENT_COUNT++;
    this.onInit()
  }

  public onInit() {
    if (!this.initialized) {
      this.busy = true
      let datasets = this.ds.getByStudyId(this.study);
      let dimensions = this.ds.getDimensions();
      let runs = this.rs.getByStudyId(this.study.id)

      forkJoin([datasets, dimensions, runs]).subscribe(results => {
        this.datasets = new Array < Dataset > ()
        results[0].forEach(element => {
          let d = new Dataset();
          let mappedDataset = d.mapFromApi(element)
          this.datasets.push(d.mapFromApi(element));
        })
        this.dimensions = results[1]
        this.runs = results[2]

        this.runs.forEach(run => {
          run.setDimensionCheckers(this.dimensions,this.datasets,run.dataset)
        });

        this.generateTabs(this.runs)

        this.initialized = true
        this.activeTab = this.activeTab || 0
        this.busy = false
      }, err => this.err = err)
    } else {
      this.busy = false
    }
  }

  public getDatasets() {
    let datasets = this.ds.getByStudyId(this.study);
    let dimensions = this.ds.getDimensions();
    forkJoin([datasets, dimensions]).subscribe(results => {
      this.datasets = new Array < Dataset > ();
      results[0].forEach(element => {
          let d = new Dataset();
          this.datasets.push(d.mapFromApi(element));

          this.busy = false;
        },
        err => {
          this.err = "Error"
        }
      );
      this.dimensions = results[1];

      this.activeTab = this.activeTab || 0;
      this.busy = false;
    });
  }

  checkAdjustment(e){
    this.checkAdjust = e
  }

  newTab(type, event ? ) {
    if (this.canCreateRun()) {
      this.runs.push(new Run("Run", this.study.id))
      this.tabs.push({
        type: "newrun",
        name: "Create a run",
        changed: false
      })
      this.activeTab = this.tabs.length - 1
    }
  }

  openTab(ind) {
    this.activeTab = ind
  }

  closeTab(tabInx, event) {
    this.tabs.splice(tabInx, 1)
  }

  remove(ind, e: Event) {
    e.preventDefault()
    if (this.activeTab >= ind)
      this.activeTab = Math.max(0, this.activeTab - 1)

    this.tabs.splice(ind, 1)
    this.runs.splice(ind, 1)
  }

  canCreateRun = () => this.runs.length < 5 && this.permissionsService.getPermission("RUN_CREATOR") && !this.study.isValidated();

  updateTab(dataset: Dataset, i) {
    let tab = this.tabs[i]
    tab["name"] = dataset.name
  }

  onContinueControl = (event) => this.continueControl.emit(event)

  canSave(fv: FormValidatorService, run): boolean {
    run.canSaveRun = (fv.isRunValid(run,this.study) && !run.running && !this.loading)
    return (fv.isRunValid(run,this.study) && !run.running && !this.loading)
  }

  canRun(fv: FormValidatorService, run): boolean {
    //let isDecsSaved = true;
    // run.decrements.forEach(s=> s.id == undefined ? isDecsSaved = false : null)
    return (fv.isRunValid(run,this.study) && !run.running && !this.loading /*&& run.id && isDecsSaved*/ )
  }

  concatinate(a,b) {
    if(a && b)
    return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }


  loading = false;
  save(run: Run) {
    console.log("----------------save------------------",run)
    if(this.study.status && this.study.status.toLowerCase().trim() == "validated") {
      this.rs.saveAfterValidation(run).subscribe(res => {
        this.tsr.pop("success", "Success", "Run saved")
      }, err => {
        this.tsr.pop("error", "Error", "Run not saved")
      })
      return;
      
    }


    let saveSatus = new Subject()
    if (!this.canSave(this.fv, run)) return
    this.loading = true

    run.run_calc = null;
    if(!run.createdBy)
    run.createdBy = this.us.getStoredUser()
    this.rs.save(run).subscribe(
      res => {
        run.id = res.id
        run.decrements.forEach((decrement, i) => {
          decrement.id = res.decrements[i].id
        });

        this.loading = false
        this.runsSaved.emit(this.runs)
        this.tsr.pop("success", "Success", "Run saved")
        this.study.countRuns++
        saveSatus.next(res)
        saveSatus.complete()
      },
      err => {
        this.loading = false
        this.tsr.pop("error", "Error", "Run not saved")
        saveSatus.error(err)
      }
    )
    return saveSatus
  }

  onExpRunSave(e,i){
   this.runsSaved.emit(this.runs)
  // console.log("exprunsave-------------->",e,this.runs[i])
  }

  launch(run: Run) {

    let AllDimensions = "";
    for (let i of run.allDimensions) {
      // if (run.dimensions.indexOf(i) < 0)
      AllDimensions += i.itemName + " "
    }
    let dimensions = "";
    for (let i of run.dimensions) {
      // if (run.dimensions.indexOf(i) < 0)
        dimensions += i.itemName + " "
    }

    run.running = true;
    this.rs.launchRun(run.id, AllDimensions,dimensions).subscribe(
      res => {
        this.tsr.pop("info", "Info", "Run launched");
        run.run_calc = res;
        this.rs.getContinuousRunStatus(run.id).subscribe(
          res => {
            run.run_calc = res;
            let status = this.rs.getStatus(run.run_calc.rclcStatus,run.run_calc.rclcStatusEa)

            switch (String(status).toUpperCase()) {
              case "ERROR":
              case "CALCULATION ERROR":
              case "ABORTED":
              case "DONE":
                {
                  this.tsr.pop("", "Run Completed", "Status: " + String(status).toUpperCase())
                  this.runStatusChanged.emit(this.runs)
                }
            }
          },
          err => {
            this.tsr.pop("error", "Error", "Couldn't fetch run update");
            run.running = false;
          },
          () => {
            run.running = false;
          }
        )
      },
      err => {
        this.tsr.pop("error", "Error", "Run not launched");
        run.running = false;
      }
    )

  }

  launchAndSave(run: Run) {
    this.save(run).subscribe(
      res => this.launch(run)
    )
  }


  generateTabs(runs: Run[]) {
    this.tabs.splice(0)
    if (!this.runs) {
      this.runs = new Array < Run > ();
      this.tabs = new Array < tab > ();
    }

    if (this.runs.length == 0) {
      this.runs.push(new Run("Run", this.study.id));
      this.tabs.push({
        type: "newrun",
        name: "Create a run",
        changed: false
      });
    } else {
      this.study.countRuns = this.runs.length
      runs.forEach(run => {
        this.tabs.push({
          type: "newrun",
          name: "Create a run",
          changed: false
        });
      })
    }
  }

  pushDataset(event) {
    let dataset = event.dataset

    if (!this.datasets) 
      this.datasets = []

    let index = this.datasets.findIndex(d => d.id == dataset.id)

    if(index>-1) 
      this.datasets[index] = dataset
    else 
      this.datasets.push(dataset)
    
    this.runGeneralComponent.forEach(rgc=> {

      rgc.datasets = this.datasets
    })
  }

  changeDatasetname(dataset: Dataset) {
    let ind = this.datasets.findIndex(d => d.id == dataset.id)
    if (ind >= 0) this.datasets[ind].name = dataset.name
  }

  deleteDataset(dataset: Dataset){
    //console.log("run managment ------->",dataset)
    let ind = this.datasets.findIndex(d => d.id == dataset.id)
    this.datasets.splice(ind,1)
  }

  static RUN_MANAGEMENT_COUNT = 0;

  // DOWNLOAD
  download(run) {
    this.rs.downloadReport(run)
  }

  downloadErrorReport(run: Run){
    this.rs.isErrorFileExist(run).subscribe(
      res => {
        if(res) this.rs.downloadError(run)
        else this.tsr.pop("error", "Error", "Cannot locate the error report")
      },
      err => this.tsr.pop("error", "Error", "Server Error")
    )
  }

  canDownload(run: Run) {
    return (run.run_calc && run.run_calc.rclcStatus == "DONE")
  }
  
  canDownloadError(run: Run) {
    if (run.run_calc && (run.run_calc.rclcStatus == "ERROR" || run.run_calc.rclcStatus == "ABORTED")) 
    {      
      return true
    }
  }

  canDelete(run: Run) {
    return (this.study && this.study.status && this.study.status.toLocaleLowerCase().trim() == "progress") &&
      run.id;
  }

  openDeleteModal(content) {

    this.modalMsg = {
      title: "Warning",
      body: "Do you really want to delete the run ?"
    }
    this.modalService.open(content);

  }

  delete(run: Run, ind: number, c: any, event) {
    if (run.id) {
      this.rs.delete(run.id).subscribe(
        res => {
          this.runDeleted.emit(run)
          this.study.countRuns = this.study.countRuns - 1
          if (this.tabs.length == 1) {
            this.tabs.splice(ind, 1)
            this.runs.splice(ind, 1)
            this.newTab("new", event)
          } else {
            this.remove(ind, event)
          }
          this.tsr.pop("success", "Success", "The run has been deleted")
        },
        err => {
          console.log(err)
          this.tsr.pop("error", "Error", "Server Error");
        }
      )
    } else this.tsr.pop("warning", "Warning", "The run is not saved yet")
    c('Close click')
  }

  canDuplicate() {
    return this.runs.length < 5 && this.study.status.toLowerCase().trim() != "validated";
  }

  duplicateRun(run: Run) {
    let dupRun: Run = run.clone();
    dupRun.id = undefined
    dupRun.decrements.forEach(d => d.id = undefined);
    if (this.canCreateRun()) {
      this.runs.push(dupRun)
      this.tabs.push({
        type: "newrun",
        name: "Create a run",
        changed: false
      })
      this.activeTab = this.tabs.length - 1
    }

  }

  canEditRun() {
 //  console.log("------------------------eeeee", this.canEditPerm())
    return (this.study && this.study.status && this.study.status.toLocaleLowerCase().trim() == "progress");
  }

  onLaunchDone()
  {
    this.runsSaved.emit(this.runs)
  }

  canEditPerm(){
    if(this.permissionsService.getPermission("RUN_EDITOR"))
    return true
    else return false
  }

  canUserDeleteRun(){
    if(this.permissionsService.getPermission("RUN_DELETER"))
    return true
    else return false
  }

  canUserLaunchRun(){
    if(this.permissionsService.getPermission("RUN_LAUNCHER"))
    return true
    else return false
  }

}

function arr_diff(a1, a2) {
  return a1.filter(function (i) {
    return a2.indexOf(i) < 0;
  });
}

interface tab {
  name
  type
  changed
}
