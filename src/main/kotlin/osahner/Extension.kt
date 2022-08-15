package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun Any?.writeValueAsString(): String? = when {
  (this != null) -> jacksonObjectMapper().writeValueAsString(this)
  else -> null
}

fun <T> String?.toArray(): Collection<T>? = when {
  !this.isNullOrEmpty() -> try {
    jacksonObjectMapper().readValue(this)
  } catch (e: Exception) {
    null
  }

  else -> null
}

fun String?.toMap(): Map<String, Any>? = when {
  !this.isNullOrEmpty() -> try {
    jacksonObjectMapper().readValue(this)
  } catch (e: Exception) {
    null
  }

  else -> null
}

// kudos to https://gist.github.com/maiconhellmann/796debb4007139d50e39f139be08811c
fun Date.add(field: Int, amount: Int): Date {
  Calendar.getInstance().apply {
    time = this@add
    add(field, amount)
    return time
  }
}

fun LocalDate?.toDate(): Date? = when {
  (this != null) -> try {
    Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())
  } catch (e: Exception) {
    null
  }

  else -> null
}

fun LocalDateTime?.toDate(): Date? = when {
  (this != null) -> try {
    Date.from(atZone(ZoneId.systemDefault()).toInstant())
  } catch (e: Exception) {
    null
  }

  else -> null
}
