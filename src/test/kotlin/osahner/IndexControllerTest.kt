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
  lateinit var bearer: String

  @BeforeEach
  fun `login to get bearer`() {
    val result = restTemplate.postForEntity<String>("/login", loginForm)
    bearer = result.headers["authorization"]?.get(0).orEmpty()
  }

  @Test
  @Order(1)
  fun ping() {
    val expected = "Pong!"
    val result = restTemplate.getForObject<String>("/api/v1/test")
    assertNotNull(result)
    assertEquals(expected, result)
  }

  @Test
  @Order(2)
  fun `ping restricted`() {
    val result = restTemplate.getForEntity<String>("/api/v1/restricted")

    assertNotNull(result)
    assertEquals(HttpStatus.FORBIDDEN, result.statusCode)
  }


  @Test
  @Order(3)
  fun `ping restricted again`() {
    val expected = "Pong!"
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers["Authorization"] = bearer
    val requestEntity = HttpEntity<String>(headers)

    val result = restTemplate.exchange<String>("/api/v1/restricted", HttpMethod.GET, requestEntity, String::class.java)
    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    assertEquals(expected, result.body)
  }

  @Test
  @Order(4)
  fun `test required`() {
    val msg = "Required Test"
    val expected = "Echo \"$msg\"!"
    val result = restTemplate.getForObject<String>("/api/v1/required?msg=$msg")

    assertNotNull(result)
    assertEquals(expected, result)
  }
}
