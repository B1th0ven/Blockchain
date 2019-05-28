import {
  Component,
  OnInit,
  Input,
  EventEmitter,
  Output,
  OnDestroy,
  ViewChildren,
  QueryList
} from '@angular/core';
import {
  Study
} from '../../shared/models/study';
import {
  Dataset
} from '../../shared/models/dataset';
import {
  DatasetService
} from '../../services/dataset.service';
import {
  isArray
} from 'util';
import { StudyDatasetComponent } from '../study-dataset/study-dataset.component';

@Component({
  selector: 'app-dataset-management',
  templateUrl: './dataset-management.component.html',
  styleUrls: ['./dataset-management.component.scss']
})
export class DatasetManagementComponent implements OnInit, OnDestroy {

  @Input() study: Study

  @Output() continueControl: EventEmitter < any > = new EventEmitter < any > ()
  @Output() changeDatasetName: EventEmitter < any > = new EventEmitter < any > ()
  @Output() datasetDelete: EventEmitter < any > = new EventEmitter < any > ()

  @ViewChildren(StudyDatasetComponent) studyDatasetComponents : QueryList<StudyDatasetComponent>

  tabs: Array < {} >
    activeTab: number

  busy: boolean = false;
  err: string;

  datasets: Array < Dataset >

    constructor(private ds: DatasetService) {}

  ngOnInit() {

    // let temp = JSON.parse(sessionStorage.getItem("datasets_of_" + this.study.id))
    // if (temp && Array.isArray(temp) && temp.length > 0) {
    //   this.datasets = temp.map(e => new Dataset(e))
    //   this.tabs = JSON.parse(sessionStorage.getItem("datasets_tabs_of_" + this.study.id))
    //   this.activeTab = JSON.parse(sessionStorage.getItem("datasets_tabs_active_of_" + this.study.id))

    //   sessionStorage.removeItem("datasets_of_")
    //   sessionStorage.removeItem("datasets_tabs_of_")
    //   sessionStorage.removeItem("datasets_tabs_active_of_")
    // }


    if (!this.datasets) {
      this.getDataSets()
    }

  }

  getDataSets() {
    this.datasets = new Array < Dataset > ()
      this.tabs = new Array < {} > ()
      this.activeTab = 0

      this.busy = true;
      this.ds.getByStudyId(this.study).subscribe(
        data => {
          this.busy = false;
          if (isArray(data)) {
            data.forEach(element => {
              let d = new Dataset()
              this.datasets.push(d.mapFromApi(element))
              this.tabs.push({
                type: "newdataset",
                name: d.name
              })

            });

            if (this.datasets.length <= 0) {
              this.tabs.push({
                type: "newdataset",
                name: "Create a dataset"
              })
              this.datasets.push(new Dataset())
            }

            this.tabs = this.tabs.splice(0, 5)
            this.datasets = this.datasets.slice(0, 5)
          }
        }, err => {
          this.busy = false;
          this.err = "Error with the server."
        }
      )
      // this.datasets.forEach(dataset => {
      //   if(dataset.mode == 2) {
      //     this.ds.getSnapshotByDatasetId(dataset.id).subscribe(
      //       {
      //         next: res => {
      //           res.forEach(el => dataset.temporaryFile.push({
      //             "typename": 'Policy',
      //             "type":'policy',
      //             "name": el["fileName"],
      //             "columns": el["fileHeader"],
      //             "inconsistent":el["inconsistentColumns"],
      //             "year":el["reportingYear"]
      //           })) 
      //         }
      //       }
      //     )
      //   }
      // })
  }

  ngOnDestroy() {
    if (this.datasets.length > 0) {
      sessionStorage.setItem("datasets_of_" + this.study.id, JSON.stringify(this.datasets))
      sessionStorage.setItem("datasets_tabs_of_" + this.study.id, JSON.stringify(this.tabs))
      sessionStorage.setItem("datasets_tabs_active_of_" + this.study.id, JSON.stringify(this.activeTab))
    }
  }

  newTab(type, event ? ) {
    if (this.canCreateDataset()) {
      this.tabs.push({
        type: "newdataset",
        name: "Create a dataset"
      })
      this.datasets.push(new Dataset())

      this.activeTab = this.tabs.length - 1
    }
  }

  openTab(ind) {
    this.activeTab = ind
  }

  closeTab(tabInx, event) {
    this.tabs.splice(tabInx, 1)
  }

 deleteTab(index,event){   
    if(this.datasets.length == 1)
    {
      this.newTab("new",event)
    }
    this.activeTab = Math.max(0, index - 1)
    this.datasetDelete.emit(this.datasets[index])
    this.tabs.splice(index, 1)
    this.datasets.splice(index, 1)
  }

  remove(ind, e: Event) {
    e.preventDefault()
    if (this.activeTab >= ind)
      this.activeTab = Math.max(0, this.activeTab - 1)

    this.tabs.splice(ind, 1)
    this.datasets.splice(ind, 1)
  }

  canCreateDataset = () => this.tabs.length < 5 && !this.study.isValidated();

  updateTab(dataset: Dataset, i) {
    let tab = this.tabs[i]
    tab["name"] = dataset.name

    this.onChangeDatasetName({dataset:dataset})
  }

  onContinueControl = (event) => this.continueControl.emit(event)
  onChangeDatasetName = (event) => this.changeDatasetName.emit(event)
}
