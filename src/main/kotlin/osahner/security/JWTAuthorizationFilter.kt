package osahner.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import osahner.config.SecurityProperties
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(
  authManager: AuthenticationManager,
  private val securityProperties: SecurityProperties
) : BasicAuthenticationFilter(authManager) {

  @Throws(IOException::class, ServletException::class)
  override fun doFilterInternal(
    req: HttpServletRequest,
    res: HttpServletResponse,
    chain: FilterChain
  ) {
    val header = req.getHeader(securityProperties.headerString)
    if (header == null || !header.startsWith(securityProperties.tokenPrefix)) {
      chain.doFilter(req, res)
      return
    }
    val authentication = getAuthentication(header)
    SecurityContextHolder.getContext().authentication = authentication
    chain.doFilter(req, res)
  }

  private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken? {
    return try {
      val claims = Jwts.parser()
        .setSigningKey(Keys.hmacShaKeyFor(securityProperties.secret.toByteArray()))
        .parseClaimsJws(token.replace(securityProperties.tokenPrefix, ""))
      val user = claims
        .body
        .subject
      val authorities = ArrayList<GrantedAuthority>()
      (claims.body["auth"] as MutableList<*>).forEach { role -> authorities.add(SimpleGrantedAuthority(role.toString())) }

      UsernamePasswordAuthenticationToken(user, null, authorities)
    } catch (e: Exception) {
      return null
    }
  }
}
