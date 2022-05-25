package osahner.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class AppAuthenticationManager(
  private val userService: AppUserDetailsService, val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : AuthenticationManager {
  @Throws(AuthenticationException::class)
  override fun authenticate(authentication: Authentication): Authentication? {
    val password = authentication.credentials.toString()
    val user = userService.loadUserByUsername(authentication.name)
    if (!bCryptPasswordEncoder.matches(password, user.password)) {
      throw BadCredentialsException("Bad credentials")
    }
    return UsernamePasswordAuthenticationToken(user.username, user.password, user.authorities)
  }
}
