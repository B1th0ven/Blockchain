export const types        = ["base","trend","adjustment","policy"]
export const origins      = ["population","industry","client","SCOR","other"]
export const decrements   = ["qx","ix","wx","tx","ix+qx"]
export const sources      = ["prophet","manual"]
export const exposure     = ["initial","central"]

export function years(begin = 1970,end = 2018){

  let this_year = new Date().getFullYear()

  let array = new Array()
  for (let i = this_year ; i>=begin;i--) array.push(i)
  return array
}
