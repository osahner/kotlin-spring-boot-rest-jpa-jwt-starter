@file:Suppress("unused")

package osahner.domain

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter

@Entity
@Table(name = "app_user")
class User(
  @Id
  @GeneratedValue(generator = "sequence-generator")
  @GenericGenerator(
    name = "sequence-generator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = [
      Parameter(name = "sequence_name", value = "user_SEQ"),
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

  @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE])
  @JoinTable(
    name = "user_role",
    joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
  )
  var roles: Set<Role>? = null
)

