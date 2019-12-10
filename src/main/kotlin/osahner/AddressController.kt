package osahner

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import osahner.dto.AddressDto
import osahner.service.AddressService
import java.io.ByteArrayOutputStream
import java.util.*

@RestController
@RequestMapping("/api/v1/address")
class AddressController(private val addressService: AddressService) {
  @GetMapping(value = ["", "/"])
  @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
  fun list() = addressService.list().map { it.toDTO() }

  @GetMapping(value = ["/edit/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun edit(@PathVariable id: Int): ResponseEntity<AddressDto> = addressService.findById(id).map {
    ResponseEntity.ok(it.toDTO())
  }.orElse(ResponseEntity.notFound().build())

  @PostMapping(value = ["/import"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun import(@RequestParam("file") multiPartFile: MultipartFile) = addressService.import(multiPartFile)

  @GetMapping(value = ["/export"])
  @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
  fun export(): ResponseEntity<ByteArrayResource> {
    val headers = HttpHeaders().apply {
      add("Content-Disposition", "filename=\"Export-${Date().time}.xls\"")
    }
    val bos = ByteArrayOutputStream()
    bos.use {
      addressService.export().apply {
        write(it)
      }
    }
    val resource = ByteArrayResource(bos.toByteArray())
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(resource.contentLength())
      .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
      .body(resource)
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun save(@RequestBody dto: AddressDto) = addressService.save(dto).toDTO()

  @DeleteMapping(value = ["/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun delete(@PathVariable id: Int) = addressService.delete(id)
}
