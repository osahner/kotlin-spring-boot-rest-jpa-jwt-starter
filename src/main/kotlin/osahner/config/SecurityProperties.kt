package osahner.config

import org.hibernate.validator.constraints.Length
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt-security")
class SecurityProperties {
  @Length(min = 42, message = "Minimum length for the secret is 42.")
  var secret = ""
  var expirationTime: Int = 31 // in days
  var tokenPrefix = "Bearer "
  var headerString = "Authorization"
  var strength = 10
}
