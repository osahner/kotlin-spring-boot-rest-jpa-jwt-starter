@file:Suppress("unused")

package osahner.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.jboss.aerogear.security.otp.api.Base32

@Entity
@Table(name = "app_user")
class User(
  @Id
  @GeneratedValue(generator = "app_user-sequence-generator")
  @GenericGenerator(
    name = "app_user-sequence-generator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
      Parameter(name = "sequence_name", value = "app_user_SEQ"),
      Parameter(name = "initial_value", value = "100"),
      Parameter(name = "increment_size", value = "1")
    ]
  )
  val id: Int,

  @Column(name = "username", unique = true)
  var username: String? = null,

  @Column(name = "password")
  var password: String? = null,

  @Column(name = "first_name")
  var firstName: String? = null,

  @Column(name = "last_name")
  var lastName: String? = null,

  var isUsing2FA: Boolean = false,

  var secret: String = Base32.random(),

  @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
  @JoinTable(
    name = "app_user_role",
    joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
  )
  var roles: Set<Role>? = null
)

