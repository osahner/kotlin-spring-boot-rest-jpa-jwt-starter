package osahner

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.core.io.FileSystemResource
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.LinkedMultiValueMap
import osahner.domain.Address
import osahner.dto.AddressDto


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
internal class AddressControllerTest(@Autowired private val restTemplate: TestRestTemplate) {
  val mapper = jacksonObjectMapper()
  val loginForm = hashMapOf("username" to "admin.admin", "password" to "test1234")

  fun authHeader(): HttpHeaders {
    val auth = restTemplate.postForEntity<String>("/login", loginForm)
    val bearer = auth.headers["authorization"]?.get(0).orEmpty()
    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_JSON
    headers["Authorization"] = bearer
    return headers
  }

  fun findIdsToCleanup(): Collection<Int?>? {
    val requestEntity = HttpEntity<String>(authHeader())
    val result =
      restTemplate.exchange<String>("/api/v1/address", HttpMethod.GET, requestEntity, String::class.java)
    val list: Collection<Address>? = mapper.readValue(result.body!!)
    return list?.filter { it.name.equals("Test") }?.map { it.id }
  }

  @Test
  @Order(1)
  fun save() {
    val header = authHeader()
    val payload = AddressDto(
      id = 1,
      name = "Test",
      street = "street",
      zip = "zip",
      city = "city",
      email = "email@email.com",
      tel = "tel"
    )
    val requestEntity = HttpEntity(payload, header)
    val result = restTemplate.exchange<String>("/api/v1/address", HttpMethod.POST, requestEntity, String::class.java)
    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    val address: Address? = mapper.readValue(result.body!!)
    assertNotNull(address)
  }

  @Test
  @Order(2)
  fun import() {
    val headers = authHeader()
    headers.contentType = MediaType.MULTIPART_FORM_DATA
    val body = LinkedMultiValueMap<Any, Any>()
    body.add("file", FileSystemResource("src/test/resources/address.csv"))
    val requestEntity = HttpEntity<Any>(body, headers)
    val result = restTemplate.postForEntity("/api/v1/address/import", requestEntity, String::class.java)
    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
  }


  @Test
  @Order(3)
  fun list() {
    val requestEntity = HttpEntity<String>(authHeader())
    val result =
      restTemplate.exchange<String>("/api/v1/address", HttpMethod.GET, requestEntity, String::class.java)
    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    val list: Collection<Address>? = mapper.readValue(result.body!!)
    assertNotNull(list)
  }

  @Test
  @Order(4)
  fun get() {
    val list = findIdsToCleanup()
    val requestEntity = HttpEntity<String>(authHeader())
    list?.map {
      val result =
        restTemplate.exchange<String>(
          "/api/v1/address/edit/${it}",
          HttpMethod.GET,
          requestEntity,
          String::class.java
        )
      assertNotNull(result)
      assertNotNull(result)
      assertEquals(HttpStatus.OK, result.statusCode)
      assertNotNull(result.body)
      val address: Address = mapper.readValue(result.body!!)
      assertNotNull(address)
      assertEquals(address.name, "Test")
    }

  }


  @Test
  @Order(5)
  fun export() {
    val requestEntity = HttpEntity<String>(authHeader())
    val result =
      restTemplate.exchange<String>("/api/v1/address/export", HttpMethod.GET, requestEntity, String::class.java)
    assertNotNull(result)
    assertEquals(HttpStatus.OK, result.statusCode)
    assertNotNull(result.body)
  }


  @Test
  @Order(100)
  fun delete() {
    val requestEntity = HttpEntity<String>(authHeader())
    val list = findIdsToCleanup()
    list?.map {
      val result =
        restTemplate.exchange<String>(
          "/api/v1/address/${it}",
          HttpMethod.DELETE,
          requestEntity,
          String::class.java
        )
      assertNotNull(result)
      assertEquals(HttpStatus.OK, result.statusCode, "Could not delete Adresse with id: $it")
    }
  }
}
