package osahner.domain

import osahner.dto.AddressDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Address(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Int?,

  var name: String?,

  var street: String?,

  var zip: String?,

  var city: String?,

  var email: String?,

  var tel: String?
) {
  companion object {
    fun fromDTO(dto: AddressDto) = Address(
      id = dto.id,
      name = dto.name,
      street = dto.street,
      zip = dto.zip,
      city = dto.city,
      email = dto.email,
      tel = dto.tel
    )
  }
}
