import { Component, OnInit, Input, ViewChild, ElementRef, OnChanges } from '@angular/core';
import { Dataset } from '../../shared/models/dataset';
import { date } from '../../shared/models/date';
import { FileService } from '../../services/file.service';
import { DatasetService } from '../../services/dataset.service'
import { Study } from '../../shared/models/study';
import { find } from 'rxjs/operator/find';
@Component({
  selector: 'app-dataset-data-privacy',
  templateUrl: './dataset-data-privacy.component.html',
  styleUrls: ['./dataset-data-privacy.component.scss']
})
export class DatasetDataPrivacyComponent implements OnInit {
  data:any
  @Input() dataset: Dataset
  @Input() study
  @Input() editable: boolean
  @Input() doneControls
  @Input() isDatasetAssociatedToRun : boolean
  @Input() isStudyValidated : boolean = false
  @Input() metadata : any;
  @Input() tab: any;
  eaAnoGuide : string ="EA Anonymisation Guideline.docx"
  portfolio : string = "Portfolio specific assessment.xlsx"
  anoGuidline : string = "Anonymization assessment.pdf"
  today: date = new date(new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate())
  metadatauntrue:any
  CompNumber:any
  constructor(private fileService : FileService, private ds : DatasetService) { 
    
  }

  ngOnInit() {
    console.log("HERE")
    this.metadata = {
      date: ' ',
      time: ' ',
      user: ' '
      
    }
    console.log(this.study)
    if(this.metadata.date ==' ' && this.study.creator.ruFirstName){
      this.metadata.date = this.study.date
      this.metadata.user = this.study.creator.ruFirstName+ ' ' + this.study.creator.ruLastName
      this.metadata.time = this.study.time
    }
  }
  concatinate(a, b) {
    if (a && b)
      return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }

  showGDPR() {
    return this.dataset.files[0].status != "na"
  }

  getStartDate(){
    let date = new Date(new Date().getTime() + 24 * 60 * 60 * 1000)
    return { year: date.getFullYear(), month: (date.getMonth() + 1), day: date.getDate() }
  }

  checkValidDateFormat(date: date) {
    if (!date) {
      return true
    }
    let date2 = new Date();
    date2.setFullYear(date.year, date.month - 1, date.day);
    return date.valid
  }

  validDate(supDate: date, lowDate: date) {
    if (!supDate || !lowDate) return true

    if (isNaN(supDate.year) || isNaN(supDate.day) || isNaN(supDate.month)) return true
    if (isNaN(lowDate.year) || isNaN(lowDate.day) || isNaN(lowDate.month)) return true

    let low = new Date(new Date(lowDate.year + '-' + lowDate.month + '-' + lowDate.day).getTime() + 24 * 60 * 60 * 1000)
    let sup = new Date(supDate.year + '-' + supDate.month + '-' + supDate.day)

    if (isNaN(low.getTime()) || isNaN(sup.getTime())) return true


    if (low <= sup) return true;

    return false
  }

  static CompNumber = 0

  download(filename : string) {
    this.fileService.openFileNda(filename);
  }
}
