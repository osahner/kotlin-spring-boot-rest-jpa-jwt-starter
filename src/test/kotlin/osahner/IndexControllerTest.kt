package osahner

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
internal class IndexControllerTest(@Autowired private val restTemplate: TestRestTemplate) {
  val loginForm = hashMapOf("username" to "john.doe", "password" to "test1234")

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
  fun `ping restricted`() {
    restTemplate.getForEntity<String>("/api/v1/restricted").also {
      assertNotNull(it)
      assertEquals(HttpStatus.FORBIDDEN, it.statusCode)
    }
  }

  @Test
  @Order(3)
  fun `ping restricted again`() {
    val expected = "Pong!"
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    restTemplate.postForEntity<String>("/login", loginForm).also {
      headers["Authorization"] = it.headers["authorization"]?.get(0).orEmpty()
    }
    val requestEntity = HttpEntity<String>(headers)

    restTemplate.exchange("/api/v1/restricted", HttpMethod.GET, requestEntity, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
      assertEquals(expected, it.body)
    }
  }

  @Test
  @Order(4)
  fun `test required`() {
    val msg = "Required Test"
    val expected = """Echo "$msg"!"""

    restTemplate.getForObject<String>("/api/v1/required?msg=$msg").also {
      assertNotNull(it)
      assertEquals(expected, it)
    }
  }
}
