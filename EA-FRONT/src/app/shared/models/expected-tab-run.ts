export class ExpTabRun {
    id_base:number;
    id_trend:number;
    id_adjustment:number;
    basis : number;
    cansaveexp: boolean = false
    cansaveexptrend: boolean 
    cansaveexpadj: boolean 
    ExprReportBase: any[]
    ExprReportAdj: any[]
    ExprReporTTrend: any[] 
    id_calibration : number
    ExprReportCalibration : any[]
    cansavecalibration : boolean = false

    constructor(base?,trend?,adj?,rpb?,rpa?,rpt?){
        this.id_base = base;
        this.id_trend = trend;
        this.id_adjustment = adj;
        this.ExprReportBase = rpb;
        this.ExprReporTTrend = rpt;
        this.ExprReportAdj = rpa;

    }

    clone(){
        let obj = new ExpTabRun(this.id_base,this.id_trend,this.id_adjustment,this.ExprReportBase,this.ExprReportAdj,this.ExprReporTTrend)
        return obj
    }
}