import { Component, ViewChild, OnInit, Input, Output, EventEmitter  } from '@angular/core';
import { Study } from '../../shared/models/study';
import { Dataset } from '../../shared/models/dataset';
import { DatasetService } from '../../services/dataset.service';
import { DatasetControlComponent } from '../dataset-control/dataset-control.component';
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';
import {
  ToasterService
} from 'angular5-toaster/dist/angular5-toaster';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { StudyDatasetCreateComponent } from '../study-dataset-create/study-dataset-create.component';

@Component({
  selector: 'app-study-dataset',
  templateUrl: './study-dataset.component.html',
  styleUrls: ['./study-dataset.component.scss'],
})
export class StudyDatasetComponent implements OnInit {

  @Input() study:Study
  @Input() dataset:Dataset
  @Input() datasets:Array<Dataset>
  @Input() tabNbr:number=0

  @Output() save:EventEmitter<any>= new EventEmitter<any>()

  @Output() continueControl:EventEmitter<any>= new EventEmitter<any>()
  @Output() changeDatasetName:EventEmitter<any>= new EventEmitter<any>()
  @Output() delete:EventEmitter<any>=new EventEmitter<any>()

  @ViewChild(DatasetControlComponent) controlsTab : DatasetControlComponent
  @ViewChild(StudyDatasetCreateComponent) studyDatasetCreateComponent : StudyDatasetCreateComponent
  activeTab:Number = 1
  doneControls : boolean = false;
  isDatasetAssociatedToRun : boolean = false


  constructor(public ds:DatasetService,private modalService: NgbModal, private tsr: ToasterService,private permissionsService: NgxPermissionsService) { }

  ngOnInit() {
    if (!this.dataset)
      this.dataset = new Dataset()
    else
      this.checkRunAssociatedToDataset()
  }

  checkRunAssociatedToDataset() {
    if(this.dataset.id) {
      this.ds.runsAssociatedToDataset(this.dataset.id).subscribe(res=> {
        if(res && Array.isArray(res) && res.length>0) {
          this.isDatasetAssociatedToRun = true
        } else {
          this.isDatasetAssociatedToRun = false
        }
      })
    }
    
  }

  updateDataset(dataset:Dataset)
  {
    this.dataset = dataset
    this.save.emit(dataset)
  }

  openControlTab(redo?)
  {
    console.log(" PRIVACY DATE DELETION" + this.dataset.files[0].privacyDataDeletion);
    console.log(this.dataset.files);
    
    if (this.ds.canDoControl(this.dataset.files))
    {
      this.activeTab = 2
      if ( redo || !this.controlsTab.initialized )
        this.controlsTab.startControls()
    }
  }

  onContinueControl(event)
  {
    this.continueControl.emit(event)
  }

  onChangeDatasetName(event)
  {
    this.changeDatasetName.emit(event)
  }

  openDeletePopUp(content) {
    this.modalService.open(content);
  }

  confirmD(c){
    console.log(this.dataset.id)
    this.ds.canDeleteDs(this.dataset.id).subscribe(
      res => 
      {
        if(res["canDelete"] == null)
        {
          this.tsr.pop("warning","Warning","This dataset is used for runs "+res["runsid"])

        }
        else
        {
          this.ds.deleteDs(this.dataset.id).subscribe(
            res => 
            {
              this.delete.emit(this.dataset);
              this.study.count = this.study.count - 1
              this.tsr.pop("success","Success","The dataset has been deleted")
    
            },
            err => {
              console.log(err)
              this.tsr.pop("error", "Error", "Server Error");
            }
          )
          
        }
      },
      err => {
        console.log(err)
        this.tsr.pop("error", "Error", "Server Error");
      }
    )
    c('Close click')
  }
  loadProgression(event){
    this.doneControls = event;
  }
  canEdit(){
    if(!this.permissionsService.getPermission('DATASET_CREATOR') && 
        !this.permissionsService.getPermission('DATASET_UPLOADER') &&
      !this.permissionsService.getPermission('DATASET_CONTROLS_EXECUTER') &&
    !this.permissionsService.getPermission('DATASET_EDITOR')) return false
    else return true
  }
  canReadControls() {
    if(this.permissionsService.getPermission('DATASET_READER')) {
      return false
    }
    return true
  }

}
