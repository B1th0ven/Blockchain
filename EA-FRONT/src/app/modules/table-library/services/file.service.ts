import {
  Injectable
} from '@angular/core';
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
import { environment } from '../../../../environments/environment';

@Injectable()
export class FileService {

  files

  constructor(private http: Http, private httpClient: HttpClient) {}

  uploadFile(file) {
    const req = new HttpRequest('POST', environment.api_endpint + "upload", file, {
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

  public openFile(name) {
    window.open(environment.api_endpint + 'download/attached/' + name+"?type=expected", "_blank");
  }

  isFileOnServer(name){
    return this.http.get(environment.api_endpint + 'checkfilepath/' + name+"/?type=expected")
  }
}
