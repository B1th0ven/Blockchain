//  tslint:disable: max-line-length
import { Injectable } from '@angular/core';
import { Control } from '../shared/models/control';
@Injectable()
export class DatasetControlDataService {

  constructor() { }
  InitFormatControl() {
    var formatControl = new Control({
      identifier: 'formatControl',
      name: 'Format Control',
      files: ['policy'],
      category: 'Blocking',
      order: 1,
      description: 'No missing values for compulsory variables. All dates must follow format dd/mm/yyyy. All numbers must follow defined format (point or comma decimal separator, no thousands separator) and specified range. All codes must follow referential'
    });
    formatControl.running = true;
    formatControl.canceled = false;
    return formatControl;
  }
  InitProductFormatControl(){
    var productFormatControl = new Control({
      name: 'Format Control',
      identifier: 'productFormatControl',
      files: ['product'],
      category: 'Blocking',
      order: 2,
      description: 'No missing values for compulsory variables. All dates must follow format dd/mm/yyyy. All numbers must follow defined format (point or comma decimal separator, no thousands separator) and specified range. All codes must follow referential'
    });
    productFormatControl.running = true;
    productFormatControl.canceled = false;
    return productFormatControl;
  }
  
  InitControls() {
    var Controls = [
      new Control({
        identifier: 'Date format',
        name: 'Date format',
        type: 'Date',
        files: ['policy']
      }),
      new Control({
        identifier: 'Numeric format',
        name: 'Numeric format',
        type: 'Numeric',
        files: ['policy']
      }),
      new Control({
        identifier: 'Code format',
        name: 'Code format',
        type: 'Code',
        files: ['policy']
      }),
      new Control({
        identifier: 'RefData format',
        name: 'RefData format',
        type: 'RefData',
        files: ['policy']
      }), new Control({
        identifier: 'Header format',
        name: 'Header format',
        type: 'Header',
        files: ['policy']
      })

    ];
    return Controls;
  }
  InitProductControls(){
    var ProductControls = [
      new Control({
        identifier: 'Date format',
        name: 'Date format',
        type: 'Date',
        files: ['policy']
      }),
      new Control({
        identifier: 'Numeric format',
        name: 'Numeric format',
        type: 'Numeric',
        files: ['policy']
      }),
      new Control({
        identifier: 'Code format',
        name: 'Code format',
        type: 'Code',
        files: ['policy']
      }),
      new Control({
        identifier: 'RefData format',
        name: 'RefData format',
        type: 'RefData',
        files: ['policy']
      }),
      new Control({
        identifier: 'Header format',
        name: 'Header format',
        type: 'Header',
        files: ['policy']
      })

    ];
    return ProductControls;
  }
  InitFuncControls(){
    var FuncControls = [
      new Control({
        controlColumns: ['Status_Begin_Current_Condition', 'Status_End_Current_Condition'],
        identifier: 'status at begin current condition & status at end current condition',
        name: 'Status Consistency',
        category: 'Warning',
        description: 'The status at the end of a period should equal the status at the begin of the following period',
        files: ['policy'],
        order: 3
      }),
      new Control({
        controlColumns: ['Date_of_Birth', 'Date_of_Commencement'],
        identifier: 'date of birth & date of commencement',
        name: 'Birth <= Commencement',
        category: 'Warning',
        description: 'The date of birth of the insured should be prior the date of Commencement of the policy',
        files: ['policy'],
        order: 4
      }),
      new Control({
        controlColumns: ['Risk_Amount_Reinsurer', 'Risk_Amount_Insurer'],
        identifier: 'risk amount reinsurer & risk amount insurer',
        name: 'Risk Amount Reinsurer <= Risk Amount Insurer',
        category: 'Warning',
        description: 'The Risk Amount covered by the reinsurer should be lower than Risk Amount covered by the insurer',
        files: ['policy'],
        order: 5
      }),
      // 6
      new Control({
        controlColumns: ['Status_Begin_Current_Condition', 'Status_End_Current_Condition', 'Type_of_Event', 'Date_of_Event_Incurred'],
        identifier: 'date of event incurred & type of event',
        name: 'Event Existence',
        category: 'Blocking',
        description: 'For each status change, all compulsory event variables must be completed',
        files: ['policy'],
        order: 6
      }),
      // NEW
      // 7
      new Control({
        controlColumns: ['Date_of_Commencement', 'Date_of_Event_Incurred'],
        identifier: 'date of commencement & date of event incurred',
        name: 'Commencement <= Event',
        category: 'Warning',
        description: 'Date of Commencement of the Policy should be prior the Date of Event Incurred',
        files: ['policy'],
        order: 7
      }),
      // 8
      new Control({
        controlColumns: ['Date_of_Begin_Current_Condition', 'Date_of_End_Current_Condition'],
        identifier: 'date of begin current condition & date of end current condition',
        name: 'Order of status dates',
        category: 'Blocking',
        description: 'The date of Begin Current Condition must be prior the date of End Current Condition',
        files: ['policy'],
        order: 8
      }),
      // 9
      new Control({
        controlColumns: ['Date_of_Commencement', 'Benefit_End_Date'],
        identifier: 'date of commencement & benefit end date',
        name: 'Commencement <= Benefit End',
        category: 'Warning',
        description: 'Date of Commencement of the policy should be prior Benefit End Date',
        files: ['policy'],
        order: 9
      }),
      // 10
      new Control({
        controlColumns: ['Date_of_Commencement', 'Date_of_Birth', 'Age_at_Commencement', 'Age_at_Commencement_definition'],
        identifier: 'date of commencement = age at commencement - date of birth',
        name: 'Coherence between age at commencement and birth',
        category: 'Warning',
        description: 'The Age of Commencement should be coherent with the Date of Commencement of the Policy and the Date of Birth',
        files: ['policy', 'product'],
        order: 10
      }),
      // 11
      new Control({
        controlColumns: ['Date_of_Commencement', 'Date_of_Begin_Current_Condition'],
        identifier: 'date of commencement & date of begin current condition',
        name: 'Commencement <= Current Condition',
        category: 'Warning',
        description: 'The date of commencement of the policy should be prior the date of begin current condition',
        files: ['policy'],
        order: 11
      }),

      // 12
      new Control({
        controlColumns: ['Date_of_Begin_Current_Condition', 'Date_of_End_Current_Condition'],
        identifier: 'overlap check',
        name: 'Status date overlap',
        category: 'Blocking',
        description: 'Each combination of Policy, Insured, Benefit and Retro Legal Entity is detailled by period. There must not be overlap with status date: the end date of period must be strictly prior the start date of the following period',
        files: ['policy'],
        order: 12
      }),
      // 13
      new Control({
        controlColumns: ['Status_Begin_Current_Condition', 'Status_End_Current_Condition', 'Type_of_Event'],
        identifier: 'Type of Event & Status at End Current Condition',
        name: 'Status Coherent with Event Type',
        category: 'Warning',
        description: 'The type of event should be coherent with the status begin/end current condition',
        files: ['policy'],
        order: 13
      }),
      // 14
      new Control({
        controlColumns: ['Status_Begin_Current_Condition'],
        identifier: 'exposure termination',
        name: 'No exposure after termination',
        category: 'Warning',
        description: 'For each combination of Policy, Insured, Benefit and Retro Legal Entity, there should not be exposure that follows a terminating status',
        files: ['policy'],
        order: 14
      }),
      // 15
      new Control({
        controlColumns: ['Date_of_Event_Incurred', 'Benefit_End_Date'],
        identifier: 'date of event incurred & benefit end date',
        name: 'Event Incurred <= Benefit End',
        category: 'Warning',
        description: 'Date of Event Incurred should be prior Benefit End Date',
        files: ['policy'],
        order: 15
      }),
      // 16
      new Control({
        controlColumns: ['Benefit_End_Date', 'Date_of_End_Current_Condition'],
        identifier: 'date of end current condition & benefit end date',
        name: 'Exposure <= Benefit End',
        category: 'Warning',
        description: 'Date of End Current Condition should be prior the Benefit End Date',
        files: ['policy'],
        order: 16
      }),
      // 17  'This is just\na simple sentence'.
      new Control({
        controlColumns: ['Start_of_Observation_Period', 'Benefit_End_Date'],
        identifier: 'start of observation period <= benefit end date',
        name: 'Observation start <= Benefit End',
        category: 'Warning',
        description: 'Start of observation period should be prior the benefit end date',
        files: ['policy'],
        order: 17
      }),
      // 18
      new Control({
        controlColumns: ['End_of_observation_Period', 'Date_of_End_Current_Condition'],
        identifier: 'date of end current condition <= end of observation period',
        name: 'Exposure <= Observation End',
        category: 'Warning',
        description: 'Date of End Current Condition should be prior the End of observation period',
        files: ['policy'],
        order: 18
      }),
      // 19
      new Control({
        controlColumns: ['Start_of_Observation_Period', 'date_of_event_incurred'],
        identifier: 'start of observation period <= date of event incurred',
        name: 'Observation Start <= Exposure',
        category: 'Warning',
        description: 'Start of observation period should be prior Date of Event Incurred',
        files: ['policy'],
        order: 19
      }),
      // 20
      new Control({
        controlColumns: ['Date_of_Event_Incurred', 'Date_of_Event_Notified', 'Date_of_Event_Settled', 'Date_of_Event_Paid'],
        identifier: 'date of event incurred & date of event notified & date of event settled & date of event paid',
        name: 'Event Incurred <= Notified <= Settled <= Paid',
        category: 'Warning',
        description: 'Date of Event Incurred should be prior Date of Event Notified that should be prior Date of Event Settled that should be prior Date of Event Paid',
        files: ['policy'],
        order: 20
      }),
      // 21
      new Control({
        controlColumns: ['Type_of_Event', 'Main_Risk_Type', 'Acceleration_Risk_Type', 'Life_ID'],
        identifier: 'claims existence',
        name: 'Claims Existence',
        category: 'Warning',
        description: 'In case that a person holds several policies at the same time, claims should occur on all his policies',
        files: ['policy'],
        order: 21
      }),
      // 22
      new Control({
        controlColumns: ['Status_End_Current_Condition', 'Date_of_End_Current_Condition', 'End_of_Observation_Period'],
        identifier: 'exposure end coherent with status',
        name: 'Exposure end coherent with status',
        category: 'Warning',
        description: 'Each combination of Policy, Insured, Benefit and Retro Legal Entity is detailled by period. For each combination:' +
          '- if the policy is ongoing, the last line should have a date of end current condition either empty or equal to end observation period' +
          '- if the policy is terminated, the last line should have a status of end current condition different from active (and claimant for annuity)',
        files: ['policy'],
        order: 22
      }),
      // 23
      new Control({
        controlColumns: ['Benefit_End_Date', 'Date_of_Birth', 'Benefit_Max_Age', 'Age_at_Commencement_Definition'],
        identifier: 'cover expiry date - date of birth <= cover expiry age',
        name: 'Coherence of Benefit Max Age with Benefit End Date',
        category: 'Warning',
        description: 'Age at Benefit End Date should be lower than the Benefit Max Age',
        files: ['policy'],
        order: 23
      }),
      // 24
      new Control({
        controlColumns: ['Risk_Amount_Insurer', 'Risk_Amount_Reinsurer', 'Event_Amount_Insurer', 'Event_Amount_Reinsurer', 'Type_of_Event', 'Settlement_decision', 'Expenses_included'],
        identifier: 'when death / withdrawal (lump sum), risk amount = event amount',
        name: 'When Death / Withdrawal (lump sum), Risk Amount = Event Amount',
        category: 'Warning',
        description: 'When the cause of the claim is death or withdrawal, the Event Amount Insurer (Reinsurer) should equal the Risk Amount Insurer (Reinsurer) or be smaller in case of settlement decision or be larger in case of expenses included',
        files: ['policy'],
        order: 24
      }),
      // 25
      new Control({
        controlColumns: ['Product_Start_Date', 'Date_of_Commencement', 'Product_End_Date'],
        identifier: 'product start date <= date of commencement <= product end date',
        name: 'Product Start Date <= Date of Commencement <= Product End Date',
        category: 'Warning',
        description: 'The start of the product should be prior the date of commencement of the policy that should be prior the end of the product',
        files: ['policy'],
        order: 25
      }),
      // 26
      // new Control({
      //   controlColumns: ['Risk_Amount_Insurer', 'Max_Face_Amount'],
      //   identifier: 'risk amount insurer <= max face amount',
      //   name: 'Risk Amount Insurer <= Max Face Amount',
      //   category: 'Warning',
      //   description: 'The Risk Amount Insurer should be lower than Maximum Face Amount',
      //   files: ['policy'],
      //   order: 26
      // }),
      // // 27
      // new Control({
      //   controlColumns: ['Risk_Amount_Reinsurer', 'Max_Face_Amount'],
      //   identifier: 'risk amount reinsurer <= max face amount',
      //   name: 'Risk Amount Reinsurer <= Max Face Amount',
      //   category: 'Warning',
      //   description: 'The Risk Amount Reinsurer should be lower than Maximum Face Amount',
      //   files: ['policy'],
      //   order: 27
      // }),
      // 28
      new Control({
        controlColumns: ['Risk_Amount_Insurer', 'Min_Face_Amount','Max_Face_Amount'],
        identifier: 'min face amount <= risk amount insurer',
        name: 'Min Face Amount <= Risk Amount Insurer <= Max Face Amount',
        category: 'Warning',
        description: 'Minimum Face amount should be lower than risk amount insurer that should be lower than Maximum Face Amount',
        files: ['policy'],
        order: 28
      }),
      // 29
      new Control({
        controlColumns: ['Risk_Amount_Reinsurer', 'Min_Face_Amount','Max_Face_Amount'],
        identifier: 'min face amount <= risk amount reinsurer',
        name: 'Min Face Amount <= Risk Amount Reinsurer <= Max Face Amount',
        category: 'Warning',
        description: 'Minimum Face amount should be lower than risk amount reinsurer that should be lower than Maximum Face Amount',
        files: ['policy'],
        order: 29
      }),
      // 30
      new Control({
        controlColumns: ['Age_at_Commencement', 'Min_Age_at_Commencement', 'Max_Age_at_Commencement'],
        identifier: 'min age at commencement <= age at commencement <= max age at commencement',
        name: 'Min Age at Commencement <= Age at Commencement <= Max Age at Commencement',
        category: 'Warning',
        description: 'The age at commencement of the policy should lower than maximum age at commencement of the policy and higher than minimum age at commencement of the policy',
        files: ['policy'],
        order: 30
      }),
      // 31
      new Control({
        controlColumns: ['Duplicated_Product_Id'],
        identifier: 'Unique ProductID Control',
        name: 'Unique Product ID',
        category: 'Blocking',
        description: 'Product ID must be unique in the Product file',
        files: ['product'],
        order: 31
      }),
      // 32
      new Control({
        controlColumns: ['Product_ID'],
        identifier: 'Product Id doesn\'t exists in product file',
        name: 'Missing Product ID',
        category: 'Blocking',
        description: 'Product ID in Policy file must be found in Product file',
        files: ['policy'],
        order: 32
      }),

      // 33
      new Control({
        controlColumns: ['Date_of_Event_Incurred', 'Date_of_End_Current_Condition'],
        identifier: 'Date of Event = Date of End Current Condition',
        name: 'Date of Event = Date of End Current Condition',
        category: 'Blocking',
        description: 'In case of Event, Date of Event Incurred must be equal to Date of End Current Condition',
        files: ['policy'],
        order: 33
      }),

      // 34
      new Control({
        controlColumns: ['Date_of_Birth', 'Date_of_Commencement', 'Risk_Amount_Insurer', 'Event_Amount_Insurer', 'Risk_Amount_Reinsurer', 'Event_Amount_Reinsurer', 'Annual_Premium_Insurer', 'Annual_Premium_Reinsurer',
          'Acceleration_Risk_Type', 'Acceleration_Risk_Amount_Insurer', 'Acceleration_Risk_Amount_Reinsur', 'Benefit_Change_Rate_Type', 'Benefit_Change_Rate', 'Benefit_Term', 'Benefit_Change_Frequency'],
        identifier: 'Missing Values Check',
        name: 'Missing Values',
        category: 'Warning',
        description: 'Certain date and numerical variables should not have missing values. For some variables, it should only be the case if another variable is completed.',
        files: ['policy'],
        order: 34
      }),
      // 35
      new Control({
        controlColumns: ['Type_of_Event',
          'Date_of_Event_Incurred',
          'Smoker_Status_Detailed',
          'Smoker_Status',
          'Benefit_Term_Years',
          'Benefit_End_Age',
          'Benefit_Term_Type',
          'Multiplicative_Rated_Status',
          'Additive_Rated_Status',
          'Waiting_Period_1_Type',
          'Waiting_Period_1',
          'Waiting_Period_2_Type',
          'Waiting_Period_2',
          'Waiting_Period_3_Type',
          'Waiting_Period_3',
          'Temp_Mult_Extra_Rating_Dur_1',
          'Temp_Mult_Extra_Rating_1',
          'Temp_Mult_Extra_Rating_Dur_2',
          'Temp_Mult_Extra_Rating_2',
          'Temp_Add_Extra_Rating_Dur_1',
          'Temp_Add_Extra_Rating_1',
          'Temp_Add_Extra_Rating_Dur_2',
          'Temp_Add_Extra_Rating_2',
          'Child_Benefit_Type',
          'Child_Benefit',
          'Acceleration_Risk_Type',
          'Acceleration_Benefit',
          'Buyback_Option_Type',
          'Buyback_Option',
          'Benefit_Change_Rate',
          'Benefit_Change_Frequency'],
        identifier: 'Missing Values Check Blocking',
        name: 'Variable dependency',
        category: 'Blocking',
        description: 'Certain variables require additional information from a second variable.',
        files: ['policy'],
        order: 35
      }),
      // 36
      new Control({
        controlColumns: ['type_of_event', 'main_risk_type', 'acceleration_risk_type'],
        identifier: 'Events should be coherent with the main risk type and the acceleration risk type',
        name: 'Consistency of events with coverage risk type',
        category: 'Warning',
        description: 'Event types should be coherent with the main risk type and the acceleration risk type.',
        files: ['policy'],
        order: 36
      }),
      // 37
      new Control({
        controlColumns: ['Client_Risk_Carrier_Name', 'Study_Client'],
        identifier: 'Product file information should match study metadata',
        name: 'Study Metadata',
        category: 'Warning',
        description: 'Information of the product file should be coherent with study metadata: Client, Client Group, Treaty Number, Distribution Brand and Country.',
        files: ['product'],
        order: 37
      }),
      //   // 38
      //   new Control({
      //    controlColumns: ['Client_Group','Study_Client_Group'],
      //    identifier: 'Product file information Client Group should match study metadata',
      //    name: 'Product file information Client Group should match study metadata',
      //    category: 'Warning',
      //    description: 'Product file information Client Group should match study metadata.',
      //    files: ['product'],
      //    order : 35
      //  }),
      //   // 39
      //   new Control({
      //    controlColumns: ['Treaty_Number_Omega','Study_Treaty_Number'],
      //    identifier: 'Product file information Treaty Number should match study metadata',
      //    name: 'Product file information Treaty Number should match study metadata',
      //    category: 'Warning',
      //    description: 'Product file information Treaty Number should match study metadata.',
      //    files: ['product'],
      //    order : 36
      //  }),
      //  // 40
      //  new Control({
      //    controlColumns: ['Distribution_Brand_Name','Distribution_Brand'],
      //    identifier: 'Product file information Distribution Brand Name should match study metadata',
      //    name: 'Product file information Distribution Brand Name should match study metadata',
      //    category: 'Warning',
      //    description: 'Product file information Distribution Brand Name should match study metadata.',
      //    files: ['product'],
      //    order : 37
      //  }),
      //  // 41
      //  new Control({
      //    controlColumns: ['Client_Country','Study_Country'],
      //    identifier: 'Product file information Client Country should match study metadata',
      //    name: 'Product file information Client Country should match study metadata',
      //    category: 'Warning',
      //    description: 'Product file information Client Country should match study metadata.',
      //    files: ['product'],
      //    order : 38
      //  }),
      // 34
      new Control({
        controlColumns: ['Date_of_Birth', 'Date_of_Commencement'],
        identifier: 'Missing Values Check_2',
        name: 'Missing Values',
        category: 'Blocking',
        description: 'Certain date and numerical variables must not have missing values',
        files: ['policy'],
        order: 38
      }),
      new Control({
        controlColumns: ['Event_Amount_Reinsurer', 'Event_Amount_Insurer'],
        identifier: 'Event Amount Reinsurer <= Event Amount Insurer',
        name: 'Event Amount Reinsurer <= Event Amount Insurer',
        category: 'Warning',
        description: 'Event Amount Reinsurer should not be greater than Event Amount Insurer',
        files: ['policy'],
        order: 39
      }),
      new Control({
        controlColumns: ['Date_of_last_medical_selection', 'Date_of_Commencement'],
        identifier: 'Consistent date of last medical selection',
        name: 'Consistent date of last medical selection',
        category: 'Warning',
        description: 'Date of last medical selection is always before or always after Date of Commencement',
        files: ['policy'],
        order: 40
      }),
      new Control({
        controlColumns: ['Date_of_Begin_Current_Condition', 'Date_of_End_Current_Condition'],
        identifier: 'status date space',
        name: 'Status Date Space',
        category: 'Warning',
        description: 'The start date of a period should be the following day of the end date of the previous period; it should not be later.',
        files: ['policy'],
        order: 41
      }),
      new Control({
        controlColumns: ['Product_ID', 'Benefit_Max_Age'],
        identifier: 'Coherence of Benefit Max Age with policy file',
        name: 'Coherence of Benefit Max Age with policy file',
        category: 'Warning',
        description: 'The Benefit Max Age of each policy should be lower than the Max Benefit Expiry Age outlined in the product file.',
        files: ['policy', 'product'],
        order: 42
      }),
      new Control({
        controlColumns: [],
        identifier: 'Incidence_Death XOR Incidence/Death',
        name: 'Incidence_Death XOR Incidence/Death',
        category: 'Blocking',
        description: 'The case where ix+qx is analyzed together cannot be mixed with the standard ix or qx analysis.',
        files: ['policy'],
        order: 43
      })
    ];
    FuncControls.forEach(c => {
      c.running = true;
      c.canceled = false;
    });
    return FuncControls;
  }
}

