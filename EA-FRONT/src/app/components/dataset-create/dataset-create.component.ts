import { Component, OnInit ,Input} from '@angular/core';
import { DatasetService } from '../../services/dataset.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FileType } from '../../shared/models/file-type';
import { Dataset } from '../../shared/models/dataset';


@Component({
  selector: 'app-dataset-create',
  templateUrl: './dataset-create.component.html',
  styleUrls: ['./dataset-create.component.scss']
})
export class DatasetCreateComponent implements OnInit {

  busy:boolean=false
  errMsg

  //Dataset Properties
  dataset:Dataset = new Dataset()
  //Dataset files
  files:[FileType]
  selectedType

  constructor(private router: Router ,private ds:DatasetService, private route: ActivatedRoute) { }

  ngOnInit() {
  }

  createDataset(e){
    
  }

}
