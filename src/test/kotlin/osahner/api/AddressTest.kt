package osahner.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.POIFSFileSystem
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
import osahner.api.address.AddressDto
import java.io.ByteArrayInputStream


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@ActiveProfiles("test")
internal class AddressTest(
  @Autowired private val restTemplate: TestRestTemplate,
  @Autowired private val mapper: ObjectMapper
) {
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
    val header = authHeader()
    val requestEntity = HttpEntity<String>(header)
    val result =
      restTemplate.exchange("/api/v1/address", HttpMethod.GET, requestEntity, String::class.java)
    val list: Collection<AddressDto>? = mapper.readValue(result.body!!)
    return list?.filter { it.name.equals("Test") }?.map { it.id }
  }

  @Test
  @Order(1)
  fun `1 save one`() {
    val header = authHeader()
    val payload = AddressDto(
      id = null,
      name = "Test",
      street = "street",
      zip = "zip",
      city = "city",
      email = "email@email.com",
      tel = null,
      enabled = true,
      things = listOf("a thing", "a second one"),
      options = mapOf("option1" to "what an option!", "option2" to 42),
      lastModfied = null
    )
    val requestEntity = HttpEntity(payload, header)
    restTemplate.exchange("/api/v1/address", HttpMethod.POST, requestEntity, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
    }
  }

  @Test
  @Order(2)
  fun `2 update one`() {
    val list = findIdsToCleanup()
    val header = authHeader()
    val payload = AddressDto(
      id = list?.first(),
      name = "Test",
      street = "updated",
      zip = "updated",
      city = "updated",
      email = "updated@updated.com",
      tel = null,
      enabled = true,
      things = listOf("a thing", "a second one", "updated"),
      options = mapOf("option1" to "updated", "option2" to 42),
      lastModfied = null
    )
    val requestEntity = HttpEntity(payload, header)
    restTemplate.exchange("/api/v1/address", HttpMethod.POST, requestEntity, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
    }
  }

  @Test
  @Order(3)
  fun `3 import some`() {
    val header = authHeader()
    header.contentType = MediaType.MULTIPART_FORM_DATA
    val body = LinkedMultiValueMap<Any, Any>()
    body.add("file", FileSystemResource("src/test/resources/address.csv"))
    val requestEntity = HttpEntity<Any>(body, header)
    restTemplate.postForEntity("/api/v1/address/import", requestEntity, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
    }

    val headerWithBOM = authHeader()
    headerWithBOM.contentType = MediaType.MULTIPART_FORM_DATA
    val bodyWithBOM = LinkedMultiValueMap<Any, Any>()
    bodyWithBOM.add("file", FileSystemResource("src/test/resources/addressWithBOM.csv"))
    val requestEntityWithBOM = HttpEntity<Any>(bodyWithBOM, headerWithBOM)
    restTemplate.postForEntity("/api/v1/address/import", requestEntityWithBOM, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
    }
  }

  @Test
  @Order(4)
  fun `4 list all`() {
    val requestEntity = HttpEntity<String>(authHeader())
    restTemplate.exchange("/api/v1/address", HttpMethod.GET, requestEntity, String::class.java).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
      val list: Collection<AddressDto>? = mapper.readValue(it.body!!)
      assertNotNull(list)
    }
  }

  @Test
  @Order(5)
  fun `5 get one`() {
    val list = findIdsToCleanup()
    val requestEntity = HttpEntity<String>(authHeader())
    list?.map { item ->
      restTemplate.exchange(
        "/api/v1/address/${item}",
        HttpMethod.GET,
        requestEntity,
        String::class.java
      ).also {
        assertNotNull(it)
        assertEquals(HttpStatus.OK, it.statusCode)
        assertNotNull(it.body)
        val address: AddressDto = mapper.readValue(it.body!!)
        assertNotNull(address)
        assertEquals("Test", address.name)
      }
    }
  }

  @Test
  @Order(6)
  fun `6 export all`() {
    val requestEntity = HttpEntity<String>(authHeader())
    restTemplate.exchange(
      "/api/v1/address/export",
      HttpMethod.GET,
      requestEntity,
      ByteArray::class.java
    ).also {
      assertNotNull(it)
      assertEquals(HttpStatus.OK, it.statusCode)
      assertNotNull(it.body)
      ByteArrayInputStream(it.body).let { bai ->
        val pkg = POIFSFileSystem(bai)
        val wb = HSSFWorkbook(pkg)
        assertEquals("Export Address List", wb.getSheetAt(0).sheetName)
      }
    }
  }

  @Test
  @Order(7)
  fun `7 delete all`() {
    val requestEntity = HttpEntity<String>(authHeader())
    val list = findIdsToCleanup()
    list?.map { item ->
      restTemplate.exchange(
        "/api/v1/address/${item}",
        HttpMethod.DELETE,
        requestEntity,
        String::class.java
      ).also {
        assertNotNull(it)
        assertEquals(HttpStatus.OK, it.statusCode)
      }
    }
  }
}
