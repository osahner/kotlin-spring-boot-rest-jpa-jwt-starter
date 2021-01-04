package osahner.api.address

import osahner.toMap
import osahner.toStringArray
import osahner.writeValueAsString
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob

@Entity
data class Address(
  @Id
  @GeneratedValue
  var id: Int?,

  var name: String?,

  var street: String?,

  var zip: String?,

  var city: String?,

  var email: String?,

  var tel: String?,

  var enabled: Boolean?,

  var lastModified: LocalDateTime?,

  @Lob var options: String?,

  @Lob var things: String?
) {
  fun toDTO() = AddressDto(
    id = this.id,
    name = this.name,
    street = this.street,
    zip = this.zip,
    city = this.city,
    email = this.email,
    tel = this.tel,
    enabled = this.enabled,
    lastModfied = lastModified,
    options = this.options.toMap(),
    things = this.things.toStringArray()
  )

  companion object {
    fun fromDTO(dto: AddressDto) = Address(
      id = dto.id,
      name = dto.name,
      street = dto.street,
      zip = dto.zip,
      city = dto.city,
      email = dto.email,
      tel = dto.tel,
      enabled = dto.enabled,
      lastModified = LocalDateTime.now(),
      options = dto.options.writeValueAsString(),
      things = dto.things.writeValueAsString()
    )
  }
}
