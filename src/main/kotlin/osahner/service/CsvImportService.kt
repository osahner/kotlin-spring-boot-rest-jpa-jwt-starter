package osahner.service

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.io.input.BOMInputStream
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import osahner.domain.Address
import java.time.LocalDateTime

@Component
class CsvImportService {
  fun importAddress(file: MultipartFile): Collection<Address> =
    BOMInputStream(file.inputStream).bufferedReader().use {
      CsvToBeanBuilder<AddressImport>(it)
        .withType(AddressImport::class.java)
        .withIgnoreLeadingWhiteSpace(true)
        .withSeparator(';')
        .build()
        .parse()
        .map { imp -> imp.toAddress() }
    }

  class AddressImport {
    @CsvBindByName(required = true)
    var id: Int? = null

    @CsvBindByName(required = true)
    var name: String? = null

    @CsvBindByName(required = true)
    var street: String? = null

    @CsvBindByName(required = true)
    var zip: String? = null

    @CsvBindByName(required = true)
    var city: String? = null

    @CsvBindByName(required = true)
    var email: String? = null

    @CsvBindByName(required = true)
    var tel: String? = null

    fun toAddress() = Address(
      id = id,
      name = name,
      street = street,
      zip = zip,
      city = city,
      email = email,
      tel = tel,
      enabled = false,
      options = null,
      things = null,
      lastModified = LocalDateTime.now()
    )
  }
}
