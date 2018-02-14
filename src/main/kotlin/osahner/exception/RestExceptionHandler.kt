package osahner.exception

import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {

  private var log = LoggerFactory.getLogger(RestExceptionHandler::class.java)

  /**
   * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
   *
   * @param ex      MissingServletRequestParameterException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  override fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val error = "${ex.parameterName} parameter is missing"
    return buildResponseEntity(ApiError(BAD_REQUEST, error, ex))
  }


  /**
   * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
   *
   * @param ex      HttpMediaTypeNotSupportedException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  override fun handleHttpMediaTypeNotSupported(
    ex: HttpMediaTypeNotSupportedException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    return buildResponseEntity(
      ApiError(
        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        "${ex.contentType} media type is not supported. Supported media types are ${ex.supportedMediaTypes.joinToString()}",
        ex
      )
    )
  }

  override fun handleHttpRequestMethodNotSupported(
    ex: HttpRequestMethodNotSupportedException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    return buildResponseEntity(
      ApiError(
        HttpStatus.METHOD_NOT_ALLOWED,
        "${ex.method} method is not supported for this request. Supported methods are ${ex.supportedHttpMethods?.joinToString()}",
        ex
      )
    )
  }

  /**
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
   *
   * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  override fun handleMethodArgumentNotValid(
    ex: MethodArgumentNotValidException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val apiError = ApiError(BAD_REQUEST)
    apiError.message = "Validation error"
    apiError.addValidationErrors(ex.bindingResult.fieldErrors)
    apiError.addValidationError(ex.bindingResult.globalErrors)
    return buildResponseEntity(apiError)
  }

  /**
   * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
   *
   * @param ex the ConstraintViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(javax.validation.ConstraintViolationException::class)
  protected fun handleConstraintViolation(
    ex: javax.validation.ConstraintViolationException
  ): ResponseEntity<Any> {
    val apiError = ApiError(BAD_REQUEST)
    apiError.message = "Validation error"
    apiError.addValidationErrors(ex.constraintViolations)
    return buildResponseEntity(apiError)
  }

  /**
   * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
   *
   * @param ex      HttpMessageNotReadableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  override fun handleHttpMessageNotReadable(
    ex: HttpMessageNotReadableException,
    headers: HttpHeaders, status:
    HttpStatus, request: WebRequest
  ): ResponseEntity<Any> {
    val servletWebRequest = request as ServletWebRequest
    log.error("${servletWebRequest.httpMethod} to ${servletWebRequest.request.servletPath}")
    val error = "Malformed JSON request"
    return buildResponseEntity(ApiError(BAD_REQUEST, error, ex))
  }

  /**
   * Handle HttpMessageNotWritableException.
   *
   * @param ex      HttpMessageNotWritableException
   * @param headers HttpHeaders
   * @param status  HttpStatus
   * @param request WebRequest
   * @return the ApiError object
   */
  override fun handleHttpMessageNotWritable(
    ex: HttpMessageNotWritableException,
    headers: HttpHeaders,
    status: HttpStatus,
    request: WebRequest
  ): ResponseEntity<Any> {
    val error = "Error writing JSON output"
    return buildResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex))
  }

  /**
   * Handle javax.persistence.EntityNotFoundException
   */
  @ExceptionHandler(EntityNotFoundException::class)
  protected fun handleEntityNotFound(ex: EntityNotFoundException): ResponseEntity<Any> {
    return buildResponseEntity(ApiError(NOT_FOUND, ex))
  }

  @ExceptionHandler(EmptyResultDataAccessException::class)
  protected fun handleEntityNotFound(ex: EmptyResultDataAccessException): ResponseEntity<Any> {
    val apiError = ApiError(NOT_FOUND)
    apiError.message = "Entity could not be found!"
    return buildResponseEntity(apiError)
  }

  /**
   * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
   *
   * @param ex the DataIntegrityViolationException
   * @return the ApiError object
   */
  @ExceptionHandler(DataIntegrityViolationException::class)
  protected fun handleDataIntegrityViolation(
    ex: DataIntegrityViolationException,
    request: WebRequest
  ): ResponseEntity<Any> {
    return if (ex.cause is ConstraintViolationException) {
      buildResponseEntity(
        ApiError(
          HttpStatus.CONFLICT,
          "Database error",
          ex.cause as ConstraintViolationException
        )
      )
    } else buildResponseEntity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex))
  }

  /**
   * Handle Exception, handle generic Exception.class
   *
   * @param ex the Exception
   * @return the ApiError object
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException::class)
  protected fun handleMethodArgumentTypeMismatch(
    ex: MethodArgumentTypeMismatchException,
    request: WebRequest
  ): ResponseEntity<Any> {
    val apiError = ApiError(BAD_REQUEST)
    apiError.message = "The parameter '${ex.name}' of value '${ex.value}' could not be converted to type '${ex.requiredType?.simpleName}'"
    apiError.debugMessage = ex.message
    return buildResponseEntity(apiError)
  }


  private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
    return ResponseEntity(apiError, apiError.status!!)
  }

}
