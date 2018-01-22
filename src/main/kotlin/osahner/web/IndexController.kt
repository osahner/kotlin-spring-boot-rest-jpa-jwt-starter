package osahner.web

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class IndexController {

  @GetMapping(value = ["", "/", "/test"])
  fun helloWorld() = "Pong!"

  @GetMapping(value = ["/restricted"])
  @PreAuthorize("hasAuthority('ADMIN_USER')")
  fun helloRestrictedWorld() = "Pong!"
}
