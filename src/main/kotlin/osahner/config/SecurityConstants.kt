package osahner.config

object SecurityConstants {
  const val SECRET = "THISisNOTsecretSECRETsecret" // TODO edit for a real secret
  const val STRENGTH = 10
  const val EXPIRATION_TIME: Long = 864000000 // 10*24*60*60*1000ms
  const val TOKEN_PREFIX = "Bearer "
  const val HEADER_STRING = "Authorization"
}
