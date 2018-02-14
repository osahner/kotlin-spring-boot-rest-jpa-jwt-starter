package osahner.web

import org.springframework.beans.factory.annotation.Required
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class IndexController {

  @GetMapping(value = ["", "/", "/test"])
  fun helloWorld() = "Pong!"

  @GetMapping(value = ["/required"])
  fun helloRequiredWorld(@RequestParam(value = "msg", required = true) msg: String) = "Echo \"$msg\"!"

  @GetMapping(value = ["/restricted"])
  @PreAuthorize("hasAuthority('STANDARD_USER')")
  fun helloRestrictedWorld() = "Pong!"
}
