import { Observable } from "rxjs/Observable";

export class Control{
    control:string
    affectedColumns:Array<{}>
     running:boolean = false
     done:boolean = false
     valid:string
     type:string
     number:number
     name:string
     identifier:string
     path:string
     files:[string]
     message:string
     progress:number = 0
     status:string
     time:number
     errors:number = 0
     errorsDetailed: Object[]
     category:string
     description:string
     controlColumns:Array<string>
     examples:string[]
        total:number = 0
    notExecuted: any
    order: number;
    orderdisplayed:number;
    canceled: boolean;

    standardControl = (files?): Observable<any> =>{
        let obs = new Observable(observer=>{
            observer.next({
                "total":0,
                "time":0,
            })
            observer.complete()
        })

        return obs
    }

    handler:Function = this.standardControl

    constructor(controlInfo){
        this.controlColumns = controlInfo.controlColumns
        this.number = ++Control.numberOfInstances
        this.name = controlInfo.name
        this.category = controlInfo.category
        this.description = controlInfo.description
        this.files = controlInfo.files
        this.path = controlInfo.path
        this.handler =  controlInfo.handler || this.handler
        this.identifier = controlInfo.identifier
        this.type = controlInfo.type
        this.order = controlInfo.order
    }

    save(){
        //TODO
        //Save in local storage
    }
    static numberOfInstances = 0;
}
