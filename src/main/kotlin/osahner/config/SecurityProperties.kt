package osahner.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@ConfigurationProperties(prefix = "jwt-security")
@Validated
class SecurityProperties {
  @field:NotBlank
  @field:Size(min = 32)
  var secret = ""

  @field:Positive
  var expirationTime: Int = 31 // in days

  @field:Positive
  var strength = 10

  // constant
  val tokenPrefix = "Bearer "
  val headerString = "Authorization"
}
