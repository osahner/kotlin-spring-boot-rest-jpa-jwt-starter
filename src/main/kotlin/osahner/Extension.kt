package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.reflect.full.memberProperties

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

// Adam Kis https://stackoverflow.com/a/56115232/7573817
@Throws(IllegalAccessException::class, ClassCastException::class)
inline fun <reified T> Any.getField(fieldName: String): T? {
  this::class.memberProperties.forEach { kCallable ->
    if (fieldName == kCallable.name) {
      return kCallable.getter.call(this) as T?
    }
  }
  return null
}
