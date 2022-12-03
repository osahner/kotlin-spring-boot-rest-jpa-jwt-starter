package osahner.api.address

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import osahner.toArray
import osahner.toMap
import osahner.writeValueAsString
import java.time.LocalDateTime

@Entity
@Table(name = "address")
data class Address(
  @Id
  @GeneratedValue(generator = "address-sequence-generator")
  @GenericGenerator(
    name = "address-sequence-generator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
      Parameter(name = "sequence_name", value = "address_SEQ"),
      Parameter(name = "initial_value", value = "100"),
      Parameter(name = "increment_size", value = "1")
    ]
  )
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
    id,
    name,
    street,
    zip,
    city,
    email,
    tel,
    enabled,
    lastModified,
    options.toMap(),
    things.toArray()
  )

  companion object {
    fun fromDTO(dto: AddressDto) = Address(
      dto.id,
      dto.name,
      dto.street,
      dto.zip,
      dto.city,
      dto.email,
      dto.tel,
      dto.enabled,
      LocalDateTime.now(),
      dto.options.writeValueAsString(),
      dto.things.writeValueAsString()
    )
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
    other as Address

    return id != null && id == other.id
  }

  override fun hashCode(): Int = javaClass.hashCode()

  @Override
  override fun toString(): String {
    return this::class.simpleName + "(id = $id )"
  }
}
