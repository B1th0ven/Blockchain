import {
  Component,
  OnInit,
  ViewContainerRef,
  Input,
  EventEmitter,
  Output
} from '@angular/core';
import { DatasetService } from '../../services/dataset.service';
import { ControlService } from '../../services/control.service';
import { BignumberPipe } from '../../shared/pipes/bignumber.pipe';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router/src/router';
import { error, isArray } from 'util';
import { Dataset } from '../../shared/models/dataset';
import { Control } from '../../shared/models/control';
import { ToasterService } from 'angular5-toaster/dist';
import { Study } from '../../shared/models/study';
import { DatasetControlSingleComponent } from '../dataset-control-single/dataset-control-single.component';
import { DatasetControlDataService } from '../../services/dataset-control-data.service';
import { DatasetControlLogicService } from '../../services/dataset-control-logic.service';
import { UsersService } from '../../services/users.service';
declare let $: any;
 import * as jspdf from 'jspdf'
 import * as html2canvas from 'html2canvas'
import { doesNotThrow } from 'assert';
@Component({
  selector: 'app-dataset-control',
  templateUrl: './dataset-control.component.html',
  styleUrls: ['./dataset-control.component.scss']
})
export class DatasetControlComponent implements OnInit {
  notExecutedJSON: any;

  constructor(
    private tsr: ToasterService,
    private ds: DatasetService,
    private cs: ControlService,
    private us: UsersService,
    private route: ActivatedRoute,
    private dds: DatasetControlDataService,
    private ls: DatasetControlLogicService
  ) {}

  @Input() study: Study;
  @Input() dataset: Dataset;
  initialized = false;
  @Input() tabNbr: number = 0;
  files: any;
  error: {
    icon;
    message;
  };
  hasReport: boolean = false;
  @Output() continue: EventEmitter<any> = new EventEmitter<any>();
  @Output() done: EventEmitter<boolean> = new EventEmitter<boolean>();

  index = 1 
  productHeader: Array<String>;
  policyHeader: Array<String>;

  formatControl: Control;
  productFormatControl: Control;
  controls: any;
  productControls: any;
  funcControls: Array<Control>;
  snapshotControl: Array<Control>;

  //REPORT
  funcReport: any;
  techReport: any;
  notExecuted: String[];
  grayedCancel: boolean;
  user = null;
  canceled= false;
  controlsDone : Boolean;

  ngOnInit() {
    this.dds.InitControls();
    this.us.user.subscribe(user=>this.user = user);
    
  }
  startControls() {
    
    if(this.dataset.mode == 2 ){
      this.ds.saveSnapshotFiles(this.dataset).subscribe()
      this.index = 5
    }
    this.controlsDone = false;
    this.initialized = true;
    this.snapshotControl = (this.dataset.mode == 2 ) ? this.dds.InitSnapshotControl() : null;
    this.formatControl = this.dds.InitFormatControl();
    console.log(this.formatControl.order);
    this.controls = this.dds.InitControls();
    this.productControls = this.dds.InitProductControls();
    this.productFormatControl = this.dds.InitProductFormatControl();
    this.funcControls = this.dds.InitFuncControls();
    if(this.dataset.mode == 2){
      this.files = this.dataset.temporaryFile
    }else {
    this.files = this.dataset.files;}
    if (this.dataset.hasReport()) {
      this.hasReport = true;
      this.viewExistingReport();
    } else if (this.ds.canDoControl(this.files)) {
      this.runNewReport();
      this.canceled = false;
    }
    console.log(this.funcControls.find(e => e.order == 51))
  }
  fileSaveStatus = {
    loading: false,
    loaded: false,
    error: false
  };

  cancelExecution() {
    //call of backend to cancel
    for(let i = 0; i < [this.formatControl,this.productFormatControl].length+this.productControls.length+this.funcControls.length; i++ ){
      $('#collapsefunc-' + i + '-' + this.tabNbr).collapse('hide');
    }
    this.cs.sendCancelRequest(this.study.id,this.user.ruLogin).subscribe((res) => {
      //Update view
      
      this.canceled = true;
      
      [[this.formatControl,this.productFormatControl],this.productControls, this.funcControls].forEach(element =>
        this.ls.updateCancel(element)  
      );

    }, (err) => {
      //Update view
      this.canceled = true;
      [[this.formatControl,this.productFormatControl],this.productControls, this.funcControls].forEach(element =>
        this.ls.updateCancel(element)
      );

    });
  }
  public captureScreen()  
  {  
    var data = document.getElementById('contentToConvert');  
    html2canvas(data).then(canvas => {  
      // Few necessary setting options  
      var imgWidth = 180;   
      var pageHeight = 295;    
      var imgHeight = canvas.height * imgWidth / canvas.width;  
      var heightLeft = imgHeight;  
  
      const contentDataURL = canvas.toDataURL('image/png')  
      let pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF  
      var position = 0;  
      pdf.addImage(contentDataURL, 'PNG', 14, 28, imgWidth, imgHeight)  
      pdf.save('Report_'+this.study.code+'.pdf'); // Generated PDF   
    });  
  } 
  continueClick() {
    this.saveReport(true);

  }

  saveClick() {
    this.saveReport(false);
  }

  private saveReport = goToRun => {
    if (this.dataset.hasReport()) {
      this.continue.emit({ goToRun: goToRun, dataset: this.dataset });
      return;
    }

    this.fileSaveStatus.loading = true;
    this.fileSaveStatus.error = false;
    this.dataset.funcReport = this.funcReport;
    this.dataset.techReport = this.techReport;
    this.dataset.notExecReport = this.notExecutedJSON;
    this.ds.saveSnapshotFiles(this.dataset).subscribe()
    this.ds.saveFiles(this.dataset).subscribe(
      res => {
        this.fileSaveStatus.loading = false;
        this.fileSaveStatus.loaded = true;

        this.study.count++;
        this.dataset.mapFromApi(res);
        this.tsr.pop('success', 'Success', 'Report saved!');

        this.continue.emit({ goToRun: goToRun, dataset: this.dataset });
      },
      err => {
        this.fileSaveStatus.loading = false;
        this.fileSaveStatus.loaded = true;
        this.fileSaveStatus.error = true;

        this.tsr.pop('danger', 'Error', 'Failed to save report!');
      }
    );
  };

  updateFormat = (resControls, typeFormatControl, controls) => {
    this.updateFormatInner(resControls, typeFormatControl, controls);

    typeFormatControl.errors = 0;
    controls.forEach(c => {
      typeFormatControl.errors += c.errors;
    });

    typeFormatControl.status = 'done';
    typeFormatControl.done = true;
    typeFormatControl.valid = typeFormatControl.errors > 0 ? 'no' : 'yes';
    typeFormatControl.running = false;
    typeFormatControl.message =
      typeFormatControl.errors > 0
        ? new BignumberPipe().transform(String(typeFormatControl.errors)) +
          ' error(s)'
        : 'Done';
  };
  updateNotExecutedControl(res: any, funcControls: any, notExecuted: any): any {
    //if (!Array.isArray(res)) return
    if (!res) return;
    notExecuted = [];
    res.forEach(notExecCtrName => {
      let control = funcControls.find(
        c =>
          c.identifier.toLocaleLowerCase() ==
          notExecCtrName.control.toLocaleLowerCase()
      );
      if (!control) return;
      control.notExecuted = notExecCtrName;
      notExecuted.push(notExecCtrName.control);
      this.updateFailed([control]);
    });
    this.notExecuted = notExecuted
  }
  updateFormatInner = (resControls, typeFormatControl, controls) => {
    controls.forEach(c => {
      if (c.valid) return;
      let resControl = resControls.find(r => r.control == c.identifier);
      if (resControl) {
        let affaected = resControl.affectedColumns;

        if (affaected.length > 0) {
          c.errors = 0;
          affaected.forEach(element => {
            c.errors += element['errorsNumber'];
          });
        } else {
          c.errors = 0;
        }

        c.affectedColumns = resControl.affectedColumns;
        c.control = resControl.control;
        c.total = c.errors;
        c.status = 'done';
        c.done = true;
        c.valid = c.errors > 0 ? 'no' : 'yes';
        c.running = false;

        c.message =
          c.errors > 0
            ? new BignumberPipe().transform(c.errors) + ' error(s)'
            : 'Done';
      }
    });
  };
  updateMissingColumnControls = (controlType, controls, id) => {
    let control: Control = controls.find(c => c.identifier == id);

    control.examples = [];
    control.status = 'done';
    control.done = true;
    control.running = false;
    control.affectedColumns = controlType.affectedColumns;
    let errors = 0;
    controlType.affectedColumns.forEach(affectedColumn => {
      errors = errors + affectedColumn.errorsNumber;
    });
    control.errors = errors;
    control.valid = control.errors > 0 ? 'no' : 'yes';
    control.message =
      control.errors > 0
        ? new BignumberPipe().transform(String(control.errors)) + ' error(s)'
        : 'Done';

    // console.log(control);
  };
  update = (resControls: Array<any>, controls) => {
    resControls.forEach(controlType => {
      if (
        controlType.control == 'Missing Values Check' ||
        controlType.control == 'Waiting Period' ||
        controlType.control == 'Status at benefit end / claim payment end' ||
        controlType.control == 'Acceleration Risk Amount and Risk Amount' ||
        controlType.control == 'Missing Values Check_2' ||
        controlType.control ==
          'Product file information should match study metadata' ||
        controlType.control == 'Incidence_Death XOR Incidence/Death'
      ) {
        this.updateMissingColumnControls(
          controlType,
          controls,
          controlType.control
        );
      } else if (controlType.control == 'Missing Values Check Blocking') {
        this.updateMissingColumnControls(
          controlType,
          controls,
          'Missing Values Check Blocking'
        );
      }else if(controlType.control == 'Variables consistency'){
        this.updateVariable(controlType,controls)
      }
       else {
        let affectedColumns: Array<any> = controlType.affectedColumns || [];
        affectedColumns.forEach(controlByName => {
          let control: Control = controls.find(
            c => c.identifier == controlByName.name
          );
          if (control) {
            control.errors = controlByName.errorsNumber || 0;
            control.examples = controlByName.examples || [];

            control.total = control.errors;
            control.status = 'done';
            control.done = true;
            control.valid = control.errors > 0 ? 'no' : 'yes';
            control.running = false;

            control.message =
              control.errors > 0
                ? new BignumberPipe().transform(String(control.errors)) +
                  ' error(s)'
                : 'Done';
          }
        });
      }
    });

    controls.forEach(control => {
      if (!control.done && !control.valid) {
        control.errors = 0;
        control.examples = [];

        control.total = control.errors;
        control.status = 'done';
        control.done = true;
        control.valid = control.errors > 0 ? 'no' : 'yes';
        control.running = false;

        control.message =
          control.errors > 0
            ? new BignumberPipe().transform(String(control.errors)) +
              ' error(s)'
            : 'Done';
      }
    });
  };
  getColumns = controls => {
    // console.log("------------------ssss",controls)
    let ret = [];
    controls.forEach(c => {
      c.affectedColumns.forEach(col => {
        col['type'] = c.type;
        ret.push(col);
      });
    });
    return ret;
  };
  canDoControl(files) {
    return this.ds.canDoControl(files);
  }
  updateVariable(res, controls){
    let control = controls.find(el => el.order == 51)
    let errors = 0;
    control.status = 'done';
    control.done = true;
    control.running = false;
    res.affectedColumns.forEach(affectedColumn => {
      errors = errors + affectedColumn.errorsNumber;
    });
    control.errors = errors;
    control.valid = control.errors > 0 ? 'no' : 'yes';
    control.message =
      control.errors > 0
        ? new BignumberPipe().transform(String(control.errors)) + ' error(s)'
        : 'Done';
    control.examples = res.affectedColumns
  }
  updateFailed = (controls, serverError?: boolean) => {
    controls.forEach(control => {
      control.total = null;
      switch (control.status) {
        case 'done':
          break;
        default:
          control.running = false;
          if (serverError) control.message = 'Server Error';
          else control.message = 'Not Executed';
          control.valid = 'no';
          break;
      }
    });
  };

  upateControls(controls, res) {
    controls.forEach(control => {
      let affaected = control.affectedColumns;

      if (affaected.length > 0) {
        control.errors = 0;
        affaected.forEach(element => {
          control.errors += element['errorsNumber'];
        });
      } else {
        control.errors = 0;
      }

      control.errorsDetailed = control.affectedColumns;
      control['total'] = control.errors;
      control['status'] = 'done';
      control['done'] = true;
      control['valid'] = control.errors > 0 ? 'no' : 'yes';
      control['running'] = false;

      control.message =
        control.errors > 0
          ? new BignumberPipe().transform(control.errors) + ' error(s)'
          : 'Done';
    });
  }

  runNewJob(dataset, files) {
    this.cs.startNewJob(files, dataset);
  }
  
  viewExistingReport = () => {
    this.notExecutedJSON = this.dataset.notExecReport;

    this.techReport = this.dataset.techReport;
    this.funcReport = this.dataset.funcReport;
    this.policyHeader =
      this.dataset.techReport[0] && this.dataset.techReport[0].header
        ? this.dataset.techReport[0].header.split(';')
        : null;
    this.productHeader =
      this.dataset.techReport[1] && this.dataset.techReport[1].header
        ? this.dataset.techReport[1].header.split(';')
        : null;

    this.updateNotExecutedControl(
      this.dataset.notExecReport,
      this.funcControls,
      this.notExecuted
    );
    this.updateFormat(
      this.dataset.techReport[0].controlResultsList,
      this.formatControl,
      this.controls
    );
    this.updateFormat(
      this.dataset.techReport[1].controlResultsList,
      this.productFormatControl,
      this.productControls
    );
    this.update(this.dataset.funcReport.controlResultsList, this.funcControls);
    this.funcControls = this.ls.sortControls(this.funcControls);
    return;
  };
  executecontrols(policypath,productpath,mode){
    this.cs.doTechControls(policypath, productpath, mode,this.study,this.user).subscribe({
      next: resArray => {
        // console.log(resArray);
        if (this.productFormatControl.canceled == false) {
          let res = resArray[0];
          let productRes = resArray[1];
          this.techReport = resArray;
          this.productHeader = productRes.header
            ? productRes.header.split(';')
            : null;
          this.policyHeader = res.header ? res.header.split(';') : null;
          this.updateFormat(
            res.controlResultsList,
            this.formatControl,
            this.controls
          );
          this.updateFormat(
            productRes.controlResultsList,
            this.productFormatControl,
            this.productControls
          );
        }
      },
      error: err => {
        if (this.productFormatControl.canceled == false) {
        this.updateFailed(this.controls, true);
        this.updateFailed(this.formatControl, true);
        this.updateFailed(this.productFormatControl, true);
        this.updateFailed(this.funcControls, true);
      }},
      complete: () =>
        this.cs
          .notExecutedControls(
            policypath,
            productpath,
            mode,
            this.study.startObsDate,
            this.study.endObsDate,
            this.study,
            this.user
          )
          .subscribe({
            next: res => {
              if (this.productFormatControl.canceled == false) {
                this.notExecutedJSON = res;
                this.updateNotExecutedControl(
                  res,
                  this.funcControls,
                  this.notExecuted
                );
              }
            },
            error: err => {
              if (this.productFormatControl.canceled == false) {
              this.updateFailed(this.controls, true);
              this.updateFailed(this.formatControl, true);
              this.updateFailed(this.productFormatControl, true);
              this.updateFailed(this.funcControls, true);
            }},
            complete: () =>
              this.cs
                .funcControls(
                  policypath,
                  productpath,
                  mode,
                  this.study.startObsDate,
                  this.study.endObsDate,
                  this.study.id,
                  this.user
                )
                .subscribe(
                  res => {
                    if (this.productFormatControl.canceled == false) {
                      this.funcReport = res;
                      this.update(res.controlResultsList, this.funcControls);
                      this.funcControls = this.ls.sortControls(
                        this.funcControls
                      );
                      this.done.emit(true);
                    }
                  },
                  err => {
                    if (this.productFormatControl.canceled == false) {
                    this.updateFailed(this.funcControls, true);}
                  },
                  ()=>{this.controlsDone = true}
                )
          })
    });
  }

  
  runNewReport = () => {
    let productfile = this.dataset.files.find(f => f.type == 'product');
    let policyfile
    let productpath = this.dataset.files.find(f => f.type == 'product').path;
    let mode
    let policypath
    switch(this.dataset.mode){
      case 0:
        mode = 'split';
        break;
      case 1:
        mode = 'combine';
        break;
      case 2:
        mode = 'snapshot';
        break;
      default:
        break;
    }
    if(this.dataset.mode == 2 ){
      let maxyears = []
      let minyears = []
      productpath = this.dataset.temporaryFile.find(f => f.type == 'product').path
      this.dataset.temporaryFile.forEach(file => {
        if(file.type != 'product'){
        maxyears.push(file.maxyear[0])
        minyears.push(file.minyear[0])}
      });
    this.cs.doSnapControls(this.dataset,this.dataset.header,maxyears,minyears).subscribe({
      next: resArray => {
        this.dataset.files.find(policy => policy.type == 'policy').path = resArray["path"]
        this.dataset.files.find(policy => policy.type == 'policy').name = resArray["path"].split("\\")[-1]
        let productHolder = this.dataset.files.find(product => policy.type == 'product') 
        productHolder = this.dataset.temporaryFile.find(f => f.type == 'product')
        this.snapshotControl.forEach(el => {el.status = 'done';
        el.done = true;
        el.valid = resArray[el.identifier] == 'true' ? 'yes' : 'no';
        el.affectedColumns = [resArray[el.identifier]]
        if(resArray[el.identifier] != 'true'){
          el.errors = 1
        }
        el.running = false;
        el.message = el.valid == 'yes' ? 'done': 'failed' })
        policypath = resArray["path"]
        let policy = this.dataset.files.find(el => el.type =='policy')
        policy.path = resArray["path"]
        policy.name = Date.now().toString()
        
      },
      error: res => {
        this.snapshotControl.forEach(el => {el.status = 'done';
        el.valid = 'no'
        el.done = false;
        el.running = false;
        el.message = 'Server Error';
         }
        )
      },
      complete:() =>{
        if(policypath){
        this.executecontrols(policypath,productpath,mode)}
        else {
          this.funcControls.forEach(control => {
            control.valid = 'no'
            control.done = false;
            control.running = false;
            control.message = 'Not Executed'
          })
          this.productControls.forEach(control => {
            control.valid = 'no'
            control.done = false;
            control.running = false;
            control.message = 'Not Executed'
          })
          this.productFormatControl.valid = 'no'
          this.productFormatControl.done = false;
          this.productFormatControl.running = false;
          this.productFormatControl.message = 'Not Executed'
          this.formatControl.valid = 'no'
          this.formatControl.done = false;
          this.formatControl.running = false;
          this.formatControl.message = 'Not Executed'
          
        }
      }

  
  })}
  else {
    policyfile = this.dataset.files.find(f => f.type == 'policy');
    productpath = productfile['path'];
    policypath = policyfile['path'];
    switch(this.dataset.mode){
      case 0:
        mode = 'split';
        break;
      case 1:
        mode = 'combine';
        break;
      case 2:
        mode = 'snapshot';
        break;
      default:
        break;
    }
    this.executecontrols(policypath,productpath,mode)

  }
    // this.executecontrols(productfile,policypath,productpath,mode)
    // this.cs.doTechControls(policypath, productpath, mode,this.study,this.user).subscribe({
    //   next: resArray => {
    //     // console.log(resArray);
    //     if (this.productFormatControl.canceled == false) {
    //       let res = resArray[0];
    //       let productRes = resArray[1];
    //       this.techReport = resArray;
    //       this.productHeader = productRes.header
    //         ? productRes.header.split(';')
    //         : null;
    //       this.policyHeader = res.header ? res.header.split(';') : null;
    //       this.updateFormat(
    //         res.controlResultsList,
    //         this.formatControl,
    //         this.controls
    //       );
    //       this.updateFormat(
    //         productRes.controlResultsList,
    //         this.productFormatControl,
    //         this.productControls
    //       );
    //     }
    //   },
    //   error: err => {
    //     if (this.productFormatControl.canceled == false) {
    //     this.updateFailed(this.controls, true);
    //     this.updateFailed(this.formatControl, true);
    //     this.updateFailed(this.productFormatControl, true);
    //     this.updateFailed(this.funcControls, true);
    //   }},
    //   complete: () =>
    //     this.cs
    //       .notExecutedControls(
    //         policypath,
    //         productpath,
    //         mode,
    //         this.study.startObsDate,
    //         this.study.endObsDate,
    //         this.study,
    //         this.user
    //       )
    //       .subscribe({
    //         next: res => {
    //           if (this.productFormatControl.canceled == false) {
    //             this.notExecutedJSON = res;
    //             this.updateNotExecutedControl(
    //               res,
    //               this.funcControls,
    //               this.notExecuted
    //             );
    //           }
    //         },
    //         error: err => {
    //           if (this.productFormatControl.canceled == false) {
    //           this.updateFailed(this.controls, true);
    //           this.updateFailed(this.formatControl, true);
    //           this.updateFailed(this.productFormatControl, true);
    //           this.updateFailed(this.funcControls, true);
    //         }},
    //         complete: () =>
    //           this.cs
    //             .funcControls(
    //               policypath,
    //               productpath,
    //               mode,
    //               this.study.startObsDate,
    //               this.study.endObsDate,
    //               this.study.id,
    //               this.user
    //             )
    //             .subscribe(
    //               res => {
    //                 if (this.productFormatControl.canceled == false) {
    //                   this.funcReport = res;
    //                   this.update(res.controlResultsList, this.funcControls);
    //                   this.funcControls = this.ls.sortControls(
    //                     this.funcControls
    //                   );
    //                   this.done.emit(true);
    //                 }
    //               },
    //               err => {
    //                 if (this.productFormatControl.canceled == false) {
    //                 this.updateFailed(this.funcControls, true);}
    //               },
    //               ()=>{this.controlsDone = true}
    //             )
    //       })
    // });
  };
}
