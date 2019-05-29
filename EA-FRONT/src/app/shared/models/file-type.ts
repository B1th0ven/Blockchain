import { User } from "./user";
import { date } from "./date";

export class FileType {
  extension: string
  typename: string
  type: string
  name: string
  size: string
  number: Number
  status: string = "na"
  path: string
  sheet: string
  missing: string[]
  ignored: String[]
  duplicated: String[]
  columns: any[]
  newcolumns: [any]
  header: string
  privacySubmitter: User = new User()
  privacyDate : Date
  privacyDataRestriction: String
  privacyDataDeletion : date
  inconsistent: any[]
  minyear: string
  maxyear: string
  source: {

  }

  constructor(FileInfo ? ) {
    this.number = ++FileType.numberOfInstances
    if (FileInfo)
      this.type = FileInfo.type
    if (this.type)
      this.setTypeName()
  }

  private setTypeName() {
    this.typename = this.type[0].toUpperCase() + this.type.substring(1)
  }

  static numberOfInstances = 0;
  public addNewFileToTemporaryFile(file){
     this.name = file['fileName']
     this.path = file['fileLink']
     this.columns = file['fileHeader']
     this.inconsistent = file['inconsistentColumns']
     this.type = 'policy'
     this.typename = 'Policy'
     return this

  }
  public static mapToApi( files: Array<FileType>, type?: string, rank?:number ) {
      if ( !files ) return null
      let file
      if(!type) { file = files[rank]}
      else if(type) {
      file = files.find( f => ( f.type == type ) )}
      else { 
        let res: any [];
        files.forEach(file =>  
            { 
              let a ={"eafFileType": file.type,
              "eafName": file.name,
              "eafLink": file.path,
              "eafHeader": file.columns.join( ";" ) || "",
              "eafIgnored": file.ignored.join( ";" ) || "",
              "eafSubmitter": ( type == "policy" ) ? file.privacySubmitter.mapToApi() : null,
              "eafprivacyDate": ( type == "policy" && file.privacyDate ) ? file.privacyDate.getTime() : null,
              "eafDataRestriction": ( type == "policy" ) ? file.privacyDataRestriction : null,
              "eafDataDeletion": ( file.privacyDataDeletion ) ? date.formatDate( file.privacyDataDeletion ) : null}
              res.push(a)
            }
          ) ; 
          return res 
        }
      if ( file && file.name && file.path ) {
          console.log( files );
          console.log( file.privacyDate ? file.privacyDate.getTime() : null );
          let policyFile = files.find( f => ( f.type == "policy" ));
          if ( policyFile &&  policyFile.privacyDataDeletion ) {
              file.privacyDataDeletion = policyFile.privacyDataDeletion;
          }
          return {
              "eafFileType": type,
              "eafName": file.name,
              "eafLink": file.path,
              "eafHeader": file.columns.join( ";" ) || "",
              "eafIgnored": file.ignored.join( ";" ) || "",
              "eafSubmitter": ( type == "policy" ) ? file.privacySubmitter.mapToApi() : null,
              "eafprivacyDate": ( type == "policy" && file.privacyDate ) ? file.privacyDate.getTime() : null,
              "eafDataRestriction": ( type == "policy" ) ? file.privacyDataRestriction : null,
              "eafDataDeletion": ( file.privacyDataDeletion ) ? date.formatDate( file.privacyDataDeletion ) : null
          }
      } else return null
    
  }
}
