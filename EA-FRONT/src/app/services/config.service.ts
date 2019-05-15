import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { HttpClient } from "@angular/common/http";
import { environment} from '../../environments/environment';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ConfigService {
    constructor(private http: Http, private httpClient: HttpClient) { }

    public getVersion() {
        return this.http.get(environment.api_endpint + "version").map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
    }
}