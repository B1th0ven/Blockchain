import { TAB_TYPE } from "../enums/tab.enum";
import { ExpectedTable } from "./expected-table.model";

export class Tab{
  name:string
  type:TAB_TYPE
  entity:ExpectedTable
}
