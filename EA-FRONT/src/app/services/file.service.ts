import {
  Injectable
} from '@angular/core';
import {
  environment
} from '../../environments/environment';
import {
  Http,
  Response,
  Headers,
  RequestOptions
} from '@angular/http';
import {
  HttpClient,
  HttpRequest
} from '@angular/common/http';
import 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

interface Change {
  type:string,
  description:string,
  id:string,
  component:string,
  func:string
}

class Log{
  date:string
  version:string
  changes:Array<Change>
}

@Injectable()
export class FileService {

  files

  constructor(private http: Http, private httpClient: HttpClient) {}

  getFiles(id,login) {
    return new Promise((resolve, reject) => {
      this.http.get(environment.api_endpint + "explorer/"+id+"/"+login)
        .timeout(5000)
        .map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
        .toPromise()
        .then(res => {
          resolve(res)
        })
        .catch(err => {
          reject(err)
        })
    })
  }

  getFileInfo(path) {
    return new Promise((resolve, reject) => {
      this.http.get(environment.api_endpint + `details?path=${path}`).toPromise()
        .then(res => {
          resolve(res.json())
        })
        .catch(err => {
          reject(err.json())
        })
    })
  }

  validateFile(path, type, sheet) {
    let body = {
      path: path,
      type: type
    }
    return new Promise((resolve, reject) => {
      this.http.post(environment.api_endpint + "CompColsCheck", body).toPromise()
        .then(res => {
          resolve(res.json())
        })
        .catch(err => {
          reject(err.json())
        })
    })
  }

  uploadFile(file,id) {
    const req = new HttpRequest('POST', environment.api_endpint + "upload/"+id, file, {
      reportProgress: true,
    });
    return this.httpClient.request(req);
  }

  upload(file,path) {
    const req = new HttpRequest('POST', environment.api_endpint + path, file, {
      reportProgress: true,
    });
    return this.httpClient.request(req);
  }

  delete(path)
  {
    return this.http.delete(environment.api_endpint+"file"+"?path="+path).catch(err=> Observable.throw(err))
  }

  copyFileToStudy(stdid,path){
   
    return this.httpClient.post(environment.api_endpint+"torrent/copy/"+stdid,path);
  }
  findDatasetByFileId(path) {
    return this.httpClient.post(environment.api_endpint+"datasets/file/",path);
  }
  public openFileIbnr(name) {
    window.open(environment.api_endpint + 'download/attached/' + name+"?type=ibnr", "_blank");
  }
  public openFileNda(name) {
    window.open(environment.api_endpint + 'download/nda/' + name);
  }

  public openFilePivots(name) {
    window.open(environment.api_endpint + 'download/pivots/' + name);
  }

  public openFileDocs(name) {
    window.open(environment.api_endpint + 'download/docs/' + name);
  }

  isFileOnServerIbnr(name){
    return this.http.get(environment.api_endpint + 'checkfilepath/' + name+"/?type=ibnr")
  }

  getHistory():Observable<Array<Log>>{
    return this.http.get(environment.api_endpint + 'history').map(result => result.json()).catch(err=> Observable.throw(err))
  }
    
}
