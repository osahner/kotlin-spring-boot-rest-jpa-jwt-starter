package osahner.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import osahner.add
import osahner.config.SecurityProperties
import osahner.service.AppUserDetailsService
import java.util.*
import javax.crypto.SecretKey


@Component
class TokenProvider(
  private val securityProperties: SecurityProperties,
  private val userDetailsService: AppUserDetailsService,
) {
  private var key: SecretKey? = null

  @PostConstruct
  fun init() {
    key = Keys.hmacShaKeyFor(securityProperties.secret.toByteArray())
  }

  fun createToken(authentication: Authentication): String {
    val tokenValidity = Date().add(Calendar.DAY_OF_MONTH, securityProperties.expirationTime)
    val authClaims: MutableList<String> = mutableListOf()
    authentication.authorities?.let { authorities ->
      authorities.forEach { claim -> authClaims.add(claim.toString()) }
    }

    return Jwts.builder()
      .subject(authentication.name)
      .claim("auth", authClaims)
      .expiration(tokenValidity)
      .signWith(key)
      .compact()
  }

  fun getAuthentication(token: String): Authentication? {
    // val jwk = Jwks.parser().build().parse(securityProperties.secret)

    return try {
      val claims = Jwts.parser()
        .verifyWith(key)
        .clockSkewSeconds(3 * 60)
        .build()
        .parseSignedClaims(token.replace(securityProperties.tokenPrefix, ""))
      val userDetail = userDetailsService.loadUserByUsername(claims.payload.subject)
      val principal = User(userDetail.username, "", userDetail.authorities)
      UsernamePasswordAuthenticationToken(principal, token, userDetail.authorities)
    } catch (e: Exception) {
      return null
    }
  }
}
