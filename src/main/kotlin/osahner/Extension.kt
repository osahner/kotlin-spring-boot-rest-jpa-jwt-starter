package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

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

// kudos to https://gist.github.com/maiconhellmann/796debb4007139d50e39f139be08811c
fun Date.add(field: Int, amount: Int): Date {
  Calendar.getInstance().apply {
    time = this@add
    add(field, amount)
    return time
  }
}
