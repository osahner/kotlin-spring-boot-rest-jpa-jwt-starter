@file:Suppress("unused", "unused")

package osahner.domain

import javax.persistence.*

@Entity
@Table(name = "app_role")
class Role(
  @Id
  @GeneratedValue
  val id: Int,

  @Column(name = "role_name", updatable = false)
  val roleName: String? = null,

  @Column(name = "description", updatable = false)
  val description: String? = null
)
