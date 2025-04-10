@file:Suppress("unused")

package osahner.domain

import jakarta.persistence.*

@Entity
@Table(name = "app_role")
class Role(
  @Id
  @GeneratedValue(generator = "app_role-sequence-generator")
  @SequenceGenerator(
    name = "app_role-sequence-generator",
    sequenceName = "app_role_SEQ",
    allocationSize = 1,
    initialValue = 100,
  )
  val id: Int,

  @Column(name = "role_name", updatable = false)
  var roleName: String? = null,

  @Column(name = "description", updatable = false)
  var description: String? = null,

  @ManyToMany(mappedBy = "roles")
  var users: MutableSet<User>? = null
)
