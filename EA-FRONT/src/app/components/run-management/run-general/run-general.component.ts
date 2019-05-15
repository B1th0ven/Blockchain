import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter
} from '@angular/core';
import {
  Run
} from '../../../shared/models/run';
import {
  FormValidatorService
} from '../../../services/form-validator.service';
import {
  Dataset
} from '../../../shared/models/dataset';
import {
  NgbModal
} from '@ng-bootstrap/ng-bootstrap';
import { IbnrAmountComponent } from '../ibnr-amount/ibnr-amount.component';
import { NgxPermissionsService } from '../../../../../node_modules/ngx-permissions';
import { SCREEN,HoverOverService } from '../../hover-over/hover-over.service';

@Component({
  selector: 'app-run-general',
  templateUrl: './run-general.component.html',
  styleUrls: ['./run-general.component.scss']
})
export class RunGeneralComponent implements OnInit {

  constructor(private permissionsService: NgxPermissionsService,private modalService: NgbModal, public formValidation: FormValidatorService,public hs:HoverOverService) {}
  dataModel: any[];
  options: any[];

  @Input() run: Run
  @Input() tab: number
  @Input() screen: number

  @Input() datasets: Dataset[]
  @Input() dimensions: string[]

  @Input() canEditRun : boolean;
  @Input() canEditPerm : boolean;

  selectedItems = [];
  dropdownSettings = {};

  component_number:number

  selected_dataset:string;

  screen_name = SCREEN.RUN

  @Output() headerCheck:EventEmitter<boolean> = new EventEmitter()

  ngOnInit() {
    console.log('---------------->',this.run.dimensions)
    this.selected_dataset = null
      if(!this.run.is_cloned)
      {
        this.run.allDimensions = []
       this.onDatasetChange(this.run.dataset)
      }
      this.component_number = RunGeneralComponent.NUMBER++;

      this.dataModel = [];
      this.options = [];

      this.dropdownSettings = {
      singleSelection: false,
      text: "",
      selectAllText: 'Select All',
      unSelectAllText: 'Unselect All',
      enableSearchFilter: true,
      classes: "myclass custom-class",
      badgeShowLimit: 0,
      disabled:!this.editable()
    };



  }

  editable() {
    return this.canEditPerm && this.canEditRun
  }

  attachIbnrFiles(inbrs:any[]) {
    let udf = inbrs.find(i=>i.type=="udf")
    let amount = inbrs.find(i=>i.type=="amount")
    let allocation = inbrs.find(i=>i.type=="allocation")

    this.run.ibnr_udf_path = (udf && udf.file )? udf.file.path : null
    this.run.ibnr_amount_path = (amount && amount.file )? amount.file.path : null
    this.run.ibnr_allocation_path = (allocation && allocation.file )? allocation.file.path : null
  }

  onItemSelect(item: any) {}
  OnItemDeSelect(item: any) {

  }
  onSelectAll(items: any) {}
  onDeSelectAll(items: any) {}

  amount_analysis_change() {
    if (!this.run.amount_analysis) {
      this.run.amount_analysis_value = null;
      this.run.amount_capped = false;
      this.run.automatic_risk_amount = false
      this.amount_capped_change();
    }else
    {
      this.run.amount_analysis_value = this.updateRadioButtons()
    }

  }

  amount_capped_change() {
    if (!this.run.amount_capped)
      this.run.amount_capped_value = null;
  }

  loss_analysis_ratio_change() {
    if (!this.run.loss_analysis_ratio)
      this.run.loss_analysis_ratio_value = null;
    else
    {
      if ( !this.isLossRatiDisabledInsurer() )
        this.run.loss_analysis_ratio_value = "Insurer"
      else if ( !this.isLossRatiDisabledRinsurer())
        this.run.loss_analysis_ratio_value = "Reinsurer"
    }


  }

  CheckHeader(e){
    if(this.run.dataset){
      let ds = this.filterDatasets(this.datasets).find(d => d.id == this.run.dataset)
      let policy_file = ds.files.find(f => f.type == "policy")
      let headers = policy_file.header
      let  bool = headers.toLocaleUpperCase().includes("TEMP_MULT_EXTRA_RATING_TERM_1") ||  headers.toLocaleUpperCase().includes("TEMP_ADD_EXTRA_RATING_TERM_1")
                  || headers.toLocaleUpperCase().includes("TEMP_MULT_EXTRA_RATING_TERM_2") ||  headers.toLocaleUpperCase().includes("TEMP_ADD_EXTRA_RATING_TERM_2")
      let bool2 = e == "adjust expected"
      this.headerCheck.emit(bool&&bool2)
      } else this.headerCheck.emit(false)
  }

  isRiskAmountDisabled(){
    if(this.run.dataset){
      let ds = this.filterDatasets(this.datasets).find(d => d.id == this.run.dataset)
      let policy_file = ds.files.find(f => f.type == "policy")
      let headers = policy_file.header

      let  bool = headers.toLocaleUpperCase().includes("BENEFIT_CHANGE_RATE_ANNUAL") 
                    && headers.toLocaleUpperCase().includes("BENEFIT_CHANGE_RATE_TYPE") 
                    && headers.toLocaleUpperCase().includes("BENEFIT_CHANGE_FREQUENCY")
                    && headers.toLocaleUpperCase().includes("DATE_OF_COMMENCEMENT")
        return !bool
      } else return false
  }

  clickRating(val){
    this.CheckHeader(val)
  }

  oldValue
  onDatasetChange(value,oldValue?) {
    this.run.is_annual_premium_insurer = false
    this.run.is_annual_premium_reinsurer = false
    this.run.is_amount_risk_insurer = false
    this.run.is_amount_event_insurer = false
    this.run.is_amount_risk_reinsurer = false
    this.run.is_amount_event_reinsurer = false
    this.run.amount_analysis = true;
    this.run.is_joint_type = false;
    this.initGreyOutRules()
    this.run.allDimensions = []
    
    if(this.oldValue) // I added this condition because on init we should not empty the dimensions
    this.run.dimensions = []


    if ( this.oldValue && value != this.oldValue )
      this.run.decrements = []

    this.oldValue = this.run.dataset
    this.getDimensions(this.run.dataset)
    this.CheckHeader(this.run.rating_adjustement)
    this.greyOutRules(this.run.dataset)    
    this.run.amount_analysis_value = this.updateRadioButtons()
    if(this.run.amount_analysis_value == null) this.run.amount_analysis = false // don't delete this line !!!
    console.log("value ----------------->",this.updateRadioButtons())
  }

  initGreyOutRules(){
    this.run.isEventAmountInsurerValue= true
    this.run.isRiskAmountInsurerValue = true
    this.run.isEventAmountReinsurerValue = true
    this.run.isRiskAmountReinsurerValue = true
    this.run.isAnnualPremiumInsurerValue = true
    this.run.isAnnualPremiumReinsurerValue = true
    this.run.isAccRiskAmountInsurer = true
    this.run.isAccRiskAmountReinsur = true

  }

  greyOutRules(value) {
    if (!Array.isArray(this.datasets)) return
    let d : Dataset;
    if (value)
      d = this.datasets.find(d => d.id == value)
    if (!d) return

    let controlType = d.funcReport.controlResultsList.find(c => c.control == "Missing Values Check")
    if(controlType) {
      console.log(controlType);
      if(controlType.affectedColumns && controlType.affectedColumns.length > 0) {
        if(controlType.affectedColumns.find(a => a.name == "event_amount_insurer/type_of_event")) {
          this.run.isEventAmountInsurerValue= false
        }
        if(controlType.affectedColumns.find(a => a.name == "risk_amount_insurer/risk_amount_insurer")) {
          this.run.isRiskAmountInsurerValue = false
        }
        if(controlType.affectedColumns.find(a => a.name == "event_amount_reinsurer/type_of_event")) {
          this.run.isEventAmountReinsurerValue = false
        }
        if(controlType.affectedColumns.find(a => a.name == "risk_amount_reinsurer/risk_amount_reinsurer")) {
          this.run.isRiskAmountReinsurerValue = false
        }
        if(controlType.affectedColumns.find(a => a.name == "annual_premium_insurer/annual_premium_insurer")) {
          this.run.isAnnualPremiumInsurerValue = false
        }
        if(controlType.affectedColumns.find(a => a.name == "annual_premium_reinsurer/annual_premium_reinsurer")) {
          this.run.isAnnualPremiumReinsurerValue = false
        }
    /*    if(controlType.affectedColumns.find(a => (a.name == "temp_mult_extra_rating_1" ||
                                                  a.name == "temp_mult_extra_rating_term_1" ||
                                                  a.name == "temp_add_extra_rating_1" ||
                                                  a.name == "temp_add_extra_rating_term_1" ||
                                                  a.name == "temp_mult_extra_rating_2" ||
                                                  a.name == "temp_mult_extra_rating_term_2" ||
                                                  a.name == "temp_add_extra_rating_2" ||
                                                  a.name == "temp_add_extra_rating_term_2" ))) {
          this.run.isTempExtraValues = false
                                                  } */

        if(controlType.affectedColumns.find(a => a.name == "acceleration_risk_amount_insurer/acceleration_risk_type")) {
          this.run.isAccRiskAmountInsurer = false
        }
        if(controlType.affectedColumns.find(a => a.name == "acceleration_risk_amount_reinsur/acceleration_risk_type")) {
          this.run.isAccRiskAmountReinsur = false
        }
      }
    }
    console.log(this.run);
  }
  

  updateRadioButtons() {
    //By Amount Analysis Radio Button
    if(!this.isByAmountBasisDisabledInsurer()) return "Insurer"
    else if(!this.isByAmountBasisDisabledReinsurer()) return "Reinsurer"
    else return null
  }

  isByAmountBasisDisabledInsurer() {
    if(!this.editable()) return true
    if(!this.run.is_amount_risk_insurer||!this.run.is_amount_event_insurer) return true
    else {
      if(!this.run.is_acc_risk_amount_insurer) {
        if(!this.run.isRiskAmountInsurerValue || !this.run.isEventAmountInsurerValue) return true
      } else {
        if(!this.run.isRiskAmountInsurerValue || !this.run.isEventAmountInsurerValue || !this.run.isAccRiskAmountInsurer) return true
      }
    }
    return false
   // return !this.editable()||!this.run.is_amount_risk_insurer||!this.run.isRiskAmountInsurerValue || !this.run.is_amount_event_insurer
  }

  isByAmountBasisDisabledReinsurer() {
    if(!this.editable()) return true
    if(!this.run.is_amount_risk_reinsurer||!this.run.is_amount_event_reinsurer) return true
    else {
      if(!this.run.is_acc_risk_amount_reinsur) {
        if(!this.run.isRiskAmountReinsurerValue || !this.run.isEventAmountReinsurerValue) return true
      } else {
        if(!this.run.isRiskAmountReinsurerValue || !this.run.isEventAmountReinsurerValue || !this.run.isAccRiskAmountReinsur) return true
      }
    }
    return false
    //return !this.editable()||!this.run.is_amount_risk_reinsurer || !this.run.is_amount_event_reinsurer|| !this.run.isRiskAmountReinsurerValue
  }

  isByAmountAnalysisDisabled() {
    return this.isByAmountBasisDisabledInsurer()&&this.isByAmountBasisDisabledReinsurer()
  }

  isLossRatiDisabledInsurer() {
    if(!this.editable()) return true
    if(!this.run.is_annual_premium_insurer || !this.run.is_event_amount_insurer) return true
    else {
       if(!this.run.isEventAmountInsurerValue  || !this.run.isAnnualPremiumInsurerValue) return true
    }
    return false
    //return  !this.run.is_annual_premium_insurer || !this.run.is_event_amount_insurer || !this.run.isEventAmountInsurerValue || !this.run.isAnnualPremiumInsurerValue||!this.editable()
  }

  isLossRatiDisabledRinsurer() {
    if(!this.editable()) return true
    if(!this.run.is_annual_premium_reinsurer || !this.run.is_event_amount_reinsurer) return true
    else {
       if(!this.run.isEventAmountReinsurerValue || !this.run.isAnnualPremiumReinsurerValue) return true
    }
    return false
    //return !this.run.is_annual_premium_reinsurer || !this.run.is_event_amount_reinsurer || !this.run.isEventAmountReinsurerValue || !this.run.isAnnualPremiumReinsurerValue||!this.editable()
  }

  isLossRatioDisabled() {
    return this.isLossRatiDisabledInsurer()&&this.isLossRatiDisabledRinsurer()
  }

  changeDS(content,newVal) {

      // if(this.run.id) {
        if(this.oldValue && this.oldValue != "") {
        this.modalService.open(content);
      } else
      {
        this.selected_dataset = this.run.dataset
        this.run.init()
        this.onDatasetChange(this.run.dataset)
      }


  }



  confirmDsChange(close){
    this.selected_dataset = this.run.dataset
    this.run.init()
    this.onDatasetChange(this.run.dataset)
    close('Close click')
  }

  cancelDsChange(close){
    this.run.dataset = this.selected_dataset
    close('Close click')

  }

  getDimensions(value) {

    if (!Array.isArray(this.datasets)) return
    let d;
    if (value)
      d = this.datasets.find(d => d.id == value)
    if (!d) return



    let cols = []
    //let cols = d.files.find(f => f.type == "policy").columns
    d.files.forEach(file => {
      cols = cols.concat(file.columns)
    });

    let dim = this.dimensions

    let id = 0

    this.run.age_of_birth_check = true
    if (Array.isArray(cols))
      cols.forEach((n: string, index) => {
        if (dim.indexOf(n.toLocaleLowerCase().trim()) !== -1) {
          let obj = {
            id: id++,
            itemName: n
          };
          this.run.allDimensions.push(obj)
        
        }
      });
     if (!this.run.dimensions || this.run.dimensions.length==0) {
      console.log("dims------------------->",this.run.dimensions)

        this.run.dimensions = []
        this.run.allDimensions.forEach(dim => this.run.dimensions.push(dim))
        console.log("these are run dimensions now :: check all the run :: " + this.run);
        
      } 
     

    this.run.setDimensionCheckers(this.dimensions,this.datasets,value)
  }

  show() {

  }

  open(ibnrAmount) {
    this.modalService.open(ibnrAmount, {
      size: "lg"
    });

  }

  filterDatasets(datasets: Dataset[]) {
    if (!datasets) return null;
    return datasets.filter(d => d.hasReport())
  }

  isDatasetSelected(run): boolean {
    return (run.dataset && run.dataset != "")
  }

  onIbnrMethodChanged(method)
  {
    switch(method)
    {
      case "manual":
        this.run.ibnr_allocation_path=null
        this.run.ibnr_amount_path=null;
        break;
      case "amount":
        this.run.ibnr_udf_path=null;
        break;
      default:
        this.run.ibnr_allocation_path=null
        this.run.ibnr_amount_path=null
        this.run.ibnr_udf_path=null
    }
  }
  
  isUserIbnrReader(){
    if(this.permissionsService.getPermission("RUN_IBNR_READER"))
    return true
    else return false
  }

  canUserUploadIbnr(){
    if(this.permissionsService.getPermission("RUN_IBNR_UPLOADER"))
    return true
    else return false
  }

  static NUMBER=0;

}
