import { Injectable } from '@angular/core';

export interface Label
{
  screen: SCREEN,
  name: string,
  detail: string,
}

export enum SCREEN{
  STUDY,
  DATASET,
  RUN,
  IBNR,
  ACCESS_RIGHT
}

@Injectable()
export class HoverOverService {

  constructor() { }

  getDetail(screen:SCREEN,name:string)
  {
    let label = this.LABELS.find(l=> l.screen == screen && name.toLocaleLowerCase() == l.name.toLocaleLowerCase())
    return (label)? label.detail: null
  }

  readonly LABELS:Label[] = [
    //Study
    {screen:SCREEN.STUDY , name: "Study ID",detail:""},
    {screen:SCREEN.STUDY , name: "EA Data Source",detail:"Source of the datasets used for the EA Study"},
    {screen:SCREEN.STUDY , name: "Study Requester",detail:"Principal EA Client that requested the study"},
    {screen:SCREEN.STUDY , name: "Client Short Name",detail:"Client abbreviation which is used for Study ID creation (3-5 letters)"},
    {screen:SCREEN.STUDY , name: "Client",detail:"Name of client as defined in Omega"},
    {screen:SCREEN.STUDY , name: "Distribution brand",detail:"Commercial brand under which the buisiness is sold"},
    {screen:SCREEN.STUDY , name: "Client group",detail:"Client group name"},
    {screen:SCREEN.STUDY , name: "Client country",detail:"Country where the client is based"},
    {screen:SCREEN.STUDY , name: "Start of observation period",detail:"Earliest date covered by the dataset"},
    {screen:SCREEN.STUDY , name: "End of observation period",detail:"Latest date covered by the dataset"},
    {screen:SCREEN.STUDY , name: "Quality data provider",detail:"Score of the quality and confidence that is generally granted to the data provider"},
    {screen:SCREEN.STUDY , name: "Creation date",detail:""},
    {screen:SCREEN.STUDY , name: "Current study status",detail:""},
    {screen:SCREEN.STUDY , name: "Comment",detail:"Short description (255 characters) of context and scope of the study"},
    {screen:SCREEN.STUDY , name: "Date of status update",detail:""},
    {screen:SCREEN.STUDY , name: "Status last modified by",detail:""},
    {screen:SCREEN.STUDY , name: "Number of Runs",detail:""},
    {screen:SCREEN.STUDY , name: "Master run ID",detail:""},
    {screen:SCREEN.STUDY , name: "Line of Business",detail:"Type of risk of business studied"},
    {screen:SCREEN.STUDY , name: "Created By",detail:""},
    {screen:SCREEN.STUDY , name: "Treaty Number",detail:"Omega Treaty Number"},
    {screen:SCREEN.STUDY , name: "Market",detail:""},
    {screen:SCREEN.STUDY , name: "Type of calculation engine",detail:"The type of engine that will be used for the calculation"},
    {screen:SCREEN.STUDY , name: "Study documentation",detail:"Additional documentation on the Study (context, data issues and corrections, assumptions, scenarios etc.) Various formats are allowed (pdf, doc, docx, xls, xlsx, ppt, pptx, txt, rar, zip) and several documents can be gathered in a rar or zip file."},
    //Dataset Screen
    {screen:SCREEN.DATASET, name:"Name", detail:"Dataset Name"},
    {screen:SCREEN.DATASET, name:"Data file type", detail:"Policy file gathers information about the insureds, events and policies and its benefits"},
    {screen:SCREEN.DATASET, name:"Product file focuses on product",detail:"treaty and client information"},
    {screen:SCREEN.DATASET, name:"Data structure type", detail:"The Data Structure Type indicates whether the policy file is combined (exposure and event in common rows) or split (exposure and event in different rows)"},
    {screen:SCREEN.DATASET, name:"First Snapshot", detail:"Does the first snapshot period cover predominantly an inforce portfolio or new business? Determines the exposure start for first snapshot period. Superseded by corresponding variable in product file if provided"},
    {screen:SCREEN.DATASET, name:"Portfolio inception/ renewal date", detail:"Product inception date or group renewal date. Determines exposure start for first snapshot period. Superseded by corresponding variable in product file if provided"},
    {screen:SCREEN.DATASET, name:"Annual snapshot extraction timing", detail:"Day of data extraction in case of annual snapshots. Applied to all snapshots."},
    {screen:SCREEN.DATASET, name:"Replace missing entries with information from previous period", detail:"Do you wish that missing entries of a snapshot for a given policy/person are replaced by previous snapshot information?"},
    {screen:SCREEN.DATASET, name:"Comment", detail:"Short description (255 characters) of the dataset"},
    {screen:SCREEN.DATASET, name:"Exposure extraction date", detail:"Date of data extraction of exposure information by original data provider (typically cedent). In case of input data being combination of multiple extracted files, provide maximum date"},
    {screen:SCREEN.DATASET, name:"Event extraction date", detail:"Date of data extraction of event information by original data provider (typically cedent). In case of input data being combination of multiple extracted files, provide maximum date"},
    {screen:SCREEN.DATASET, name:"Migration source", detail:""},
    {screen:SCREEN.DATASET, name:"Created by", detail:""},
    {screen:SCREEN.DATASET, name:"Creation date", detail:""},
    //Run Screen
    {screen:SCREEN.RUN,name:"RunID", detail:""},
    {screen:SCREEN.RUN,name:"engine parameters_id", detail:""},
    {screen:SCREEN.RUN,name:"Decrement", detail:"Decrement that will be calculated"},
    {screen:SCREEN.RUN,name:"Dimensions", detail:"Input Dimensions that will be included in the run calculation"},
    {screen:SCREEN.RUN,name:"Study Period Start Date", detail:"Start of the observation period retained for the decrement"},
    {screen:SCREEN.RUN,name:"Study Period End Date", detail:"End of the observation period retained for the decrement"},
    {screen:SCREEN.RUN,name:"Include partial duration", detail:"TBD"},
    {screen:SCREEN.RUN,name:"Exposure Metric", detail:"The exposure metric can be intial or central. Central refers to the assumption of constant hazard rate. Initial refers to the Balducci method that induces a prolongation of the exposure till the next birthday/policy year in case of event death"},
    {screen:SCREEN.RUN,name:"Slicing Dimensions", detail:"Time-dependent dimensions included in the calculation results"},
    {screen:SCREEN.RUN,name:"Leading Slicing Dimension", detail:"Leading dimension used by the (initial) Balducci method for prolonging exposure in case of event"},
    {screen:SCREEN.RUN,name:"Slicing granularity", detail:"Granularity of the slicing dimension selected"},
    {screen:SCREEN.RUN,name:"Monthly duration", detail:"Duration (in year) of the monthly slicing granularity. After this period, the slicing is done annually"},
    {screen:SCREEN.RUN,name:"Attained age definition", detail:"Age definition used for attained age calculation"},
    {screen:SCREEN.RUN,name:"By count analysis", detail:"Include count-based results in calculation (based on number of exposure years and number of events)"},
    {screen:SCREEN.RUN,name:"By amount analysis", detail:"Include amount-based results in calculation (based on amount weighted exposure years and event amounts)"},
    {screen:SCREEN.RUN,name:"By amount analysis basis", detail:"Amount analysis is based on event amount and exposure amount. Can be based on insured or reinsured amounts."},
    {screen:SCREEN.RUN,name:"By amount capped", detail:"Amount analysis is done with capped amounts (for example to avoid outliers)"},
    {screen:SCREEN.RUN,name:"Missing entries", detail:"Replace missing variable entries (e.g. smoker_status) of a snapshot for a given policy/person by previous snapshot information?"},
    {screen:SCREEN.RUN,name:"Exposure holes", detail:"In case a policy is absent from a snapshot but present in both surrounding snapshots with the same policy status then continued exposure is assumed"},
    {screen:SCREEN.RUN,name:"Capped amount", detail:"Capped Amount"},
    {screen:SCREEN.RUN,name:"Rating adjustment method", detail:"Method for adjusting expected results according to policy loadings (extra rating)"},
    {screen:SCREEN.RUN,name:"Automatic Risk Amount Change", detail:"Exposure risk amount is automatically adjusted depending on rule triggered by benefit_change_rate_type"},
    {screen:SCREEN.RUN,name:"Joint Lives", detail:"In case a Joint Policy is recorded in two different lines (different Life ID with same Policy ID), each lapse will be count as 0.5 to avoid double counting"},
    {screen:SCREEN.RUN,name:"Run description", detail:"Short description (255 characters) of the run"},
    {screen:SCREEN.RUN,name:"Dataset", detail:"Dataset"},
    {screen:SCREEN.RUN,name:"Loss Ratio Analysis", detail:"Include Loss Ratio results in calculation (based on event amounts and annual premiums)"},
    {screen:SCREEN.RUN,name:"Loss Ratio Analysis Basis", detail:"Loss Ratio is based on event amount and annual premium. Can be based on insured or reinsured amounts."},
    {screen:SCREEN.RUN, name:"IBNR method",detail:"Selection of IBNR method (upload of table with Ultimate Development Factors or upload of aggregated IBNR amounts with allocation table)"},
    {screen:SCREEN.RUN, name:"Leading Slicing Dimensions",detail:"Main dimension used for analysis. Typically Attained Age for age-based rate tables or Policy Duration for select tables or lapse analysis"},
    {screen:SCREEN.RUN, name:"Monthly Slicing",detail:"Monthly Slicing"},
    {screen:SCREEN.RUN, name:"Expected Tables Method",detail:"Expected Tables Method"},









    //IBNR
    {screen:SCREEN.IBNR, name:"IBNR amount",detail:"IBNR amounts that are allocated to its relevant code"},
    {screen:SCREEN.IBNR, name:"IBNR allocation",detail:"Definition of IBNR codes in function of dimension of the dataset"},

    //ACESS RIGHT
    {screen:SCREEN.ACCESS_RIGHT, name:"Exception",detail:"Exception indicates when the access right has been manually modified from the default rule"},
    {screen:SCREEN.ACCESS_RIGHT, name:"General Profile",detail:"General Profile indicates whether the user is allowed to create a study or not"},
    {screen:SCREEN.ACCESS_RIGHT, name:"Additional Runs Retained",detail:"Select runs that should be kept after the study validation, every other run will be removed"},

  ]

}
