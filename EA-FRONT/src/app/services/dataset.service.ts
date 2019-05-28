import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { environment } from '../../environments/environment';
import { Dataset } from '../shared/models/dataset';
import { Study } from '../shared/models/study';
import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DatasetService {

  dimensons:BehaviorSubject<string[]>= new BehaviorSubject<string[]>(null)

  constructor(private http: Http) {
  }

  save(dataset:Dataset)
  {
    return this.http.post(environment.api_endpint+"datasets",Dataset.mapToApi(dataset)).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  saveFiles(dataset:Dataset)
  {
    console.log("ds------------>",dataset)
    return this.http.post(environment.api_endpint+"datasets/report",Dataset.mapFiles(dataset)).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
  saveSnapshotFiles(res)
  {
    return this.http.post(environment.api_endpint+"datasets/snapshot",Dataset.SaveFiles(res)).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
  getByStudyId(study:Study)
  {
    return this.http.get(environment.api_endpint+"datasets/study/"+study.id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
  getSnapshotByDatasetId(id)
  {
    return this.http.get(environment.api_endpint+"datasets/snapshot/"+id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
  getPivots(type : string)
  {
    return this.http.get(environment.api_endpint+"datasets/pivots/"+type).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  getRunValidation(study: Study): any {
    return this.http.get(environment.api_endpint+"datasets/run/study/"+study.id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  getDimensions()
  {
    return this.http.get(environment.api_endpint+"dimensions").map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  canDoControl(files){
    if (!files) return false;
    for (let i = 0; i < files.length; i++) {
      if (!files[i]) return false
      if( files[i].status != "ok" ){
        return false
      }

    }
    return true
  }

  canDeleteDs(id){
    return this.http.get(environment.api_endpint+"run/datasets/candelete/"+id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  deleteDs(id){
    return this.http.delete(environment.api_endpint+"datasets/delete/"+id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  runsAssociatedToDataset(id) {
    return this.http.get(environment.api_endpint+"run/dataset?id="+id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }
}
