package osahner.api.address

import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.io.input.BOMInputStream
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class AddressImportService {
  fun importAddress(file: MultipartFile): Collection<Address> =
    BOMInputStream(file.inputStream).bufferedReader().use { stream ->
      CsvToBeanBuilder<AddressImportDto>(stream)
        .withType(AddressImportDto::class.java)
        .withIgnoreLeadingWhiteSpace(true)
        .withSeparator(';')
        .build()
        .parse()
        .map { it.toAddress() }
    }
}
