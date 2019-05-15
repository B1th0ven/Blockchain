import { Injectable } from '@angular/core';
import { DatasetService } from './dataset.service';
import { ControlService } from './control.service';
import { BignumberPipe } from '../shared/pipes/bignumber.pipe';
import { Control } from '../shared/models/control';

// updateFormatInner return controls
// updateFormat return typeFormatControl
// updateFailed return controls
// updateNotExecutedControl return [funcControls, notExecuted]
// updateMissingColumnControls retunr control
// findContol return control
// updateControls return controls
// getColumns return array
// upateControls return controls
// getStatus return string
// update return controls
// viewExistingReport return [techReport, funcReport, policyHeader, productHeader, funcControls,formatControl,productFormatControl,notExecuted,notExecutedJSON]
// runNewReport  return [productHeader, policyHeader, funcControls, notExecuted, techReport, funcReport, notExecutedJSON, formatControl, controls,productFormatControl]
//progressPerc return Number
// valid return bool
// isNotExecuted bool

@Injectable()
export class DatasetControlLogicService {
  constructor(private ds: DatasetService, private cs: ControlService) {}

  map: Map<number, Array<String>> = new Map();
  productHeader;
  policyHeader;
  funcReport;
  techReport;

  updateFormat = (resControls, typeFormatControl, controls) => {
    this.updateFormatInner(resControls, controls);

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
    return typeFormatControl;
  };

  updateNotExecutedControl(res: any, funcControls: any, notExecuted: any): any {
    if (!res) return (notExecuted = []);

    res.forEach(notExecCtrName => {
      let control = funcControls.find(
        c =>
          c.identifier.toLocaleLowerCase() ==
          notExecCtrName.control.toLocaleLowerCase()
      );
      control.notExecuted = notExecCtrName;
      console.log('not Executed down bellow');
      console.log(notExecuted);
      notExecuted.push(notExecCtrName.control);
      control = this.updateFailed([control]);
    });
    return [funcControls, notExecuted];
  }
  updateFormatInner = (resControls, controls) => {
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
    return controls;
  };
  findControl(controls, id) {
    let control: Control = controls.find(c => c.identifier == id);
    return control;
  }
  UpdateControls(control, controls) {
    let resControl = controls.find(r => r.control == control.identifier);
    controls[controls.indexOf(resControl)] = control;
    return controls;
  }
  updateMissingColumnControls = (controlType, control) => {
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
    return control;
    // console.log(control);
  };
  update = (resControls: Array<any>, controls) => {
    resControls.forEach(controlType => {
      if (
        controlType.control == 'Missing Values Check' ||
        controlType.control == 'Missing Values Check_2' ||
        controlType.control ==
          'Product file information should match study metadata' ||
        controlType.control == 'Incidence_Death XOR Incidence/Death'
      ) {
        let control = this.findControl(controls, controlType.control);
        let updatedcontrol = this.updateMissingColumnControls(
          controlType,
          control
        );
        controls = this.UpdateControls(updatedcontrol, controls);
      } else if (controlType.control == 'Missing Values Check Blocking') {
        let control = this.findControl(
          controls,
          'Missing Values Check Blocking'
        );
        let updatedcontrol = this.updateMissingColumnControls(
          controlType,
          control
        );
        controls = this.UpdateControls(updatedcontrol, controls);
      } else {
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
    return controls;
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
    return controls;
  };
  updateCancel(controls){
    controls.forEach(control => {
      control.running = false;
      control.message = 'Not Executed';
      control.valid = 'no';
      control.canceled = true;
      control.errors = 0;
      control.notExecuted = false;
    }
     )
  }
  upateControls(controls) {
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
    return controls;
  }

  runNewJob(dataset, files) {
    this.cs.startNewJob(files, dataset);
  }

  viewExistingReport = (
    dataset,
    policyHeader,
    productHeader,
    funcControls,
    notExecuted,
    formatControl,
    controls,
    productFormatControl,
    productControls,
    notExecutedJSON,
    techReport,
    funcReport
  ) => {
    notExecutedJSON = dataset.notExecReport;

    techReport = dataset.techReport;
    funcReport = dataset.funcReport;
    policyHeader =
      dataset.techReport[0] && dataset.techReport[0].header
        ? dataset.techReport[0].header.split(';')
        : null;
    productHeader =
      dataset.techReport[1] && dataset.techReport[1].header
        ? dataset.techReport[1].header.split(';')
        : null;
    [funcControls, notExecuted] = this.updateNotExecutedControl(
      dataset.notExecReport,
      funcControls,
      notExecuted
    );
    formatControl = this.updateFormat(
      dataset.techReport[0].controlResultsList,
      formatControl,
      controls
    );
    productFormatControl = this.updateFormat(
      dataset.techReport[1].controlResultsList,
      productFormatControl,
      productControls
    );
    funcControls = this.update(
      dataset.funcReport.controlResultsList,
      funcControls
    );
    funcControls = this.sortControls(funcControls);
    return [
      techReport,
      funcReport,
      policyHeader,
      productHeader,
      funcControls,
      formatControl,
      productFormatControl,
      notExecuted,
      notExecutedJSON
    ];
  };
  // public runNewReport = (dataset,study,productHeader, policyHeader, funcControls, notExecuted, techReport, notExecutedJSON, formatControl, controls, productFormatControl,productControls,funcReport) => {
  //   let productfile = dataset.files.find(f => f.type == "product");
  //   let policyfile = dataset.files.find(f => f.type == "policy");
  //   let productpath = productfile["path"];
  //   let policypath = policyfile["path"];
  //   let mode = (dataset.mode == 0) ? "split" : "combine";
  //   this.cs.notExecutedControls(policypath, productpath, mode,study.startObsDate,study.endObsDate).subscribe({ next: res => {
  //     notExecutedJSON = res;
  //     this.updateNotExecutedControl(res, funcControls, notExecuted);
  //   }, error: err => {
  //     controls = this.updateFailed(controls,true);
  //     [formatControl] = this.updateFailed([formatControl],true);
  //     [productFormatControl] = this.updateFailed([productFormatControl],true);
  //     funcControls = this.updateFailed(funcControls,true);
  //   }, complete: () => console.log(policyHeader)
  // });
  // this.cs.funcControls(policypath, productpath, mode,study.startObsDate,study.endObsDate,study.id).subscribe(res => {
  //   funcReport = res;
  //   funcControls = this.update(res.controlResultsList, funcControls);
  //   funcControls = this.sortControls(funcControls)
  // }, err => {
  //   funcControls = this.updateFailed(funcControls,true);
  // });
  // this.cs.doTechControls(policypath, productpath, mode).subscribe({ next:  resArray => {
  //   let res = resArray[0]
  //   let productRes = resArray[1]
  //   techReport = resArray;
  //   this.productHeader = (productRes.header) ? productRes.header.split(";") : null;
  //   this.policyHeader = (res.header) ? res.header.split(";") : null;
  //   formatControl = this.updateFormat(res.controlResultsList, formatControl, controls);
  //   productFormatControl = this.updateFormat(productRes.controlResultsList, productFormatControl, productControls);
  // }, error: err => {
  //   controls = this.updateFailed(controls,true);
  //   [formatControl] = this.updateFailed([formatControl],true);
  //   [productFormatControl] = this.updateFailed([productFormatControl],true);
  //   funcControls = this.updateFailed(funcControls,true);
  // }, complete: () => console.log(policyHeader)})
  // ;
  // }

  // runNewReport = (dataset,study,productHeader, policyHeader, funcControls, notExecuted, techReport, notExecutedJSON, formatControl, controls, productFormatControl,productControls,funcReport) => {

  //   let productfile = dataset.files.find(f => f.type == "product");
  //   let policyfile = dataset.files.find(f => f.type == "policy");
  //   let productpath = productfile["path"];
  //   let policypath = policyfile["path"];
  //   let mode = (dataset.mode == 0) ? "split" : "combine";
  //   this.cs.doTechControls(policypath, productpath, mode).subscribe(resArray => {
  //     // console.log(resArray);

  //     let res = resArray[0]
  //     let productRes = resArray[1]
  //     techReport = resArray;
  //     productHeader = (productRes.header) ? productRes.header.split(";") : null;
  //     policyHeader = (res.header) ? res.header.split(";") : null;
  //     formatControl = this.updateFormat(res.controlResultsList, formatControl, controls);
  //     productFormatControl = this.updateFormat(productRes.controlResultsList, productFormatControl, productControls);

  //     if (true) {
  //       console.log(policyHeader)
  //       this.cs.funcControls(policypath, productpath, mode,study.startObsDate,study.endObsDate,study.id).subscribe(res => {
  //         funcReport = res;
  //         funcControls = this.update(res.controlResultsList, funcControls);
  //         funcControls = this.sortControls(funcControls)
  //       console.log(policyHeader)

  //       }, err => {
  //       console.log(policyHeader)
  //       this.updateFailed(funcControls,true);
  //       });
  //       console.log(policyHeader)

  //     }
  //     console.log(policyHeader)

  //   }
  //     , err => {
  //       console.log(policyHeader)
  //       controls = this.updateFailed(controls,true);
  //       formatControl = this.updateFailed(formatControl,true);
  //       productFormatControl = this.updateFailed(productFormatControl,true);
  //       funcControls = this.updateFailed(funcControls,true);
  //     });
  //     console.log(policyHeader)

  //   this.cs.notExecutedControls(policypath, productpath, mode,study.startObsDate,study.endObsDate).subscribe(res => {
  //     notExecutedJSON = res;
  //     [funcControls, notExecuted] =  this.updateNotExecutedControl(res,funcControls,notExecuted);
  //   }, err => {
  //     controls = this.updateFailed(controls,true);
  //     formatControl = this.updateFailed(formatControl,true);
  //     productFormatControl = this.updateFailed(productFormatControl,true);
  //     funcControls = this.updateFailed(funcControls,true);
  //   });
  //   return [techReport, funcReport, policyHeader, productHeader, funcControls,formatControl,productFormatControl,notExecuted,notExecutedJSON,controls]
  // }

  progressPerc(
    fileType: string,
    formatControl,
    productFormatControl,
    funcControls
  ) {
    let ctrNbr = 0;
    let total = 0;

    if (fileType.toLocaleLowerCase() == 'policy') {
      if (
        formatControl.files[0] == fileType.toLowerCase() &&
        formatControl.done &&
        formatControl.valid
      )
        ctrNbr++;
      total++;
    } else if (fileType.toLocaleLowerCase() == 'product') {
      if (
        productFormatControl.files[0] == fileType.toLowerCase() &&
        productFormatControl.done &&
        productFormatControl.valid
      )
        ctrNbr++;
      total++;
    }

    funcControls.forEach(element => {
      if (element.files.findIndex(f => f == fileType.toLowerCase()) >= 0) {
        total++;
        if (element.done && element.valid) ctrNbr++;
      }
    });

    if (total == 0) return 100;

    return Math.ceil((ctrNbr / total) * 100);
  }

  getStatus(
    fileType: String,
    formatControl,
    funcControls,
    productFormatControl
  ): String {
    
    if (fileType.toLocaleLowerCase() == 'policy') {
      if (
        formatControl.files[0] == fileType.toLowerCase() &&
        formatControl.running
      )
        return 'Technical controls in progress';
      if (
        formatControl.files[0] == fileType.toLowerCase() &&
        !formatControl.running &&
        funcControls[0].running
      )
        return 'Functional controls in progress';
      
      else if(formatControl.canceled ){ return 'Canceled'} else return 'Finished';
    } else if (fileType.toLocaleLowerCase() == 'product') {
      if (
        productFormatControl.files[0] == fileType.toLowerCase() &&
        productFormatControl.running
      )
        return 'Technical controls in progress';
      if (
        productFormatControl.files[0] == fileType.toLowerCase() &&
        !productFormatControl.running &&
        funcControls[0].running
      )
        return 'Functional controls in progress';
      
      else if(productFormatControl.canceled ){ return 'Canceled'} else return 'Finished';
    }
  }

  valid(
    formatControl,
    productFormatControl,
    funcControls,
    notExecuted
  ): boolean {
    if (formatControl.valid != 'yes') return false;
    if (productFormatControl.valid != 'yes') return false;
    for (let control of funcControls) {
      if (
        !this.isNotExecuted(control, notExecuted) &&
        control.category &&
        control.category.toLowerCase() == 'Blocking'.toLowerCase() &&
        control.valid != 'yes'
      )
        return false;
    }
    return true;
  }

  isNotExecuted(c: Control, notExecuted): boolean {
    if (!notExecuted) return false;
    return (
      notExecuted.findIndex(
        nc => nc.toLowerCase() == c.identifier.toLowerCase()
      ) >= 0
    );
  }

  onScrolRight(index, funcControls, map) {
    let arr: Array<String> = map.get(index) ? map.get(index) : [];
    arr.push(funcControls[index].controlColumns[0]);
    map.set(index, arr);
    funcControls[index].controlColumns.splice(0, 1);
  }
  onScrolLeft(index, funcControls, map) {
    let number: number = map.get(index).length;
    funcControls[index].controlColumns.splice(
      0,
      0,
      String(map.get(index)[number - 1])
    );
    map.get(index).splice(number - 1, 1);
  }

  sortControls(funcControls: Array<Control>): Array<Control> {
    for (let index = 0; index < funcControls.length; index++) {
      funcControls[index].orderdisplayed = index;
    }
    funcControls.sort((control1, control2) => {
      let r =
        this.calculatePriority(control2) - this.calculatePriority(control1);
      if (r != 0) {
        return r;
      } else {
        return control1.orderdisplayed - control2.orderdisplayed;
      }
    });
    return funcControls;
  }
  getSecondHeader(identifier, name) {
    let names = name.split('/');
    if (identifier == 'Product file information should match study metadata') {
      return names[0];
    }
    if (names[1] == 'product_id') {
      return names[1];
    }
    return 'POLICY_ID';
  }
  calculatePriority(control: Control) {
    if (String(control.message).toLowerCase() == 'not executed') {
      return 1;
    }
    if (String(control.message).toLowerCase() == 'done') {
      return 0;
    }
    if (control.category.toLowerCase() == 'warning') {
      return 2;
    }
    if (control.category.toLowerCase() == 'blocking') {
      return 3;
    }
  }
  getColumnValue(col: string, header: String[], line, study) {
    if (
      col &&
      'Start_of_Observation_Period'.toLocaleLowerCase() ==
        col.toLocaleLowerCase()
    ) {
      return (
        study.startObsDate.day +
        '/' +
        study.startObsDate.month +
        '/' +
        study.startObsDate.year
      );
    }
    if (
      col &&
      'End_of_Observation_Period'.toLocaleLowerCase() == col.toLocaleLowerCase()
    ) {
      return (
        study.endObsDate.day +
        '/' +
        study.endObsDate.month +
        '/' +
        study.endObsDate.year
      );
    }

    if (!Array.isArray(header)) {
      return 'No Header';
    }
    let ind = header.findIndex(
      s => col && s.trim().toLowerCase() == col.trim().toLowerCase()
    );
    if (ind >= 0) {
      let values = line.split(';');
      return values[ind];
      //  if(values[ind] != '') return values[ind]
      //  else return 'N/A'
    } else {
      return 'N/A';
    }
  }
}
