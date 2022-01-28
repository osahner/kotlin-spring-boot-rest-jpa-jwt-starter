package osahner.service

import org.springframework.data.jpa.repository.JpaRepository
import osahner.domain.User
import java.util.*

interface UserRepository : JpaRepository<User, Int> {
  fun findByUsername(username: String): Optional<User>
}
