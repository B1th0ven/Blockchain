import { ExpectedTable } from "../../models/expected-table.model";

export function isValid(table:ExpectedTable):boolean
{
  let res = (
    CheckString(table.name)
    && table.country && table.country.rcId
    && CheckString(table.decrement)
    && CheckString(table.source)
    && CheckString(table.origin)    
    && CheckString(table.type)
    && checkExposureMethod(table)
    && ( !isBaseOrTrend(table) || ( isBaseOrTrend(table) && CheckNumber(table.application_year) && isYearValid(table.application_year)) )
    && CheckNumber(table.publication_year)
    && isYearValid(table.publication_year)
    && isCompValid(table)
    && isTechValid(table)
    && isFuncValid(table)
  )
  return res
}

export function checkExposureMethod(t:ExpectedTable)
{
  if(t.type.trim().toLocaleLowerCase() == "base")
  return CheckString(t.exposure_method)  
  else return true 
}
export function showExtractedValues(t:ExpectedTable)
{
  if (t.path && t.path != "" ) return true;

  return false;
}

export function isTyped(t:ExpectedTable)
{
  return CheckString(t.type)
}

export function typeChanged(t:ExpectedTable)
{
  if ( !isBaseOrTrend(t) ) t.application_year = null

  if ( t.path && t.path != "" && !t.id )
  {
    t.path = t.comp_report = t.func_report = t.tech_report = null
  }
}

export function isBaseOrTrend(t:ExpectedTable): boolean{
  return (t.type == "base" || t.type == "trend")
}

export function isTrend(t:ExpectedTable): boolean{
  return (t.type == "trend")
}

export function disableInput(t:ExpectedTable,inputName)
{
  if ( inputName == "type" && t.id && t.path && t.path != "" ) return true;

  if (t.latest_version) return false;

  switch(inputName)
  {
    case "publication":
    case "name":
    case "decrement":
    case "country":
    case "source":
    case "country":
      return true;
    default:
      return false;
  }
}

export function isCompValid(t:ExpectedTable)
{
  return (t && t.comp_report && t.comp_report[1] && t.comp_report[1].length == 0)
}

export function isTechValid(t:ExpectedTable)
{
  return (t && t.tech_report && t.tech_report.number_of_errors == 0)
}


export function isFuncValid(t:ExpectedTable)
{
  if ( t.func_report && t.func_report.controls ){
    for(let c of t.func_report.controls)
    {
      if ( c.error_number > 0 ) return false
    }

    return true
  }else return false
}

export function isFuncValidControl(t:ExpectedTable,control)
{
  return (t && t.func_report && t.func_report.loaded && control && control.error_number == 0)
}

export function isYearValid(year:any)
{
  if ( isNaN(year)) return true

  return ( Number(year) >= 1970 && Number(year) <= new Date().getFullYear())
}


function CheckString(str:string)
{
  return ( str && str != "" )
}

function CheckNumber(n:number)
{
  return !isNaN(n)
}

