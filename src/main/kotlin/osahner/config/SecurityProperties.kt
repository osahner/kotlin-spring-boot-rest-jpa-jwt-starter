package osahner.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt-security")
class SecurityProperties {
  var secret = "PseudoSecret-Pseudosecret-Please-Use-Ur-Own-Key-PseudoSecret-Pseudosecret"
  var expirationTime: Long = 864_000_000 // 10 days
  var tokenPrefix = "Bearer "
  var headerString = "Authorization"
  var strength = 10
}
