import { date } from "../../../shared/models/date";

export class ExpectedTable{

  name:string
  country:any
  country_id:number = null
  version:number = 1
  decrement:string
  origin:string
  type:string
  exposure_method:string
  application_year:number
  publication_year:number
  source:string
  code:string

  id:number
  comment:string

  age_min:number
  age_max:number
  cal_year_min:number
  cal_year_max:number
  duration_min:number
  duration_max:number

  path:string
  func_report:any
  tech_report:any
  comp_report:any
  latest_version: boolean = true;

  dimensions: string;

  creation_date:date
  creator: any
  status:string
  isConfidential: boolean = false

  constructor(table?)
  {
    if (table)
    {
      this.name = table["stuff"]
      this.country = table["stuff"]
      this.version = table["stuff"]
      this.decrement = table["stuff"]
      this.origin = table["stuff"]
      this.type = table["stuff"]
      this.application_year = table["stuff"]
      this.publication_year = table["stuff"]
      this.source = table["stuff"]
    }
  }
}
