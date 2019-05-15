import { decrements } from "../../modules/table-library/components/table-definition/table-form-values";

let engines =[
  {
    id:1,
    name:"Lump Sum",
    decrements:[
      "qx","wx","ix","ix+qx"
    ]
  }
]

export function getEngineDecrementsByName(name:string)
{
  let en = engines.find(e=>e.name==name)
  return (en)?en.decrements:[];
}

export function getEngineDecrementsById(id:number)
{
  let en = engines.find(e=>e.id==id)
  return (en)?en.decrements:[];
}
