package osahner.api.replaceme

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.util.*

@RestController
@RequestMapping("/api/v1/replaceme")
class REPLACEMEController(private val replacemeService: REPLACEMEService) {
  @PostMapping(value = ["/import"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun import(@RequestParam("file") multiPartFile: MultipartFile) = replacemeService.import(multiPartFile)

  @GetMapping(value = ["/export"])
  @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
  fun export(): ResponseEntity<ByteArrayResource> {
    val headers = HttpHeaders().apply {
      add("Content-Disposition", "filename=\"Export-${Date().time}.xls\"")
    }
    val bos = ByteArrayOutputStream()
    bos.use {
      replacemeService.toWorkbook().apply {
        write(bos)
      }
    }
    val resource = ByteArrayResource(bos.toByteArray())
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(resource.contentLength())
      .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
      .body(resource)
  }

  @GetMapping(value = ["", "/"])
  fun list() = replacemeService.list().map { it.toDTO() }

  @GetMapping(value = ["/{id}"])
  fun edit(@PathVariable id: Int): ResponseEntity<REPLACEMEDto> = replacemeService.findById(id).map {
    ResponseEntity.ok(it.toDTO())
  }.orElse(ResponseEntity.notFound().build())

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun save(@RequestBody dto: REPLACEMEDto) = replacemeService.save(dto).toDTO()

  @PutMapping(value = ["/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun update(@PathVariable id: Int, @RequestBody dto: REPLACEMEDto) = replacemeService.save(dto).toDTO()

  @DeleteMapping(value = ["/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun delete(@PathVariable id: Int) = replacemeService.delete(id)
}
