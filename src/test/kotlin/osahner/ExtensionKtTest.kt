package osahner

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ExtensionKtTest {

    @Test
    fun `writeValueAsString and toStringArray`() {
      val list = listOf("test1", "test2")
      val result = list.writeValueAsString()
      assertNotNull(result)
      val nullResult = (null as List<String>?).writeValueAsString();
      assertNull(nullResult);
      val listFromResult = result.toStringArray()
      assertNotNull(listFromResult)
      assertEquals(list, listFromResult)
      val nullReslut2 = (null as String?).toStringArray()
      assertNull(nullReslut2);
    }

}
