package osahner.api.replaceme

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class REPLACEME(
  @Id
  @GeneratedValue
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
