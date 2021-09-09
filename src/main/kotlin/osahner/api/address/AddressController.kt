package osahner.api.address

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/address")
class AddressController(private val addressService: AddressService) {
  @PostMapping(value = ["/import"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun import(@RequestParam("file") multiPartFile: MultipartFile) = addressService.import(multiPartFile)

  @GetMapping(value = ["/export"])
  @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
  fun export(): ResponseEntity<ByteArrayResource> = addressService.export()

  @GetMapping(value = ["", "/"])
  fun list() = addressService.list().map { it.toDTO() }

  @GetMapping(value = ["/{id}"])
  fun edit(@PathVariable id: Int): ResponseEntity<AddressDto> = addressService.findById(id).map {
    ResponseEntity.ok(it.toDTO())
  }.orElse(ResponseEntity.notFound().build())

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun save(@RequestBody dto: AddressDto) = addressService.save(dto).toDTO()

  @PutMapping(value = ["/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun update(@PathVariable id: Int, @RequestBody dto: AddressDto) = addressService.save(dto).toDTO()

  @DeleteMapping(value = ["/{id}"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun delete(@PathVariable id: Int) = addressService.delete(id)
}
