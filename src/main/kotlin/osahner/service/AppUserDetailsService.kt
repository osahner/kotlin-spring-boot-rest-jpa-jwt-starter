package osahner.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class AppUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(s: String): UserDetails {
    val user = userRepository.findByUsername(s)
      .orElseThrow { UsernameNotFoundException("The username $s doesn't exist") }

    val authorities = ArrayList<GrantedAuthority>()
    user.roles!!.forEach { authorities.add(SimpleGrantedAuthority(it.roleName)) }

    return User(
      user.username,
      user.password,
      authorities
    )
  }
}
