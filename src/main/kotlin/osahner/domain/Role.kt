@file:Suppress("unused")

package osahner.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter

@Entity
@Table(name = "app_role")
class Role(
  @Id
  @GeneratedValue(generator = "app_role-sequence-generator")
  @GenericGenerator(
    name = "app_role-sequence-generator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
      Parameter(name = "sequence_name", value = "app_role_SEQ"),
      Parameter(name = "initial_value", value = "100"),
      Parameter(name = "increment_size", value = "1")
    ]
  )
  val id: Int,

  @Column(name = "role_name", updatable = false)
  var roleName: String? = null,

  @Column(name = "description", updatable = false)
  var description: String? = null,

  @ManyToMany(mappedBy = "roles")
  var users: MutableSet<User>? = null
)
