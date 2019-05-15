import { Component, OnInit } from '@angular/core';
import { ConfigService } from '../../services/config.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  constructor(private configService: ConfigService) { }

  version = ""
  ngOnInit() {
    this.configService.getVersion().subscribe(res => {
      console.log(res);
      
      this.version = res.globalVersion + "." + res.sasVersion + "." + res.eaVersion
    })
  }

}
