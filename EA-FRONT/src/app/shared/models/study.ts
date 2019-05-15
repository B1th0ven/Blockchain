import {
  Dataset
} from "./dataset"
import {
  date
} from "./date";
import { Timestamp } from "../../../../node_modules/rxjs";

export class Study {
  // Basic
  id: Number
  code: string
  name: string
  description: string
  datetime: string
  date: string
  creationDate: date
  lastStatusModificationDate : date
  lastStatusModifiedBy : any 
  time: string
  datasets: Array < Dataset >
  status: string
    // Required
    createdBy: string
  Market: string
  studyClient: string
  dataSource: string
  startObsDate: date = new date()
  endObsDate: date = new date()
  calcEngine: string
  lineOfBuisness: Array < any >
    clientShortName: string
  clientGroup: string
  client: string
  clientCountry: string
  // Not required
  riskCountry: string
  comment: string
  clientCommercialName: string
  distributionBrand: string
  qualityDataProvider: string
  treatyNumber: string
  documentName: string
  attachedFilePath: string
  count: number = null
  number: number;
  creator:any;
  countRuns: number = null

  //for managinf  study deletion
  flag: String
  dateLastFlagModification: any



  constructor(study ? ) {
    if (study) {
      this.name = study["name"]
      this.description = study["description"]
      this.id = study["id"]
      this.date = study["date"]
      this.time = study["time"]
      this.datetime = study["datetime"]
      this.datasets = study["datasets"] || this.datasets
      this.lineOfBuisness = study["lineOfBuisness"] || new Array < any > ()
    }

    if (!this.datetime) {
      this.datetime = String(new Date());
      this.date = new Date().toLocaleDateString()
      this.time = new Date().toLocaleTimeString()
    }

    this.lineOfBuisness = new Array < any > ()
    let d : Date = new Date();
    console.log(d);
    
    let dddd : string = date.getCurrentDate(d)
    console.log(dddd);
    
    this.creationDate = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])
    this.lastStatusModificationDate = new  date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])
    this.number = Study.STUDY_COUNT++;
  }

  valid() {
    return (
      this.studyClient && this.studyClient != "" &&
      this.dataSource && this.dataSource != "" &&
      this.startObsDate && date.valid(this.startObsDate) &&
      this.endObsDate && date.valid(this.endObsDate) &&
      this.validDates() &&
      this.calcEngine && this.calcEngine != "" &&
      this.clientGroup && this.clientGroup != "" &&
      this.clientShortName && this.clientShortName != "" &&
      this.client && this.client != "" &&
      this.clientCountry && this.clientCountry != "" &&
      this.lineOfBuisness && this.lineOfBuisness.length > 0 &&
      this.validClientsShortName() && this.validStartDate() && this.validClientsShortNameCharacters()
    )
  }

  validClientsShortName() {
    let len
    if (this.clientShortName && this.clientShortName != "") {
      len = this.clientShortName.length

      if (len <= 5 && len >= 3) {
        return true
      } else {
        return false
      }
    }
    return true
  }

  validClientsShortNameCharacters() {
    if(this.clientShortName) {
      let validCaracters = this.clientShortName.match(/[A-Za-z0-9]/g)      
      let validCaractersNumbre = validCaracters ? validCaracters.length : 0
      return validCaractersNumbre == this.clientShortName.length
    }
    return true
  }

  validClient() {
    if (!this.clientGroup || this.clientGroup == '') {
      this.client = null
      return false;

    } else if (this.clientGroup == "other") {
      this.client = "other"
      return false;

    } else if (this.clientGroup == "multi") {
      this.client = "multi"
      this.validTraety()
      return false;
    } else {
      return true;
    }
  }

  validTraety() {
    if (this.client && this.client == "multi") {
      this.treatyNumber = "multi"
    }
  }

  getTreatyList() {

  }

  validDates() {
    if (!this.startObsDate || !this.endObsDate)
      return true

    if (new Date(this.startObsDate.year + "-" + this.startObsDate.month + "-" + this.startObsDate.day) >
      new Date(this.endObsDate.year + "-" + this.endObsDate.month + "-" + this.endObsDate.day))
      return false;
    else
      return true
  }

  validStartDate() {
    if (!this.startObsDate)
      return true
    if(!this.checkValidDateFormat(this.startObsDate)) {
      return false
    }

    let now = new Date()

    let today = new Date(now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate())
    let start = new Date(this.startObsDate.year + "-" + this.startObsDate.month + "-" + this.startObsDate.day)

    if (start >= today)
      return false;
    else
      return true

  }

  validStartDateFormat() {
    return this.checkValidDateFormat(this.startObsDate)    
  }

  validEndDate() {
    if (!this.endObsDate)
      return true
        
    let now = new Date()

    let today = new Date(now.getFullYear() + "-" + (now.getMonth() + 1) + "-" + now.getDate())
    let end = new Date(this.endObsDate.year + "-" + this.endObsDate.month + "-" + this.endObsDate.day)

    if (end >= today)
      return false;
    else
      return true

  }

  validEndDateFormat() {
    return this.checkValidDateFormat(this.endObsDate)
  }

  checkValidDateFormat(date : date) {
    if(!date) {
      return true
    }
    let date2 = new Date();
    date2.setFullYear(date.year, date.month - 1, date.day);
    return date.valid
  }

  save() {

    if (!this.valid())
      return false

    if (this.comment)
      this.comment = this.comment.slice(0, 254);

    if (!this.datetime) {
      this.datetime = String(new Date());
      this.date = new Date().toLocaleDateString()
      this.time = new Date().toLocaleTimeString()
    }

    let studies = Study.getAll()

    if (!Array.isArray(studies))
      studies = new Array < Study > ()

    if (!this.id) {
      //this.id = this.newID()

      studies.push(this)
    } else {
      let index = studies.findIndex(s => s.id == this.id)

      if (index) {
        studies[index] = this
      } else
        return false
    }

    
    localStorage.setItem("studies", JSON.stringify(studies))

    return true
    //TODO
    //Save in local storage
  }

  delete() {

  }

  static getAll() {
    return JSON.parse(localStorage.getItem('studies'))
  }

  private newID() {
    return (Date.now().toString(36) + Math.random().toString(36).substr(2, 5)).toUpperCase();
  };

  private formatDate(date) {
    var d = new Date(date),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

  public static mapApiStudy(study: Study) {
    let lastObj = {
      "stId": study.id || null,
      "stCode": study.code,
      "stRdsId": study.dataSource,
      "stRcnId": (Number(study.client)) ? study.client : null,
      "stRcetId": study.calcEngine,
      "stRpgId": (Number(study.clientGroup)) ? study.clientGroup : null,
      "stStcId": study.studyClient,
      "stRtId": (Number(study.treatyNumber)) ? study.treatyNumber : null,
      "stStartObservationDate": date.formatDate(study.startObsDate),
      "stEndObservationDate": date.formatDate(study.endObsDate),
      "stCreatedDate": date.formatDate(study.creationDate),
      "stComment": study.comment || "",
      "stRcId": study.clientCountry,
      "stStatus": study.status && study.status.trim() != "" ? study.status : "progress",
      "stLastStatusModificationDate": (study.lastStatusModificationDate ? date.formatDate(study.lastStatusModificationDate): null),
      "stMultiParentGroup": study.clientGroup == "multi",
      "stMultiCedent": study.client == "multi",
      "stMultiTreaty": study.treatyNumber == "multi",
      "stOtherParentGroup": study.clientGroup == "other",
      "stOtherCedent": study.client == "other",
      "stOtheriTreaty": study.treatyNumber == "other",
      "stShortName": study.clientShortName,
      "stQualityDataProvider": study.qualityDataProvider,
      "stDistributionBrand": study.distributionBrand,
      "refLobsById": study.lineOfBuisness,
      "stAttachedFilePath": study.attachedFilePath,
     "stRlobId": null,
      "stLastStatusModifiedBy": null,
      "stCreatedById": study.creator.ruId,
      "stFlag":"not_deleted"
    }
    console.log(lastObj);
    
    

    return lastObj;
  }

  public mapFromApiStudy(study) {

    let pct = (mul, oth, value) => {
      if (mul) return "multi"
      if (oth) return "other"
      if (value) return value
    }

    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }
    console.log(study);
    

    this.id = study["stId"]
    this.code = study["stCode"]
    this.dataSource = study["stRdsId"]
    this.calcEngine = study["stRcetId"]
    this.studyClient = study["stStcId"]
    this.clientCountry = study["stRcId"]
    this.status = study["stStatus"]
    this.lineOfBuisness = study["refLobsById"]
    this.qualityDataProvider = study["stQualityDataProvider"]
    this.distributionBrand = study["stDistributionBrand"]
    this.clientShortName = study["stShortName"]
    this.clientCountry = study["stRcId"]
    this.client = pct(study["stMultiCedent"], study["stOtherCedent"], study["stRcnId"])
    this.clientGroup = pct(study["stMultiParentGroup"], study["stOtherParentGroup"], study["stRpgId"])
    this.treatyNumber = pct(study["stMultiTreaty"], study["stOtheriTreaty"], study["stRtId"])
    this.comment = (study["stComment"])?study["stComment"].trim():""
    this.startObsDate = datef(study["stStartObservationDate"])
    this.endObsDate = datef(study["stEndObservationDate"])
    this.creationDate = datef(study["stCreatedDate"])
    this.attachedFilePath = study["stAttachedFilePath"]
    this.flag = study["stFlag"]
    this.dateLastFlagModification = study["stLastFlagModificationDate"]
    this.creator = study["refUserByStCreatedById"]
    this.lastStatusModificationDate = study["stLastStatusModificationDate"] ? datef(study["stLastStatusModificationDate"]) : null
    this.lastStatusModifiedBy = study["refUserByStLastStatusModifiedBy"]
  }

  hasDatasets=()=>(this.count>0)
  hasRuns=()=>(this.countRuns>0)

  getNumberOfRuns(){
    return this.countRuns || 0
  }
  isValidated() {
    if(!this.status) return false
    return this.status.toLowerCase().trim() == "validated"
  }


  public static STUDY_COUNT = 0
}
