package osahner.api.replaceme

import com.opencsv.bean.CsvBindByName

class REPLACEMEImportDto {
  @CsvBindByName(required = true)
  var id: Int? = null

  fun toREPLACEME() = REPLACEME(
    id = id,
  )
}
