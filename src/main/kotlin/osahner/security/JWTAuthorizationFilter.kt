package osahner.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import osahner.config.SecurityConstants.HEADER_STRING
import osahner.config.SecurityConstants.SECRET
import osahner.config.SecurityConstants.TOKEN_PREFIX
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {
  private var log = LoggerFactory.getLogger(JWTAuthorizationFilter::class.java)

  @Throws(IOException::class, ServletException::class)
  override fun doFilterInternal(
    req: HttpServletRequest,
    res: HttpServletResponse,
    chain: FilterChain
  ) {
    val header = req.getHeader(HEADER_STRING)
    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res)
      return
    }
    val authentication = getAuthentication(req)
    SecurityContextHolder.getContext().authentication = authentication
    chain.doFilter(req, res)
  }

  private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
    val token = request.getHeader(HEADER_STRING)
    return if (token != null) {
      val claims = Jwts.parser()
        .setSigningKey(Keys.hmacShaKeyFor(SECRET.toByteArray()))
        .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
      val user = claims
        .body
        .subject

      val authorities = ArrayList<GrantedAuthority>()
      (claims.body["auth"] as MutableList<*>).forEach { role -> authorities.add(SimpleGrantedAuthority(role.toString())) }

      if (user != null) {
        UsernamePasswordAuthenticationToken(user, null, authorities)
      } else null
    } else null
  }
}
