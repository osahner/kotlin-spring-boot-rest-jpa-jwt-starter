package osahner.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import osahner.security.JWTAuthenticationFilter
import osahner.security.JWTAuthorizationFilter
import osahner.service.AppUserDetailsService

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebConfig(
  val bCryptPasswordEncoder: BCryptPasswordEncoder,
  val userDetailsService: AppUserDetailsService
) : WebSecurityConfigurerAdapter() {

  override fun configure(http: HttpSecurity) {
    http
      .cors().and()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no sessions
      .and()
      .authorizeRequests()
      .antMatchers("/api/**").permitAll()
      .antMatchers("/error/**").permitAll()
      .antMatchers(HttpMethod.POST, "/login").permitAll()
      .anyRequest().authenticated()
      .and()
      .addFilter(JWTAuthenticationFilter(authenticationManager()))
      .addFilter(JWTAuthorizationFilter(authenticationManager()))
  }

  @Throws(Exception::class)
  override fun configure(auth: AuthenticationManagerBuilder) {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(bCryptPasswordEncoder)
  }

  @Bean
  fun authProvider(): DaoAuthenticationProvider {
    val authProvider = DaoAuthenticationProvider()
    authProvider.setUserDetailsService(userDetailsService)
    authProvider.setPasswordEncoder(bCryptPasswordEncoder)
    return authProvider
  }
}
