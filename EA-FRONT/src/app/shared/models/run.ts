import {
  Decrement
} from "./decrement";
import {
  RunTable
} from "./run-table";
import {
  date
} from "./date";

export class Run {
  id: number
  name: string
  decrements: Decrement[]

  study: number
  dataset: string
  dimensions: any[]
  allDimensions: any[]
  age_of_birth_check: boolean = true
  exposure_metric: string = "Initial"
  attained_age: string = "Age Last Birthday"
  automatic_risk_amount: boolean = false
  count_analysis: boolean = true
  amount_analysis: boolean = true
  amount_analysis_value: string
  amount_capped: boolean = false
  amount_capped_value: number
  loss_analysis_ratio: boolean = false
  loss_analysis_ratio_value: string
  ibnr_method: string = ""
  run_description: string
  creation_date: date
  createdBy:any
  status: string
  ibnr_amount_path: string
  ibnr_allocation_path: string
  ibnr_udf_path: string

  rating_adjustement: string = "none"
  joint_life: string = "1"

  is_cloned: boolean = false

  masterRunQX : boolean = false
  masterRunIX : boolean = false
  masterRunWX : boolean = false
  masterRunIXQX : boolean = false
  retainedRun : boolean = false

  //Variables For Rules Check
  is_amount_risk_insurer: boolean = false
  is_amount_event_insurer: boolean = false
  is_amount_risk_reinsurer: boolean = false
  is_amount_event_reinsurer: boolean = false
  is_joint_type: boolean = false
  is_annual_premium_reinsurer: boolean = false
  is_event_amount_reinsurer: boolean = false
  is_annual_premium_insurer: boolean = false
  is_event_amount_insurer: boolean = false
  is_date_of_commencement: boolean = true
  is_event_premium_insurer : boolean = false
  is_acc_risk_amount_insurer: boolean = false
  is_acc_risk_amount_reinsur: boolean = false

  isDateOfCommencement : boolean = false
  isDateOfBirth : boolean = false
  isRatingAdjustmentNone : boolean = false
  isAgeAtCommencement: boolean = false;
  isAccRiskAmountInsurer: boolean = true
  isAccRiskAmountReinsur: boolean = true
  grayedRatingAdjustment: boolean = false
  isEventAmountInsurerValue : boolean = true
  isAnnualPremiumInsurerValue : boolean = true
  isRiskAmountInsurerValue : boolean = true
  isEventAmountReinsurerValue : boolean = true
  isRiskAmountReinsurerValue : boolean = true
  isAnnualPremiumReinsurerValue : boolean = true
  isTempExtraValues : boolean = true

  running: boolean = false
  run_calc = null
  canSaveRun: boolean = false

  decrementLimit: number
  constructor(name, studyId) {
    this.name = name
    this.study = studyId;

    this.decrements = this.decrements || new Array < Decrement > ()

    let d : Date = new Date();
    let dddd : string = date.getCurrentDate(d)
    this.creation_date = new date(dddd.split("/")[2],dddd.split("/")[1],dddd.split("/")[0])

    this.updateDecLimit()
    this.status = ""
  }

  public updateDecLimit() {
    if (this.isInitial()) {
      this.decrementLimit = Run.DECREMENT_MIN
    } else
      this.decrementLimit = Run.DECREMENT_MAX
  }

  public isInitial = (): boolean => {
    return (this.exposure_metric == "Central")
  }

  public setDimensionCheckers(dimensions, datasets, datasetID) {
      let a = false
      let b = false
      let c = false
    if (!Array.isArray(datasets)) return
    let d;
    if (datasetID)
      d = datasets.find(d => d.id == datasetID)
    if (!d) return

    let cols = []

    d.files.forEach(file => {
      cols = cols.concat(file.columns)
    });

    //console.log('header ------->',cols)
    this.age_of_birth_check = true
    if (Array.isArray(cols))
      cols.forEach((n: string, index) => {
        switch (n.toUpperCase().trim()) {
          case "RISK_AMOUNT_INSURER":
            {
              this.is_amount_risk_insurer = true;
              break;
            }
          case "EVENT_AMOUNT_INSURER":
            {
              this.is_amount_event_insurer = true;
              this.is_event_amount_insurer = true;
              break;
            }
          case "RISK_AMOUNT_REINSURER":
            {
              this.is_amount_risk_reinsurer = true;
              break;
            }
          case "EVENT_AMOUNT_REINSURER":
            {
              this.is_amount_event_reinsurer = true;
              this.is_event_amount_reinsurer = true;
              break;
            }
          case "DATE_OF_BIRTH":
            {
              this.age_of_birth_check = false;
              this.isDateOfBirth = true;
              break;
            }
          case "JOINT_LIFE_TYPE":
            {
              this.is_joint_type = true;
              break;
            }
          case "ANNUAL_PREMIUM_REINSURER":
            {
              this.is_annual_premium_reinsurer = true;
              break;
            }
          case "ANNUAL_PREMIUM_INSURER":
            {
              this.is_annual_premium_insurer = true;
              break;
            }
            case "DATE_OF_COMMENCEMENT":
            {
              this.is_date_of_commencement = false;
              this.isDateOfCommencement = true;
              c = true
              break;
            }
            case "EVENT_PREMIUM_INSURER" : 
            {
              this.is_event_premium_insurer = true;
              break;
            }
            case "TEMP_MULT_EXTRA_RATING_TERM_1":
            case "TEMP_ADD_EXTRA_RATING_1":
            case "TEMP_ADD_EXTRA_RATING_TERM_1":
            case "TEMP_MULT_EXTRA_RATING_1":
            case "TEMP_MULT_EXTRA_RATING_2":
            case "TEMP_MULT_EXTRA_RATING_TERM_2":
            case "TEMP_ADD_EXTRA_RATING_2":
            case "TEMP_ADD_EXTRA_RATING_TERM_2":
            {
              if( !this.is_date_of_commencement) {
                this.isRatingAdjustmentNone = false;
                this.rating_adjustement = "none";
                this.grayedRatingAdjustment = true;
              }
              break;
            }
            case "AGE_AT_COMMENCEMENT":
            {
              a = true
              break;

            }
            case "AGE_AT_COMMENCEMENT_DEFINITION":
            {
              b = true
              break;
            }   
            case "ACCELERATION_RISK_AMOUNT_REINSUR":
            {
              this.is_acc_risk_amount_reinsur = true
              break;
            }   
            case "ACCELERATION_RISK_AMOUNT_INSURER": 
            {
              this.is_acc_risk_amount_insurer = true
              break;
            }     
           
        }
      });

      //console.log("a-->",a,"b-->",b,"c-->",c)

      if(a && b && c) this.isAgeAtCommencement = true
      
    if (!this.is_joint_type) this.joint_life = null;

    if (this.is_amount_risk_insurer && this.is_amount_event_insurer && this.isEventAmountInsurerValue && this.isRiskAmountInsurerValue)
      this.amount_analysis_value = "Insurer"
    else
      this.amount_analysis_value = "Reinsurer"

    if ((!this.is_amount_risk_insurer || !this.is_amount_event_insurer || !this.isEventAmountInsurerValue || !this.isRiskAmountInsurerValue) 
        && (!this.is_amount_risk_reinsurer || !this.is_amount_event_reinsurer || !this.isEventAmountReinsurerValue || ! this.isRiskAmountReinsurerValue)) {
      this.amount_analysis = false;
      this.amount_analysis_value = null
    }
  }

  public clone(): Run {
    let cloneObj = new Run("Run", this.id);
    cloneObj.decrements = []
    this.decrements.forEach(s => cloneObj.decrements.push(s.clone()))
    cloneObj.study = this.study
    cloneObj.dataset = this.dataset
    cloneObj.dimensions = []
    this.dimensions.forEach(s => cloneObj.dimensions.push(s))
    cloneObj.allDimensions = this.allDimensions
    cloneObj.age_of_birth_check = this.age_of_birth_check
    cloneObj.exposure_metric = this.exposure_metric
    cloneObj.attained_age = this.attained_age
    cloneObj.automatic_risk_amount = this.automatic_risk_amount
    cloneObj.count_analysis = this.count_analysis
    cloneObj.amount_analysis = this.amount_analysis
    cloneObj.amount_analysis_value = this.amount_analysis_value
    cloneObj.amount_capped = this.amount_capped
    cloneObj.amount_capped_value = this.amount_capped_value
    cloneObj.loss_analysis_ratio = this.loss_analysis_ratio
    cloneObj.loss_analysis_ratio_value = this.loss_analysis_ratio_value
    cloneObj.ibnr_method = this.ibnr_method
    cloneObj.run_description = this.run_description
    cloneObj.status = this.status
    cloneObj.ibnr_amount_path = this.ibnr_amount_path
    cloneObj.ibnr_allocation_path = this.ibnr_allocation_path
    cloneObj.ibnr_udf_path = this.ibnr_udf_path
    cloneObj.is_amount_risk_insurer = this.is_amount_risk_insurer
    cloneObj.is_amount_event_insurer = this.is_amount_event_insurer
    cloneObj.is_amount_risk_reinsurer = this.is_amount_risk_reinsurer
    cloneObj.is_amount_event_reinsurer = this.is_amount_event_reinsurer
    cloneObj.running = false
    cloneObj.rating_adjustement = this.rating_adjustement
    cloneObj.run_calc = null
    cloneObj.decrementLimit = this.decrementLimit

    cloneObj.is_joint_type = this.is_joint_type
    cloneObj.is_annual_premium_reinsurer = this.is_annual_premium_reinsurer
    cloneObj.is_event_amount_reinsurer = this.is_event_amount_reinsurer
    cloneObj.is_annual_premium_insurer = this.is_annual_premium_insurer
    cloneObj.is_event_amount_insurer = this.is_event_amount_insurer
    cloneObj.is_date_of_commencement = this.is_date_of_commencement
    cloneObj.is_event_premium_insurer = this.is_event_premium_insurer
    cloneObj.isDateOfCommencement = this.isDateOfCommencement
    cloneObj.isDateOfBirth = this.isDateOfBirth
    cloneObj.isRatingAdjustmentNone = this.isRatingAdjustmentNone
    cloneObj.isEventAmountInsurerValue = this.isEventAmountInsurerValue
    cloneObj.isAnnualPremiumInsurerValue = this.isAnnualPremiumInsurerValue
    cloneObj.isRiskAmountInsurerValue = this.isRiskAmountInsurerValue
    cloneObj.isEventAmountReinsurerValue = this.isEventAmountReinsurerValue
    cloneObj.isRiskAmountReinsurerValue = this.isRiskAmountReinsurerValue
    cloneObj.isAnnualPremiumReinsurerValue = this.isAnnualPremiumReinsurerValue
    cloneObj.isTempExtraValues = this.isTempExtraValues
    cloneObj.isAgeAtCommencement = this.isAgeAtCommencement
    cloneObj.is_acc_risk_amount_insurer = this.is_acc_risk_amount_insurer
    cloneObj.is_acc_risk_amount_reinsur = this.is_acc_risk_amount_reinsur

    cloneObj.is_cloned = true
    return cloneObj;
  }

  public init() {
    this.decrements = []
    this.age_of_birth_check = true
    this.exposure_metric = "Initial"
    this.attained_age = "Age Last Birthday"
    this.automatic_risk_amount = false
    this.count_analysis = true
    this.amount_analysis = true
    this.amount_capped = false
    this.loss_analysis_ratio = false
    this.loss_analysis_ratio_value = ""
    this.ibnr_method = ""
    this.ibnr_allocation_path = null
    this.ibnr_amount_path = null
    this.ibnr_udf_path = null
    this.run_description = ""
    this.amount_capped_value = 0
    this.is_cloned = false
    this.is_amount_risk_insurer = false
    this.is_amount_event_insurer = false
    this.is_amount_risk_reinsurer = false
    this.is_amount_event_reinsurer = false
    this.is_acc_risk_amount_insurer = false
    this.is_acc_risk_amount_reinsur = false
    this.running = false
    this.run_calc = null
    this.isAgeAtCommencement = false

  }

  static NumberOfRuns: number = 0

  public static readonly DECREMENT_MAX = 3
  public static readonly DECREMENT_MIN = 3
}
