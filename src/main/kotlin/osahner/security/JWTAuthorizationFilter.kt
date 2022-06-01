package osahner.security

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import osahner.config.SecurityProperties
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(
  authManager: AuthenticationManager,
  private val securityProperties: SecurityProperties,
  private val tokenProvider: TokenProvider

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
    tokenProvider.getAuthentication(header)?.also { authentication ->
      SecurityContextHolder.getContext().authentication = authentication
    }
    chain.doFilter(req, res)
  }
}
