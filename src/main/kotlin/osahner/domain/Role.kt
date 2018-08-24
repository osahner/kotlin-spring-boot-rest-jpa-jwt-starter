package osahner.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "app_role")
class Role(
  @Column(name = "role_name")
  var roleName: String? = null,

  @Column(name = "description")
  var description: String? = null
) : AbstractJpaPersistable<Long>()
