package osahner

import osahner.domain.Address
import osahner.dto.AddressDto

fun Address.toDTO() = AddressDto(
  id = id,
  name = name,
  street = street,
  zip = zip,
  city = city,
  email = email,
  tel = tel
)
