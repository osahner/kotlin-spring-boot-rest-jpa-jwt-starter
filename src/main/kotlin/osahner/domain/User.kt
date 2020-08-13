package osahner.domain

import javax.persistence.*

@Entity
@Table(name = "app_user")
class User(
  @Id
  @GeneratedValue()
  val id: Int,

  @Column(name = "username")
  var username: String? = null,

  @Column(name = "password")
  var password: String? = null,

  @Column(name = "first_name")
  var firstName: String? = null,

  @Column(name = "last_name")
  var lastName: String? = null,

  @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
  @JoinTable(
    name = "user_role",
    joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
  )
  var roles: Set<Role>? = null
)

