package osahner.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt-security")
class SecurityProperties {
  var secret = "" // Minimum length for the secret is 42.
  var expirationTime: Int = 31 // in days
  var strength = 10

  // constant
  val tokenPrefix = "Bearer "
  val headerString = "Authorization"
}
