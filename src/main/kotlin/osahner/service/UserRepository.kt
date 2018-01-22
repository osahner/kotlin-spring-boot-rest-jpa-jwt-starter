package osahner.service

import org.springframework.data.repository.CrudRepository
import osahner.domain.User
import java.util.*

interface UserRepository : CrudRepository<User, Long> {
  fun findByUsername(username: String): Optional<User>
}
