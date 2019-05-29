import {
  FileType
} from "./file-type";
import {
  date
} from "./date";

export class Dataset {
  id: string
  name: string
  creationDate: date
  description: string
  datetime: string
  date: string
  time: string
  mode: Number = 1
  jobID: Number
  files: Array < FileType >
  eventExtractionDate: date = new date()
  exposureExtractionDate: date = new date()
  studyId: Number
  techReport: any
  funcReport: any
  notExecReport: any
  creator:any
  dataInjectionTableau: boolean = false
  changed = false;
  firstSnapshot: number ;
  portfolioInception: date;
  annualSnapshot: date;
  missingEntries: boolean;
  temporaryFile: Array < FileType >;
  temporaryData: any;
  header: Array<string>;
  exposureHoles:boolean = true;
  

  constructor(dataset ? ) {
    if (dataset) {
      console.log("i'm called const hahahahah",dataset)
      this.name = dataset["name"]
      this.description = dataset["description"]
      this.id = dataset["id"]
      this.date = dataset["date"]
      this.time = dataset["time"]
      this.datetime = dataset["datetime"]
      this.files = dataset["files"] || this.files
      this.mode = dataset["mode"] || 0
      this.jobID = dataset["jobID"]
      this.creationDate = dataset["creationDate"]
      
      this.eventExtractionDate = (date.valid(dataset["eventExtractionDate"]))? new date(
        dataset["eventExtractionDate"].year,
        dataset["eventExtractionDate"].month,
        dataset["eventExtractionDate"].day
      ):null
      this.exposureExtractionDate = (date.valid(dataset["exposureExtractionDate"]))? new date(
        dataset["exposureExtractionDate"].year,
        dataset["exposureExtractionDate"].month,
        dataset["exposureExtractionDate"].day
      ):null
      this.studyId = dataset["studyId"]
      this.techReport = dataset["techReport"]
      this.funcReport = dataset["funcReport"]
      this.notExecReport = dataset["notExecReport"]
      this.creator = dataset["dsCreatedBy"]
      this.dataInjectionTableau = dataset["dsDataAvailableTableau"]
      this.firstSnapshot = dataset["firstSnapshot"]
      this.portfolioInception = dataset["portfolioInceptionDate"]
    }

    if (!this.files) {
      this.createDatasetFileHolders()
    }

    if (!dataset || !dataset["creationDate"]) {
      let d : Date = new Date();
      let dddd : string = date.getCurrentDate(d)
      this.creationDate = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])
    }
  }

  hasReport():boolean
  {
    if ( this.funcReport && this.techReport )
      return true
    else
      return false 
  }

  createDatasetFileHolders() {
    let fileTypes = ["policy", "product"]

    if (!this.files || fileTypes.length != this.files.length )
      this.files = new Array < FileType > ()
      if(!this.temporaryFile){
      this.temporaryFile = new Array <FileType> ()
    }
    for (let fileType of fileTypes) {
      if (!this.files || !this.files.find(f => f.type == "product")) {
        this.files.push(new FileType({
          type: fileType
        }));
        if(!this.temporaryFile || !this.temporaryFile.find(f => f.type == "product")){
        this.temporaryFile.push(
          new FileType({
            type: fileType
          })
        )}
      }
    }
  }
  addPolicyFile(){
    if(this.temporaryFile.length<10){
    this.temporaryFile.splice(-1,0,new FileType({
      type: "policy"
    }))
  }
  }
  setJobID(id) {
    this.jobID = id
  }

  resetJob() {
    this.jobID = null
  }

  public static mapToApiFile(ds : Dataset, Header, maxyears, minyears){
    return {
        "firstSnapshot": (ds.firstSnapshot == 1)?'new portfolio': 'inforce portfolio' ,
        "portfolioInceptionDate": date.formatDate(ds.portfolioInception),
        "AnnualSnapshotExtractionTiming": date.formatDate(ds.annualSnapshot,'dd/mm')?date.formatDate(ds.annualSnapshot,'dd/mm').toString():null,
        "missingvalues": ds.missingEntries,
        "listPolicySnapshotPath":  ds.extractpath(ds.temporaryFile),
        "studyId": ds.studyId.toString(),
        "datasetid": ds.id.toString(),
        "allCols": Header,
        "minReportingPeriod": minyears,
        "maxReportingPeriod": maxyears,
        "exposureHoles": ds.exposureHoles
    }
}
public extractpath(files: Array<FileType>): Array<string> {
  let res = []
  files.forEach(element => { 
     if(element.type == 'policy')
      res.push(element.path)
      
  });
  return res
}
  public static mapToApi(ds: Dataset) {
    let d : Date = new Date();
    let dddd : string = date.getCurrentDate(d)
    let dates : date = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])
    let res = "combined"
    if (ds.mode == 0) res = "split"
    if (ds.mode == 2) res = "snapshot"
    return {
      "dsId": ds.id,
      "dsName": (ds.name) ? ds.name.trim() : null,
      "dsExposureExtractionDate": date.formatDate(ds.exposureExtractionDate),
      "dsEventExtractionDate": date.formatDate(ds.eventExtractionDate),
      "dsDataStructureType": res,
      "dsStId": ds.studyId,
      "dsComment" : ds.description,
      "dsCode": null,
      "dsEventExposureFileId": null,
      "dsProductFileId": null,
      "dsEventExposureFile": (ds.hasReport())? FileType.mapToApi(ds.files, "policy"):null,
      "dsProductFile": (ds.hasReport())? FileType.mapToApi(ds.files, "product"):null,
      "dsTechReport": JSON.stringify(ds.techReport),
      "dsFuncReport": JSON.stringify(ds.funcReport),
      "dsNotExecuted": (ds.notExecReport)? JSON.stringify(ds.notExecReport):"",//.join(";"): "",
      "dsCreatedBy": ds.creator,
      "dsCreatedDate":(ds.creationDate)? date.formatDate(ds.creationDate): dates,
      "dsDataAvailableTableau": ds.dataInjectionTableau,
      "firstSnapshot": (ds.firstSnapshot == 1)?'new portfolio': 'inforce portfolio' ,
      "portfolioInceptionDate": date.formatDate(ds.portfolioInception),
      "annualSnapshotExtractionTiming": date.formatDate(ds.annualSnapshot,'dd/mm')? date.formatDate(ds.annualSnapshot,'dd/mm').toString(): null,
      "snapshotMissingValues": ds.missingEntries,
      "exposureHoles":ds.exposureHoles
    }
  }

  public static mapFiles(ds:Dataset) {
    let d : Date = new Date();
    let dddd : string = date.getCurrentDate(d)
    let dates : date = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])
    let res = "combined"
    if (ds.mode == 0) res = "split"
    if (ds.mode == 2) res = "snapshot"
    let obj = {
      "dsId": ds.id,
      "dsName": (ds.name) ? ds.name.trim() : null,
      "dsExposureExtractionDate": date.formatDate(ds.exposureExtractionDate),
      "dsEventExtractionDate": date.formatDate(ds.eventExtractionDate),
      "dsDataStructureType": res,
      "dsStId": ds.studyId,
      "dsCode": null,
      "dsEventExposureFile": FileType.mapToApi(ds.files, "policy"),
      "dsProductFile": FileType.mapToApi(ds.files, "product"),
      "dsTechReport": JSON.stringify(ds.techReport),
      "dsFuncReport": JSON.stringify(ds.funcReport),
      "dsNotExecuted": (ds.notExecReport)? JSON.stringify(ds.notExecReport) : "",//.join(";"): ""
      "dsCreatedBy": ds.creator,
      "dsCreatedDate":(ds.creationDate)? date.formatDate(ds.creationDate): dates,
      "dsComment" : ds.description,
      "dsDataAvailableTableau": ds.dataInjectionTableau

    }
    
    
    return obj;
  }
  public static SaveFiles(dataset: Dataset){
    let result = []
    if(dataset.temporaryFile){
    dataset.temporaryFile.forEach(element => { 
      if(element.type != 'product'){
        result.push(
            {
                "dataSet":dataset.id,
                "esfId": null,
                "fileType": 'snapshot',
                "fileLink": element.path,
                "fileName": element.name,
                "fileHeader": element["columns"]? element["columns"].toString():null,
                "inconsistentColumns": element["inconsistent"]?element["inconsistent"].toString():null,
                "ReportingYear": element["maxyear"]?element["maxyear"][0]:null
                
            }
        )
    }})};
    
    return result
}
  public mapFromApi = (data) => {

    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }
    
    this.id = data["dsId"]
    this.name = data["dsName"].trim()
    this.exposureExtractionDate = date.dateFromString(data["dsExposureExtractionDate"]) || new date()
    this.eventExtractionDate = date.dateFromString(data["dsEventExtractionDate"]) || new date()
    this.studyId = data["dsStId"]
    this.creator = data["dsCreatedBy"]
    let mod = String(data["dsDataStructureType"]).trim()
    if(mod == "combined") this.mode = 1 ;
    if(mod == "split") this.mode = 0 ;
    if(mod == "snapshot") this.mode = 2 ;

    this.description = data["dsComment"]
    //FILES
    this.mapFileFromApi(data["dsEventExposureFile"], "policy")
    this.mapFileFromApi(data["dsProductFile"], "product")
  
    //REPORT
    this.notExecReport = data["dsNotExecuted"] ? JSON.parse(data["dsNotExecuted"]): null//String(data["dsNotExecuted"]||"").split(";")
    this.techReport = JSON.parse(data["dsTechReport"])
    this.funcReport = JSON.parse(data["dsFuncReport"])

    this.creationDate = datef(data["dsCreatedDate"])
    this.dataInjectionTableau = data["dsDataAvailableTableau"]
    this.firstSnapshot = data["firstSnapshot"] == 'new portfolio'? 1: 0 
    this.portfolioInception = date.dateFromString( data["portfolioInceptionDate"]) 
    this.annualSnapshot = date.dateFromString(data["annualSnapshotExtractionTiming"]) 
    this.missingEntries = data["snapshotMissingValues"]
    return this
  }

  public mapFileFromApi = (res: any, type: string) => {

    let file = this.files.find(f => f.type == type)
    let ind =  this.files.findIndex(f => f.type == type)
    
    if (!res) return null
    file.name = res["eafName"]
    file.path = res["eafLink"]
    file.header = res["eafHeader"]
    
    file.ignored = (res["eafIgnored"])? res["eafIgnored"].split(";"):[]
    file.columns = (file.header)? file.header.split(";"):[]
    file.missing = []  
    file.duplicated = []
    file.status = "ok"
    if(res["eafSubmitter"])  file.privacySubmitter.mapFromApi(res["eafSubmitter"])
    if(res["eafprivacyDate"])file.privacyDate = new Date(res["eafprivacyDate"])
    if(res["eafDataRestriction"])file.privacyDataRestriction = res["eafDataRestriction"]
    if(res["eafDataDeletion"])file.privacyDataDeletion = this.getDateFromTimeStamp(res["eafDataDeletion"])

    this.files[ind]= file
  }

  getDateFromTimeStamp(timeStamp) {
    let dates = new Date(timeStamp);
    let d : date = new date(dates.getFullYear(), dates.getMonth()+1, dates.getDate());    
    return d;
  }
}
