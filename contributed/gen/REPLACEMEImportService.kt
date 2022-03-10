package osahner.api.replaceme

import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.io.input.BOMInputStream
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class REPLACEMEImportService {
  fun importREPLACEME(file: MultipartFile): Collection<REPLACEME> =
    BOMInputStream(file.inputStream).bufferedReader().use { stream ->
      CsvToBeanBuilder<REPLACEMEImportDto>(stream)
        .withType(REPLACEMEImportDto::class.java)
        .withIgnoreLeadingWhiteSpace(true)
        .withSeparator(';')
        .build()
        .parse()
        .map { it.toREPLACEME() }
    }
}
