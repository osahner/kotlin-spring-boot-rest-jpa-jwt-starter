package osahner

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ExtensionKtTest {

  @Test
  fun `writeValueAsString and toStringArray`() {
    val list = listOf("test1", "test2")
    val result = list.writeValueAsString()
    assertNotNull(result)
    val nullResult = (null as List<String>?).writeValueAsString()
    assertNull(nullResult)
    val listFromResult = result.toStringArray()
    assertNotNull(listFromResult)
    assertEquals(listFromResult?.size, 2)
    assertEquals(list, listFromResult)
    val nullResult2 = (null as String?).toStringArray()
    assertNull(nullResult2)
    val nullResult3 = "this [ is no string array}".toStringArray()
    assertNull(nullResult3)
    val map = mapOf("stringProperty" to "theProperty", "numberProperty" to 6)
    val result4 = map.writeValueAsString()
    assertNotNull(result4)
    val mapFromResult = result4.toMap()
    assertNotNull(mapFromResult)
    assertEquals(map, mapFromResult)
    val nullResult4 = (null as String?).toMap()
    assertNull(nullResult4)
  }
}
