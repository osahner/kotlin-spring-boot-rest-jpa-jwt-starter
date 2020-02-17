package osahner.dto

import com.opencsv.bean.CsvBindByName
import osahner.domain.Address
import java.time.LocalDateTime

class AddressImportDto {
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
