package osahner.service

import org.apache.poi.ss.usermodel.Workbook
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import osahner.domain.Address
import osahner.dto.AddressDto
import osahner.toDTO
import java.util.*

@Component
class AddressService(
  val addressRepository: AddressRepository,
  val csvImportService: CsvImportService,
  private val poiExportService: PoiExportService
) {
  fun list() = addressRepository.findAll().map { it.toDTO() }

  fun findById(id: Int): Optional<Address> = addressRepository.findById(id)

  fun save(saveDto: AddressDto): Address = addressRepository.saveAndFlush(Address.fromDTO(saveDto))

  fun delete(id: Int) = addressRepository.deleteById(id)

  fun import(file: MultipartFile): Collection<Address> {
    val adressen = csvImportService.importAddress(file)
    return addressRepository.saveAll(adressen)
  }

  fun export(): Workbook {
    val result = addressRepository.findAll()
    return poiExportService.buildExcelDocument(
      "Export Address List",
      listOf("id", "name", "street", "zip", "city", "email", "tel"),
      result
    )
  }
}
