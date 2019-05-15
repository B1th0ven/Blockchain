import { Injectable } from '@angular/core';
import { Study } from '../../shared/models/study';
import { Dataset } from '../../shared/models/dataset';
import { Run } from '../../shared/models/run';

@Injectable()
export class StoreService {

  readonly STORE_NAME = "STUDY_STORE_DATA"
  studies:studyTab[] = []

  constructor() {
    this.studies = JSON.parse(localStorage.getItem(this.STORE_NAME)) || []
  }

  setStudyTabs(tabs)
  {
    this.studies = tabs;
    this.saveStore()
  }

  setStudyDatasets(studyId,datasets)
  {
    //this.studies.find(s=> s.ststId == studyId ||  s.id == studyId );
    this.saveStore()
  }

  getStudyTabs()
  {
    return [];
    //return this.studies;
  }

  saveStore()
  {
    //console.log("tabssssssss",this.studies)
    //localStorage.setItem(this.STORE_NAME,JSON.stringify(this.studies))
  }
}

interface studyTab{
  type:string,
  name:string,
  study?:Study,
  datasetsTabs?:any[]
  datasets?:Dataset[]
  runs?:Run[]
}
