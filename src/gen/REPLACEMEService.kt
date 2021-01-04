package osahner.api.replaceme

import org.apache.poi.ss.usermodel.Workbook
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import osahner.service.CsvImportService
import osahner.service.PoiExportService
import java.util.*

@Component
class REPLACEMEService(
  private val replacemeRepository: REPLACEMERepository,
  private val csvImportService: CsvImportService,
  private val poiExportService: PoiExportService
) {
  fun list(): Collection<REPLACEME> = replacemeRepository.findAll()

  fun findById(id: Int): Optional<REPLACEME> = replacemeRepository.findById(id)

  fun save(dto: REPLACEMEDto): REPLACEME = replacemeRepository.saveAndFlush(REPLACEME.fromDTO(dto))

  fun delete(id: Int) = replacemeRepository.deleteById(id)

  fun import(file: MultipartFile): Collection<REPLACEME> =
    csvImportService.importREPLACEME(file).also { replacemeRepository.saveAll(it) }

  fun toWorkbook(): Workbook {
    val result = replacemeRepository.findAll().map { it.toDTO() }
    return poiExportService.buildExcelDocument(
      "Export REPLACEME List",
      listOf("id"),
      result
    )
  }
}
