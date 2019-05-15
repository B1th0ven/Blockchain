import {
  Injectable
} from '@angular/core';
import {
  Observable,
  Subject,
  BehaviorSubject
} from 'rxjs';
import {
  ExpectedTable
} from '../models/expected-table.model';
import {
  Http
} from '@angular/http';
import {
  environment
} from '../../../../environments/environment';

import {
  HttpClient,
  HttpParams,
  HttpRequest,
  HttpEvent
} from '@angular/common/http';
import { date } from '../../../shared/models/date';


@Injectable()
export class TablesService {

  countries = null

  constructor(private http: Http, private httpClient: HttpClient) {}

  getCountries() {
    if (this.countries) return new BehaviorSubject(this.countries)
    else return this.http.get(environment.api_endpint + "countries").map(res => res.json()).do(res => this.countries = res).catch((response: any) => Observable.throw(response.json()))
  }

  latestTable(table: ExpectedTable) {
    return this.http.post(environment.api_endpint + "table/latest", this.mapToApi(table)).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  saveTable(table: ExpectedTable) {
    return this.http.post(environment.api_endpint + "table", this.mapToApi(table)).map(res => res.json()).map(res => this.mapOneFromApi(res)).catch((response: any) => Observable.throw(response.json()))
  }

  getTable() {
    return this.http.get(environment.api_endpint + "table").map(res => res.json()).map(res => this.mapArrayFromApi(res)).catch((response: any) => Observable.throw(response.json()))
  }

  getOneTable(id) {
    return this.http.get(environment.api_endpint + "table" + "/" + id).map(res => res.json()).map(res => this.mapOneFromApi(res)).catch((response: any) => Observable.throw(response.json()))
  }

  deleteTable(id) {
    return this.http.delete(environment.api_endpint+"table/"+ id).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  checkTableAssociationToRun(id,type){
    console.log("checking table value service",id,type);

    return this.http.get(environment.api_endpint+"table/run/"+ id + "/"+ type ).map(res => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  private mapToApi(table: ExpectedTable) {
    let d : Date = new Date();
    let dddd : string = date.getCurrentDate(d)    
    let creationDate : date = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])

    let obj = {
      "retName": table.name,
      "retUrl": table.path,
      "retCode": table.code,
      //"country": table.country["rcId"] || table.country_id,
      "retRcId": (table.country) ? table.country["rcId"] : null,
      "retDecrement": table.decrement,
      "retVersion": table.version,
      "retOrigin": table.origin,
      "retSource": table.source,
      "retExposureMethod": table.exposure_method,
      "retType": table.type,
      "retApplicationYear": table.application_year,
      "retPublicationYear": table.publication_year,
      "retComment": table.comment,
      "retAgeMin": table.age_min,
      "retAgeMax": table.age_max,
      "retCalYearMin": table.cal_year_min,
      "retCalYearMax": table.cal_year_max,
      "retDurationMin": table.duration_min,
      "retDurationMax": table.duration_max,
      "retLatestVersion": table.latest_version,
      "retCompReport":  JSON.stringify(table.comp_report),
      "retTechReport":   JSON.stringify(table.tech_report),
      "retFuncReport":   JSON.stringify(table.func_report),
      "retCreationDate": date.formatDate(table.creation_date) || date.formatDate(creationDate),
      "retDimensions": table.dimensions,
      "retCreatedBy": table.creator,
      "retStatus": table.status,
      "retIsConfidential": table.isConfidential
    }

    if (table.id) obj["retId"] = table.id

    return obj;
  }

  private mapArrayFromApi(res: Array < any > ) {
    let tables = new Array < ExpectedTable > ()
    res.forEach(jsonTable => {
      tables.push(this.mapOneFromApi(jsonTable))
    });

    return tables
  }

  private mapOneFromApi(jsonTable: any) {
    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }

    let t = new ExpectedTable()

    t.id = jsonTable["retId"];
    t.name = jsonTable["retName"];
    t.code = jsonTable["retCode"];
    t.path = jsonTable["retUrl"];
    t.country = jsonTable["country"];
    t.country_id = jsonTable["retRcId"];
    t.decrement = jsonTable["retDecrement"];
    t.version = jsonTable["retVersion"];
    t.origin = jsonTable["retOrigin"];
    t.source = jsonTable["retSource"];
    t.exposure_method = jsonTable["retExposureMethod"];
    t.type = jsonTable["retType"];
    t.application_year = jsonTable["retApplicationYear"];
    t.publication_year = jsonTable["retPublicationYear"];
    t.comment = jsonTable["retComment"];
    t.age_min = jsonTable["retAgeMin"];
    t.age_max = jsonTable["retAgeMax"];
    t.cal_year_min = jsonTable["retCalYearMin"];
    t.cal_year_max = jsonTable["retCalYearMax"];
    t.duration_min = jsonTable["retDurationMin"];
    t.duration_max = jsonTable["retDurationMax"];
    t.latest_version = jsonTable["retLatestVersion"];
    t.tech_report = (jsonTable["retTechReport"])?JSON.parse(jsonTable["retTechReport"]):null;
    t.comp_report = (jsonTable["retCompReport"])?JSON.parse(jsonTable["retCompReport"]):null;
    t.func_report = (jsonTable["retFuncReport"])?JSON.parse(jsonTable["retFuncReport"]):null;
    t.creation_date = datef(jsonTable["retCreationDate"])
    t.dimensions = jsonTable["retDimensions"];
    t.creator = jsonTable["retCreatedBy"];
    t.status = jsonTable["retStatus"];
    t.isConfidential = jsonTable["retIsConfidential"];

    return t
  }

  getPage(page: number, size: number, q: object, sort: string, desc: boolean) {

    return this.http.get(environment.api_endpint + "table/?limit=" + size + "&page=" + page + this.queryBuilder(q, sort, desc)).map((res) => res.json())
      .map(res => {
        return {
          totalPages: res.totalPages,
          content: this.mapArrayFromApi(res.content)
        }
      }).catch((response: any) => Observable.throw(response.json()))
  }

  queryBuilder(q: object, sort: string, desc: boolean) {
    let res = "";
    for (let key in q) {
      if (q[key] != "" && q[key] != null) {
        res += "&" + key + "=" + q[key].replace('+','	%2B');
      }
    }
    if (sort && sort != "") {
      res += "&sort=" + sort + "&desc=" + desc
    }

    return res;
  }

  readonly EXPECTED_CONTROL = "ExpTableControls/"

  getCompControls(path, type) {
    return this.http.post(environment.api_endpint + this.EXPECTED_CONTROL + "Comp", this.getControlsBody(path, type)).map((res) => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  getTechControls(path, type) {
    return this.http.post(environment.api_endpint + this.EXPECTED_CONTROL + "Tech", this.getControlsBody(path, type)).map((res) => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  getFuncControls(path, type,maxValues,minValues) {
    let body = {
      type: type,
      path: (path) ? path : null,
      maxValues:maxValues,
      minValues:minValues
    }
    return this.http.post(environment.api_endpint + this.EXPECTED_CONTROL + "Func", body).map((res) => res.json()).map((res) => this.mapFuncControls(res)).catch((response: any) => Observable.throw(response.json()))
  }

  getCalcControls(path, type) {
    return this.http.post(environment.api_endpint + this.EXPECTED_CONTROL + "Calc", this.getControlsBody(path, type)).map((res) => res.json()).catch((response: any) => Observable.throw(response.json()))
  }

  private getControlsBody(path: string, type: string) {
    let body = {
      type: type,
      path: (path) ? path : null
    }

    console.log(body)
    return body;
  }


  mapFuncControls(report) {
    let controls = this.getInitialFuncControls()
    console.log("report ---------->",report);
    
    if (report && report.controlResultsList && report.controlResultsList[0] && report.controlResultsList[0].affectedColumns) {
      let unmappedControls: any[] = report.controlResultsList[0].affectedColumns

      controls.forEach(c => {
        let unmappedControl = unmappedControls.find(u => u.name == c.id)
            
        if (unmappedControl) {
          c.error_number = unmappedControl.errorsNumber
          c.examples = unmappedControl.examples
        }
      });

    }
    if (report && report.controlResultsList 
              && report.controlResultsList[1] 
              && report.controlResultsList[1].affectedColumns
              && report.controlResultsList[1].affectedColumns.length > 0 ) {
      let missingValueControl = controls.find(c => c.id == report.controlResultsList[1].control)
      // console.log(missingValueControl);
      // console.log(report.controlResultsList[1]);
      missingValueControl.examples=[]
      missingValueControl.error_number = 0
      report.controlResultsList[1].affectedColumns.forEach(missingValueReportCol => {
        let col = missingValueReportCol.name
        let lines = []
        for (let i = 0; i < 5 && i < missingValueReportCol.examples.length; i++) {
          lines.push(missingValueReportCol.examples[i]);
        }
        let example = {
          col : col,
          lines : lines
        }
        missingValueControl.examples.push(example)
        missingValueControl.error_number = missingValueControl.error_number + missingValueReportCol.errorsNumber
      });
      // console.log(controls);
      
    }

    //Combination Control Mapping
    let combinationCheckReport = report.controlResultsList[2]
    if (report && report.controlResultsList 
      && combinationCheckReport
      && combinationCheckReport.affectedColumns
      && combinationCheckReport.affectedColumns.length > 0 ) {
        let combinationCheckControl = controls.find(c => c.id == combinationCheckReport.control)
        combinationCheckControl.error_number = combinationCheckReport.affectedColumns[0].errorsNumber
        combinationCheckControl.examples = combinationCheckReport.affectedColumns[0].examples

      }

    return {
      header: report.header,
      controls: controls,
      loading:false,
      loaded:true,
    }
  }

  initFuncControls() {
    let controls = this.getInitialFuncControls()

    return {
      header: "",
      controls: controls,
      loading:false,
      loaded:false,
    }
  }

  private getInitialFuncControls() {
    return [{
      name: "Unique age variables",
      id: "age_attained age_at_commencement both are provided",
      description: "Only either 'Age Attained' or 'Age of Commencement' or 'Insurance Age Attained' is expected",
      error_number: 0,
      examples: null,
    }, {
      name: "Duplicate",
      id: "Duplicated lines",
      description: "There must be no duplicate lines by combination of dimensions",
      error_number: 0,
      examples: null,
    },
    {
      name: "Variable dependency",
      id: "age_at_commencement provided but age_at_commencement_definition doesn't exist",
      description: "Certain variables require additional information from a second variable",
      error_number: 0,
      examples: null,
    },
    {
      name: "Missing values",
      id: "Missing values",
      description: "Certain variables must not have missing values.",
      error_number: 0,
      examples: null,
    },
    {
      name: "Complete slicing dimensions",
      id: "Combination Check",
      description: "All combinations between min and max of slicing dimensions must be provided.",
      error_number: 0,
      examples: null,
    }
    ];
  }
}
