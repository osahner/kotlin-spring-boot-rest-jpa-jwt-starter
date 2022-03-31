package osahner.api.replaceme

import com.opencsv.bean.CsvBindByName
import osahner.service.CsvImportDto

class REPLACEMEImportDto : CsvImportDto<REPLACEME> {
  @CsvBindByName(required = true)
  var id: Int? = null

  override fun toEntity() = REPLACEME(
    id = id,
  )
}
