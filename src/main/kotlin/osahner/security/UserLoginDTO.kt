package osahner.security

data class UserLoginDTO(
  val username: String,
  val password: String,
  val verificationCode: String? = null
)
