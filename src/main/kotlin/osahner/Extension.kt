package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun Any?.writeValueAsString(): String? {
  return if (this != null) {
    val mapper = jacksonObjectMapper()
    mapper.writeValueAsString(this)
  } else {
    null
  }
}

fun String?.toStringArray(): Collection<String>? {
  return if (this != null && this.isNotEmpty()) {
    val mapper = jacksonObjectMapper()
    return try {
      mapper.readValue(this)
    } catch (e: Exception) {
      null
    }
  } else {
    null
  }
}

fun String?.toMap(): Map<String, Any>? {
  return if (this != null && this.isNotEmpty()) {
    val mapper = jacksonObjectMapper()
    return try {
      mapper.readValue(this)
    } catch (e: Exception) {
      null
    }
  } else {
    null
  }
}
