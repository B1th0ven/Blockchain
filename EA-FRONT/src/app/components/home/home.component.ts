import { Component, OnInit } from '@angular/core';
import { FileService } from '../../services/file.service';


class Log{
  date:string
  version:string
  changes:Array<Change>
}

interface Change {
  type:string,
  description:string,
  id:string,
  component:string,
  func:string
}

interface Doc {
  name:string,
  type:string,
  path:string
}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {

  constructor(private fs: FileService) { }

  logs:Array<Log>
  docs:Array<Doc>

  ngOnInit() {
    this.docs = [
      {
        name:"User Guide",
        type:"pptx",
        path:"path"
      },
      {
        name:"Manual",
        type:"docx",
        path:"path"
      },
      {
        name:"Data dictionary",
        type:"xlsx",
        path:"path"
      }
    ]

    this.fs.getHistory().subscribe(
      res => {
      this.logs = res
      this.logs.reverse()
    },
    err =>  console.log(err));

  }

  clearCache(){
    localStorage.clear()
    sessionStorage.clear()
    alert('Cleared')
  }

  download(filename : string) {
    this.fs.openFileDocs(filename);
  }

}
