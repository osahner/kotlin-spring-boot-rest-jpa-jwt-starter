package osahner.web

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.getForObject
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
internal class IndexControllerTest(@Autowired private val restTemplate: TestRestTemplate) {

  @Test
  fun ping() {
    val expected = "Pong!"
    val result = restTemplate.getForObject<String>("/api/test")
    assertNotNull(result)
    assertEquals(expected, result)
  }

  @Test
  fun pingRestricted() {
    val result = restTemplate.getForEntity<String>("/api/restricted")
    val expected = "Pong!"

    assertNotNull(result)
    assertEquals(HttpStatus.FORBIDDEN, result.statusCode)

    // TODO Get JWT
  }
}
