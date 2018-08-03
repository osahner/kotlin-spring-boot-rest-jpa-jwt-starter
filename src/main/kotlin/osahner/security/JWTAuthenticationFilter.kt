package osahner.security

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import osahner.config.SecurityConstants.EXPIRATION_TIME
import osahner.config.SecurityConstants.HEADER_STRING
import osahner.config.SecurityConstants.SECRET
import osahner.config.SecurityConstants.TOKEN_PREFIX
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val _authenticationManager: AuthenticationManager) :
  UsernamePasswordAuthenticationFilter() {
  private var log = LoggerFactory.getLogger(JWTAuthenticationFilter::class.java)

  @Throws(AuthenticationException::class)
  override fun attemptAuthentication(
    req: HttpServletRequest,
    res: HttpServletResponse?
  ): Authentication {
    return try {
      val creds = ObjectMapper()
        .readValue(req.inputStream, osahner.domain.User::class.java)

      _authenticationManager.authenticate(
        UsernamePasswordAuthenticationToken(
          creds.username,
          creds.password,
          ArrayList<GrantedAuthority>()
        )
      )
    } catch (e: IOException) {
      throw RuntimeException(e)
    }
  }

  @Throws(IOException::class, ServletException::class)
  override fun successfulAuthentication(
    req: HttpServletRequest,
    res: HttpServletResponse,
    chain: FilterChain?,
    auth: Authentication
  ) {
    val claims: MutableList<String> = mutableListOf()
    auth.authorities!!.forEach { a -> claims.add(a.toString()) }

    val token = Jwts.builder()
      .setSubject((auth.principal as User).username)
      .claim("auth", claims)
      .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()), SignatureAlgorithm.HS512)
      .compact()
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
  }
}
