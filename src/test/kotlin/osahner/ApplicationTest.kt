package osahner

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTests(@Autowired private val restTemplate: TestRestTemplate) {

  @Test
  fun ping() {
    val expected = "Pong!"
    assertEquals(expected, restTemplate.getForObject<String>("/api/test"))
  }

  @Test
  fun pingRestricted() {
    val responseEntity = restTemplate.getForEntity<String>("/api/restricted")
    val expected = "Pong!"

    assertEquals(HttpStatus.FORBIDDEN, responseEntity.statusCode)

    // TODO Get JWT
  }
}
