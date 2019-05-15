import {
  Injectable
} from '@angular/core';
import {
  Run
} from '../shared/models/run';
import {
  Dataset
} from '../shared/models/dataset';
import {
  Http
} from '@angular/http';
import {
  IfObservable
} from 'rxjs/observable/IfObservable';
import {
  Observable
} from 'rxjs/Observable';
import {
  environment
} from '../../environments/environment';
import {
  date
} from '../shared/models/date';
import {
  Decrement
} from '../shared/models/decrement';
import {
  Study
} from '../shared/models/study';
import {
  Subject, BehaviorSubject
} from 'rxjs';
import {DOCUMENT} from '@angular/common';
import { ExpTabRun } from '../shared/models/expected-tab-run';
import { StudyValidationSectionComponent } from '../components/study-validation/study-validation-section/study-validation-section.component';

@Injectable()
export class RunService {

  constructor(private http: Http) {}

  getByStudyId(studyId) {
    return this.http.get(environment.api_endpint + "run/study" + "?id=" + studyId)
      .map(res => res.json()).map(res => this.mapFromAllApi(res))
      .catch(err => Observable.throw(err))
  }

  save(run) {
    return this.http.post(environment.api_endpint + "run", this.mapToApi(run)).map(res => res.json()).map(res=>this.mapFromApi(res)).catch(err => Observable.throw(err))
  }
  saveExp(run) {
    return this.http.post(environment.api_endpint + "run/exp", this.mapToApi(run)).map(res => res.json()).map(res=>this.mapFromApi(res)).catch(err => Observable.throw(err))
  }

  saveAfterValidation(run) {
    return this.http.post(environment.api_endpint + "run/save", this.mapToApi(run)).map(res => res.json()).map(res=>this.mapFromApi(res)).catch(err => Observable.throw(err))
  }

  getValidatedDecrement(datasetId, decrements) {
    let body = {
      decrements : decrements
    }
    return this.http.post(environment.api_endpint + "datasets/"+datasetId+"/decrement", body).map(res => res.json()).catch(err => Observable.throw(err))
  }

  private mapFromAllApi(res: any[]) {
    return res.map(element => {
      return this.mapFromApi(element);
    });
  }

  private mapFromApi(element: any) {

    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }

    let run = new Run(null, null);
    console.log("RUN FROM API",element)


    run.id = element["runId"];
    run.run_description = element["runDescription"];
    run.status = element["runStatus"];
    run.creation_date = datef(element["runCreationDate"]);
    run.exposure_metric = element["runExposureMetric"];
    run.automatic_risk_amount = element["runAutRiskAmountChangeSplit"];
    run.count_analysis = element["runByCountAnalysis"];
    run.amount_analysis = element["runByAmountAnalysis"];
    run.amount_analysis_value = element["runByAmountAnalysisBasis"];
    run.amount_capped = element["runByAmountCapped"];
    run.loss_analysis_ratio = element["runLossRatioAnalysis"];
    run.loss_analysis_ratio_value = element["runLossRatioAnalysisBasis"];
    run.amount_capped_value = Number(element["runCappedAmount"]);
    run.ibnr_udf_path = element["runIbnrManuelUdf"];
    run.ibnr_amount_path = element["runIbnrAmount"];
    run.ibnr_allocation_path = element["runIbnrAllocation"];
    run.dataset = element["runDsId"];
    run.study = element["runStId"];
    run.dimensions = JSON.parse(element["runFilterJsonUrl"]);
    run.ibnr_method = element["runIbnrMethod"] || "";
    run.run_calc = element["runCalcEntity"];
    run.masterRunQX = element["masterRunQx"];
    run.masterRunIX = element["masterRunIx"];
    run.masterRunIXQX = element["masterRunIxQx"];
    run.masterRunWX = element["masterRunWx"];
    run.retainedRun = element["runRetained"];
    run.createdBy = element["runCreatedBy"]

    //SPRINT 5
    run.rating_adjustement  =  element["runRating"];
    run.joint_life          =  element["runLifeJoint"];

    console.log("rating adj after mapping -------->",run.rating_adjustement,element["runRating"])

    //setTimeout(() => {
      let decs = []
      run.decrements = [];
      if (Array.isArray(element["decrementParametersByRunId"]))
        element["decrementParametersByRunId"].forEach(dec => {

          let dec_to_push = new Decrement();
          dec_to_push.id = dec["dpId"];
          dec_to_push.decrement = dec["dpDecrement"];
          // TODO
          //+++++++++++
          dec_to_push.slicingDimension = new Array();

          if (dec["dpSlicingDimensionAge"] == true)
          {
            dec_to_push.slicingDimension.push(Decrement.D_SLICE_AGE.value);
          }
          if (dec["dpSlicingDimensionDuration"] == true)
          {
            dec_to_push.slicingDimension.push(Decrement.D_SLICE_MONTH.value);
          }
          if (dec["dpSlicingDimensionCalenderYear"] == true)
          {
            dec_to_push.slicingDimension.push(Decrement.D_SLICE_CALENDAR.value);
          }


          //----
          console.log("leading before",dec_to_push.leadingSlicingDimension ,dec["dpLeadingSlicingDimension"])

          dec_to_push.leadingSlicingDimension = dec["dpLeadingSlicingDimension"];

          console.log("leading AFTER",dec_to_push.leadingSlicingDimension,dec["dpLeadingSlicingDimension"])
          dec_to_push.slicingGranularity = dec["dpSlicingGranularity"];
          dec_to_push.monthlyDuration = dec["dpMonthlyDuration"];
          dec_to_push.monthlyDurationLeader = dec["dpMonthlyDurationBy"];
          dec_to_push.studyStart = date.dateFromString(dec["dpStudyPeriodStartDate"]);
          dec_to_push.studyEnd = date.dateFromString(dec["dpStudyPeriodEndDate"]);
          dec_to_push.partialDuration = dec["dpIncludePartial"];
          dec_to_push.attainedAgeDefinition = dec["dpAttainedAgeDef"];

          //Expected Table Save
          dec_to_push.ExpMethod = dec["dpExpMethod"]
          dec_to_push.ExpCalibration = dec["dbExpCalibration"]
          dec_to_push.ExpCalibrationUrl = dec["dbCalibrationUrl"]
          let exps = []
          dec["decrementExpectedTablesByDpId"].forEach(exp =>
          {
            let exp_to_push = new ExpTabRun()
            exp_to_push.id_base = exp["retBase"]
            exp_to_push.id_trend = exp["retTrend"]
            exp_to_push.id_adjustment = exp["retAdjustment"]
            exps.push(exp_to_push)
          })
          dec_to_push.ExpectedTable = exps

          decs.push(dec_to_push);
        });
    //}, 0);

    run.decrements = decs;

    console.log("OBJ",run)


    return run;
  }

  private mapToApi(run: Run) {
    let api_obj = {
      "runRimId": null,
      "runCode": null,
      "runDescription": run.run_description,
      "runStatus": run.status,
      "runCreationDate": date.formatDate(run.creation_date),
      "runMasterRun": null,
      "runCreatedBy": run.createdBy,
      "runExposureMetric": run.exposure_metric,
      "runAttainedAgeDef": run.attained_age,
      "runAutRiskAmountChangeSplit": run.automatic_risk_amount,
      "runByCountAnalysis": run.count_analysis,
      "runByAmountAnalysis": run.amount_analysis,
      "runByAmountAnalysisBasis": run.amount_analysis_value,
      "runByAmountCapped": run.amount_capped,
      "runLossRatioAnalysis": run.loss_analysis_ratio,
      "runLossRatioAnalysisBasis": run.loss_analysis_ratio_value,
      "runCappedAmount": run.amount_capped_value,
      "runIbnrManuelUdf": run.ibnr_udf_path,
      "runIbnrAmount": run.ibnr_amount_path,
      "runIbnrAllocation": run.ibnr_allocation_path,
      "runRating": run.rating_adjustement,
      "runLifeJoint": run.joint_life,
      "runFilterJsonUrl": JSON.stringify(run.dimensions),
      "runDsId": run.dataset,
      "runStId": run.study,
      "runIbnrMethod": run.ibnr_method,
      "decrementParametersByRunId": run.decrements.map(d => {
        let dec = {
          "dpDecrement": d.decrement || "",
          "dpSlicingDimensionAge": d.slicingDimension.indexOf(Decrement.D_SLICE_AGE.value) >= 0,
          "dpSlicingDimensionDuration": d.slicingDimension.indexOf(Decrement.D_SLICE_MONTH.value) >= 0,
          "dpSlicingDimensionCalenderYear": d.slicingDimension.indexOf(Decrement.D_SLICE_CALENDAR.value) >= 0,
          "dpLeadingSlicingDimension": d.leadingSlicingDimension,
          "dpSlicingGranularity": d.slicingGranularity,
          "dpMonthlyDuration": d.monthlyDuration,
          "dpMonthlyDurationBy": d.monthlyDurationLeader,
          "dpStudyPeriodStartDate": date.formatDate(d.studyStart),
          "dpStudyPeriodEndDate": date.formatDate(d.studyEnd),
          "dpStudyPeriodTruncated": null,
          "dpIncludePartial": d.partialDuration,
          "dpAttainedAgeDef": d.attainedAgeDefinition,
          // TODO
          // SAVE EXPECTED TABLES
          "dpExpMethod": d.ExpMethod,
          "dbExpCalibration" : d.ExpCalibration ? d.ExpCalibration : null,
          "dbCalibrationUrl" : d.ExpCalibrationUrl ? d.ExpCalibrationUrl : null,
          "decrementExpectedTablesByDpId":d.ExpMethod == "us_method" ? []:d.ExpectedTable.map((d,index) => {
            let exp = {
              "retBase":d.id_base || null,
              "retTrend":d.id_trend || null,
              "retAdjustment":d.id_adjustment || null,
              "retBasis":index + 1
          } 
          return exp
          })
        }
        //if (d.id) dec["dpId"] = d.id;
        //if (run.id) dec["dpRunId"] = run.id;

        return dec
      })
    }
    if (run.id)
      api_obj["runId"] = run.id;

      console.log("OBJ",api_obj)

    return api_obj
  }

  launchRun(runId, AllDimensions,dimensions) {
    let body = {
      "run_id": String(runId),
      "Alldimensions": AllDimensions,
      "dimensions" : dimensions
    }

    return this.http.post(environment.api_endpint + "run/start", body).map(res => res.json()).catch(err => Observable.throw(err))
  }

  getRunStatus(runId) {
    return this.http.get(environment.api_endpint + "run/status/" + runId).map(res => {
      if (res["_body"] && res["_body"] != "")
        return res.json()
      else
        Observable.throw("Not Created")
    }).catch(err => Observable.throw(err))
  }

  getContinuousRunStatus(runId) {
    let timeout
    let timespan = 10000

    let results = new Subject < any > ()

    const getResults = () => {
      this.getRunStatus(runId)
        .subscribe(run_calc => {
          if (run_calc) {
            let status = this.getStatus(run_calc.rclcStatus,run_calc.rclcStatusEa)
            switch (String(status).toUpperCase()) {
              case "PREPARING CALCULATION":
              case "CALCULATION IN PROGRESS":
              case "SYNCHRONISING RESULTS":
              case "CALCULATION TERMINATED":
                results.next(run_calc);
                break;
              case "ERROR":
              case "ABORTED":
              case "DONE":
              case "CALCULATION ERROR":
                results.next(run_calc);
                results.complete();
                clearInterval(timeout);
                break;
              default:
                console.log("NOT A VALID VALUE");
            }
          }
        });
    };
    getResults()
    timeout = setInterval(getResults, timespan);
    return results;
  }

  downloadReport(run: Run) {
   // run.decrements.forEach(d => {
      //this.http.get("run/"+run.id+"/"+d.id).subscribe()

     window.open(environment.api_endpint + "run/download/" + run.id, "page_"+run.id);

   // });
  }

  isErrorFileExist(run: Run){
    return this.http.get(environment.api_endpint + "run/download/error/check/" + run.id).map((data: any) => data.json())
  }

  downloadError(run:Run){
    window.open(environment.api_endpint + "run/download/error/" + run.id);
  }

  delete(runId:number){
    return this.http.delete(environment.api_endpint+"run/?id="+runId);
  }


  //ExpecTableWebService

  mapFrom1piExp(exp){

  }

  base: any[]
  trend: any[]
  adjs: any[]
  calibration : any[]
  

  getExpectedTabByTypeAndDecrement(type: string,dec:string, user : number){  

    switch(type) { 
      case "base": { 
        if (this.base) return new BehaviorSubject(this.base)
        else return this.getParams(type,dec,this.base,user)
      } 
      case "trend": { 
        if (this.trend) return new BehaviorSubject(this.trend)
        else return this.getParams(type,dec,this.trend,user)
      } 
      case "adjustment": { 
        if (this.adjs) return new BehaviorSubject(this.adjs)
        else return this.getParams(type,dec,this.adjs,user)
     } 
     case "calibration": { 
      if (this.calibration) return new BehaviorSubject(this.calibration)
      else return this.getParams(type,dec,this.calibration,user)
   } 
      default: { 
         break; 
      } 
   } 
  }

  getParams(t,d,tab, user : number){
    d = d.replace('+','	%2B')
    return Observable.forkJoin(
      this.http.get(environment.api_endpint+"table/run/exp?decrement="+d+"&type="+t+"&user="+user).map(res => res.json()).catch((response: any) => Observable.throw(response.json())),
    )
    .do(res=>tab=res["content"])
    .catch((response: any) => Observable.throw(response.json()))
  }

  checkBase(sid,rid,bid,dimensions,isDateOfCommencement,isDateOfBirth, exposureMetric, tablesArray,isAgeAtcom){
    console.log(tablesArray);
    
    let dims = (dimensions) ? String(dimensions.map(d => d.itemName).join(";")) : "" 
    let body = {
      "dimensions" : dims,
      "isDateOfCommencement" : isDateOfCommencement,
      "isDateOfBirth" : isDateOfBirth,
      "exposureMetric" : exposureMetric,
      "otherTables" : tablesArray,
      "isAgeAtCommencement" : isAgeAtcom
    }
    return this.http.post(environment.api_endpint + "run/exp/checkbase/"+sid+"/"+rid+"/"+bid, body).map((res) => res.json()).catch((response: any) => Observable.throw(response))
  }

  checkAdjOrTrend(bid,id,type,dimensions,isDateOfCommencement,isDateOfBirth,tablesArray, isAgeAtcom){
    let dims = (dimensions) ? String(dimensions.map(d => d.itemName).join(";")) : "" 
    let body = {
      "dimensions" : dims,
      "isDateOfCommencement" : isDateOfCommencement,
      "isDateOfBirth" : isDateOfBirth,
      "otherTables" : tablesArray,
      "isAgeAtCommencement" : isAgeAtcom

    }    
    return this.http.post(environment.api_endpint+"run/exp/check/"+type+"/"+bid+"/"+id, body).map((res) => res.json()).catch((response: any) => Observable.throw(response))
  }

  validateStudy(svsc : StudyValidationSectionComponent, ruId) {
    let body = {
      masterRunIx : Number(svsc.selectedIx),
      masterRunQx : Number(svsc.selectedQx),
      masterRunWx : Number(svsc.selectedWx),
      masterRunIxQx : Number(svsc.selectedIxQx),
      runRetained : svsc.selectedRetained.map(item => item.id),
      deleteUnusedDatasets : svsc.deleteUnused,
      ruId : ruId
    }
    return this.http.post(environment.api_endpint + "study/"+ svsc.study.id+"/validate", body).map(res => res.json()).catch(err => Observable.throw(err))
  }

 // checkCalibration(cid){
   // return this.http.get(environment.api_endpint+"run/exp/checkcalibration/"+cid).map(res => res.json()).catch(err => Observable.throw(err))
 // }

  getCalibrationControls(path) {
    let body = {
      path: (path) ? path : null
    }
    return this.http.post(environment.api_endpint + "run/exp/checkcalibration/", body).map((res) => res.json()).catch((response: any) => Observable.throw(response))
  }

  isAttainedAgeCompulsory(list:any[]){
    let body = list;
    return this.http.post(environment.api_endpint+"table/check/ageattained",body).map((res) => res.json()).catch((response: any) => Observable.throw(response))
  }

  getStatus(sasStatus,eaStatus) {
    if(!sasStatus) {
      return null
    }
    switch (sasStatus.toUpperCase()) {
      case "PENDING" :
        return "Preparing Calculation"
      case "LAUNCHING" :
      case "IN PROGRESS" : 
        return "Calculation In Progress"
      case "ERROR":
      case "ABORTED":
        return "Calculation Error"
      case "DONE":
        if(!eaStatus) return "Calculation Terminated"
        switch (eaStatus.toUpperCase()) {
          case "SYNCHRONIZING RESULT":
            return "Synchronising Results"
          case "DONE" : 
            return "DONE"
          case "ERROR":
          case "ABORTED":
          case "ERROR WHILE SYNCHRONIZING RESULT":
            return "Error"        
          default:
            return "Calculation Terminated";
        }
      default:
        return "";
    }
  }
}
