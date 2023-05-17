package osahner.service

import org.jboss.aerogear.security.otp.Totp
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import osahner.domain.User


@Component
class AppAuthenticationManager(
  private val userRepository: UserRepository,
  val bCryptPasswordEncoder: BCryptPasswordEncoder,
) : AuthenticationManager {
  @Throws(AuthenticationException::class)
  override fun authenticate(authentication: Authentication): Authentication {
    val password = authentication.credentials.toString()
    val user: User = userRepository.findByUsername(authentication.name).orElseThrow {
      UsernameNotFoundException("The username ${authentication.name} doesn't exist")
    }
    if (!bCryptPasswordEncoder.matches(password, user.password)) {
      throw BadCredentialsException("Bad credentials")
    }
    if (user.isUsing2FA) {
      val verificationCode: String = (authentication.details as Map<*, *>)["verificationCode"].toString()
      val totp = Totp(user.secret)
      when {
        !isValidLong(verificationCode) -> throw BadCredentialsException("Invalid verfication code")
        !totp.verify(verificationCode) -> throw BadCredentialsException("Invalid verfication code")
      }
    }
    val authorities = ArrayList<GrantedAuthority>()
    if (user.roles != null) {
      user.roles!!.forEach { authorities.add(SimpleGrantedAuthority(it.roleName)) }
    }
    return UsernamePasswordAuthenticationToken(user.username, user.password, authorities)
  }

  private fun isValidLong(code: String): Boolean {
    try {
      code.toLong()
    } catch (e: NumberFormatException) {
      return false
    }
    return true
  }
}
