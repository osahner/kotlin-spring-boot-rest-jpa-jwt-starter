package osahner.domain

import javax.persistence.*

@Entity
@Table(name = "app_role")
class Role(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long,

  @Column(name = "role_name")
  var roleName: String? = null,

  @Column(name = "description")
  var description: String? = null
)
