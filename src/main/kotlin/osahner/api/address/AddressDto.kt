package osahner.api.address

import java.time.LocalDateTime

data class AddressDto(
  var id: Int?,

  var name: String?,

  var street: String?,

  var zip: String?,

  var city: String?,

  var email: String?,

  var tel: String?,

  var enabled: Boolean?,

  var lastModfied: LocalDateTime?,

  var options: Map<String, Any>?,

  var things: Collection<String>?
)

