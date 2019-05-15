import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class IbnrControlService {

  constructor(private http : Http) { }

  compulsoryCheck(path,type)
  {
    let body=
    {
      path:path,
      type:type
    }
    return this.http.post(environment.api_endpint+"ibnr/compulsory",body)
      .map(res=>res.json())
      .catch((res) => Observable.throw(res.json()))
  }

  technicalCheck(path,type)
  {
    let body=
    {
      path:path,
      type:type
    }
    return this.http.post(environment.api_endpint+"ibnr/technical",body)
      .map(res=>res.json())
      .catch((res) => Observable.throw(res.json()))
  }

  functionalCheck(path,type,runId,runDimensions: any[] ,runStudyId)
  {
    let dimension : Array<any> = new Array();
    runDimensions.forEach(element => {
      dimension.push(element.itemName)
    });
    let body=
    {
      path:path,
      type:type,
      runId : runId,
      runDimensions : dimension,
      runStudyId : runStudyId
    }
    return this.http.post(environment.api_endpint+"ibnr/functional",body)
      .map(res=>res.json())
      .catch((res) => Observable.throw(res.json()))
  }

  amountAllocationCheck(pathAmount, pathAllocation) {
    let body=
    {
      pathAmount:pathAmount,
      pathAllocation:pathAllocation
    }
    return this.http.post(environment.api_endpint+"ibnr/amountAllocationControl",body)
      .map(res=>res.json())
      .catch((res) => Observable.throw(res.json()))
  }

}
