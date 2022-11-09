package osahner.config

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@ConfigurationProperties(prefix = "jwt-security")
@Validated
class SecurityProperties {
  @field:NotBlank
  @field:Size(min = 64)
  var secret = ""

  @field:Positive
  var expirationTime: Int = 31 // in days

  @field:Positive
  var strength = 10

  // constant
  val tokenPrefix = "Bearer "
  val headerString = "Authorization"
}
