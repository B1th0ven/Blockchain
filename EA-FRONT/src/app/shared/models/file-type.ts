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

  public static mapToApi( files: Array<FileType>, type: string ) {
      if ( !files ) return null
      let file = files.find( f => ( f.type == type ) )
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
