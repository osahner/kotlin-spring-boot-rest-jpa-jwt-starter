package osahner

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
internal class IndexControllerTest(@Autowired private val restTemplate: TestRestTemplate) {
  @Test
  @Order(1)
  fun ping() {
    val expected = "Pong!"
    restTemplate.getForObject<String>("/api/v1/test").also {
      assertNotNull(it)
      assertEquals(expected, it)
    }
  }

  @Test
  @Order(2)
  fun `test required`() {
    val msg = "Required Test"
    val expected = """Echo "$msg"!"""

    restTemplate.getForObject<String>("/api/v1/required?msg=$msg").also {
      assertNotNull(it)
      assertEquals(expected, it)
    }
  }
}
