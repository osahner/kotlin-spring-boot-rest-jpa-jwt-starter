package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
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

fun LocalDate?.toDate(): Date? {
  return if (this != null) {
    Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
  } else {
    null
  }
}

fun Date?.toLocalDate(): LocalDate? {
  return if (this != null) {
    Instant.ofEpochMilli(this.time)
      .atZone(ZoneId.systemDefault())
      .toLocalDate()
  } else {
    null
  }
}

fun LocalDateTime?.toDate(): Date? {
  return if (this != null) {
    Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
  } else {
    null
  }
}

fun Date?.toLocalDateTime(): LocalDateTime? {
  return if (this != null) {
    Instant.ofEpochMilli(this.time)
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime()
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
