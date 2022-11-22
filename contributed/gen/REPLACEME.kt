package osahner.api.replaceme

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class REPLACEME(
  @Id
  @GeneratedValue(generator = "replaceme-sequence-generator")
  @GenericGenerator(
    name = "replaceme-sequence-generator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
      Parameter(name = "sequence_name", value = "replaceme_SEQ"),
      Parameter(name = "initial_value", value = "100"),
      Parameter(name = "increment_size", value = "1")
    ]
  )
  var id: Int?,
) {
  fun toDTO() = REPLACEMEDto(
    id = this.id,
  )

  companion object {
    fun fromDTO(dto: REPLACEMEDto) = REPLACEME(
      id = dto.id,
    )
  }
}
