import { RunTable } from "./run-table";
import { date } from "./date";
import { Study } from "./study";
import { Run } from "./run";
import { ExpTabRun } from "./expected-tab-run";

export class Decrement
{
    id: number
    name: string
    decrement:string = ""
    tables:RunTable[] = []
    studyStart:date
    studyEnd:date
    slicingDimension:string[] = new Array<string>()
    leadingSlicingDimension:string //= "age"
    slicingGranularity:string = ""
    partialDuration:boolean = true
    monthlyDuration:number
    monthlyDurationLeader:string
    attainedAgeDefinition:string = "Age Last Birthday"
    ExpMethod:string 
    ExpCalibration : number
    ExpCalibrationUrl : string
    ExpectedTable: ExpTabRun[] = new Array<ExpTabRun>()

    constructor(study?:Study,run?:Run)
    {
        if(study)
        {
            this.studyStart = study.startObsDate
            this.studyEnd = study.endObsDate
        }

        // this.leadingSlicingDimension = Decrement.D_SLICE_CALENDAR.value
        this.slicingDimension = [Decrement.D_SLICE_AGE.value, Decrement.D_SLICE_MONTH.value, Decrement.D_SLICE_CALENDAR.value]

    }

    private checkedSlicing(val: boolean,run:Run) {
      if (!val)
        return true
      else
        return !(val && run.age_of_birth_check)
    }


    static readonly D_SLICE_AGE = {
        value: "age",
        name: "Age",
        isDisabled: true
      }
      static readonly D_SLICE_MONTH = {
        value: "duration",
        name: "Duration",
        isDisabled: false

      }
      static readonly D_SLICE_CALENDAR = {
        value: "calendar",
        name: "Calendar Year",
        isDisabled: false

      }

      static readonly G_SLICE_AGE = {
        value: "annual",
        name: "Annual"
      }
      static readonly G_SLICE_MONTH = {
        value: "monthly",
        name: "Monthly"
      }

      clone(){
        let obj = new Decrement()
        obj.name = this.name
        obj.decrement = this.decrement
        obj.tables = []
        this.tables.forEach(s=>obj.tables.push(s.clone()))
        obj.studyStart = this.studyStart
        obj.studyEnd = this.studyEnd
        obj.slicingDimension = []
        this.slicingDimension.forEach(s=>obj.slicingDimension.push(s))
        obj.leadingSlicingDimension = this.leadingSlicingDimension
        obj.slicingGranularity = this.slicingGranularity
        obj.partialDuration = this.partialDuration
        obj.monthlyDuration = this.monthlyDuration
        obj.monthlyDurationLeader = this.monthlyDurationLeader
        this.ExpectedTable.forEach(s => obj.ExpectedTable.push(s.clone()))
        obj.ExpMethod = this.ExpMethod
        obj.ExpCalibration = this.ExpCalibration
        obj.ExpCalibrationUrl = this.ExpCalibrationUrl
        return obj
      }

      showPolicyDuration()
      {
        return ( this.decrement == "qx" || this.decrement == "wx"  || this.decrement == "ix" )
      }
}
