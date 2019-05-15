import {
  Injectable
} from '@angular/core';
import {
  environment
} from '../../environments/environment';
import {
  Observable
} from 'rxjs/Observable';
import {
  Http,
  Response,
  Headers,
  RequestOptions
} from '@angular/http';
import {
  Study
} from '../shared/models/study';
import {
  HttpRequest,
  HttpClient
} from '@angular/common/http';
import { Subject, BehaviorSubject , pipe} from 'rxjs';
import {catchError, map, debounceTime, switchMap} from 'rxjs/operators';
import {of} from 'rxjs/observable/of';

@Injectable()
export class StudyService {


  lobs=null
  params=null

  constructor(private http: Http, private httpClient: HttpClient) {
  }

  public getLobs() {
    if (this.lobs) return new BehaviorSubject(this.lobs)
    return this.http.get(environment.api_endpint + "business")
      .map(res => res.json())
      .do( res=> {this.lobs=res})
      .catch((response: any) => Observable.throw(response))
  }

  public getParams() {
    if (this.params) return new BehaviorSubject(this.params)

    return Observable.forkJoin(
      this.http.get(environment.api_endpint + "clienttypes").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint + "engines").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint + "groups").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      //this.http.get(environment.api_endpint+"clients").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      //this.http.get(environment.api_endpint+"treaties").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint + "business").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint + "datasources").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint + "countries").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      this.http.get(environment.api_endpint+"clients").map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
      
    )
    .do(res=>this.params=res)
    .catch((response: any) => Observable.throw(response.json()))
  }

  public getPageOfClients(page : Number , size : Number, searchCriteria?: String) {
    if(searchCriteria) {
      return this.http.get(environment.api_endpint + "clients/?page=" + page + "&size=" + size+"&searchCriteria="+searchCriteria ).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
    } 
    return this.http.get(environment.api_endpint + "clients/?page=" + page + "&size=" + size ).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public getClienById(id : Number ) {
      return this.http.get(environment.api_endpint + "client/"+id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public openFile(name) {
    window.open(environment.api_endpint + 'download/attached/' + name, "_blank");
  }

  public getClientsByParentGroup(id: Number) {
    return this.http.get(environment.api_endpint + "clients/groups/" + id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public getAllClients() {
    return this.http.get(environment.api_endpint + "clients").map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public getTreatieByClient(id: Number) {
    return this.http.get(environment.api_endpint + "treaties/clients/" + id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public postStudy(study: Study) {
    return this.http.post(environment.api_endpint + "studies", Study.mapApiStudy(study)).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  public getStudies() {
    return this.http.get(environment.api_endpint + "studies").map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  uploadAttachedFile(file) {
    const req = new HttpRequest('POST', environment.api_endpint + 'uploadattached', file, {
      reportProgress: true,
    });
    return this.httpClient.request(req);
  }

  deleteFile(path,id) {
    let body = {
      path : path,
      id : id
    }
    return this.http.post(environment.api_endpint+"study/file",body).catch(err=> Observable.throw(err))
  }

  getPage(page: number, size: number, q: object, sort: string, desc: boolean) {

    return this.http.get(environment.api_endpint + "studies?limit=" + size + "&page=" + page + this.queryBuilder(q, sort, desc)).map((res) => res.json())
  }
  queryBuilder(q: object,sort:string,desc:boolean) {
    let res = "";
    for (let key in q) {
      if (q[key] != "" && q[key] != null) {
        if (key == "statusdate") {
          res += "&" + key + "=" + q[key].split("/").reverse().join("-")
        } else res += "&" + key + "=" + q[key];
      }
    }
    if ( sort && sort != ""){
      res +="&sort="+sort+"&desc="+desc
    }

    return res;
  }

  deleteStudy(id,bool){
    return this.http.delete(environment.api_endpint +"studies/"+id+"?rminput="+bool).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  changeStudyStatus(id,status,ruId){
    return this.http.patch(environment.api_endpint +"studies/changeStatus/"+id,{"status":status,"ruId":ruId}).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  searchTreaties(term: string): Observable<any[]> {
    // return this.http.get(environment.api_endpint+"treaties?key=" + term).pipe(
    //     catchError(() => of(({items: []}))),
    //     map(rsp => rsp.items),
    // );
    return this.http.get(environment.api_endpint+"treaty?key=" + term).map(res => res.json())
  }


}
