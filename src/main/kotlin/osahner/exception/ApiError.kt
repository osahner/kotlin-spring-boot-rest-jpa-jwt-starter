package osahner.exception

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase
import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import javax.validation.ConstraintViolation

@JsonTypeInfo(
  include = JsonTypeInfo.As.WRAPPER_OBJECT,
  use = JsonTypeInfo.Id.CUSTOM,
  property = "error",
  visible = true
)
@JsonTypeIdResolver(LowerCaseClassNameResolver::class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
internal class ApiError private constructor() {

  var status: HttpStatus? = null
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private var timestamp: LocalDateTime? = null
  var message: String? = null
  var debugMessage: String? = null
  private var subErrors: MutableList<ApiSubError>? = null

  init {
    timestamp = LocalDateTime.now()
  }

  constructor(status: HttpStatus) : this() {
    this.status = status
  }

  constructor(status: HttpStatus, ex: Throwable) : this() {
    this.status = status
    this.message = "Unexpected error"
    this.debugMessage = ex.localizedMessage
  }

  constructor(status: HttpStatus, message: String, ex: Throwable) : this() {
    this.status = status
    this.message = message
    this.debugMessage = ex.localizedMessage
  }

  private fun addSubError(subError: ApiSubError) {
    if (subErrors == null) {
      subErrors = ArrayList()
    }
    subErrors!!.add(subError)
  }

  private fun addValidationError(obj: String, field: String, rejectedValue: Any, message: String) {
    addSubError(ApiValidationError(obj, field, rejectedValue, message))
  }

  private fun addValidationError(obj: String, message: String) {
    addSubError(ApiValidationError(obj, message))
  }

  private fun addValidationError(fieldError: FieldError) {
    this.addValidationError(
      fieldError.objectName,
      fieldError.field,
      fieldError.rejectedValue,
      fieldError.defaultMessage
    )
  }

  fun addValidationErrors(fieldErrors: List<FieldError>) {
    fieldErrors.forEach(Consumer<FieldError> { this.addValidationError(it) })
  }

  private fun addValidationError(objectError: ObjectError) {
    this.addValidationError(
      objectError.objectName,
      objectError.defaultMessage
    )
  }

  fun addValidationError(globalErrors: List<ObjectError>) {
    globalErrors.forEach(Consumer<ObjectError> { this.addValidationError(it) })
  }

  /**
   * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
   *
   * @param cv the ConstraintViolation
   */
  private fun addValidationError(cv: ConstraintViolation<*>) {
    this.addValidationError(
      cv.rootBeanClass.simpleName,
      (cv.propertyPath as PathImpl).leafNode.asString(),
      cv.invalidValue,
      cv.message
    )
  }

  fun addValidationErrors(constraintViolations: Collection<ConstraintViolation<*>>) {
    constraintViolations.forEach(Consumer<ConstraintViolation<*>> { this.addValidationError(it) })
  }

  fun getSubErrors(): Collection<ApiSubError>? {
    return this.subErrors
  }

  fun setSubErrors(subErrors: MutableList<ApiSubError>) {
    this.subErrors = subErrors
  }


  internal abstract inner class ApiSubError

  internal inner class ApiValidationError : ApiSubError {
    private var `object`: String? = null
    private var field: String? = null
    private var rejectedValue: Any? = null
    private var message: String? = null

    constructor(obj: String, message: String) {
      this.`object` = obj
      this.message = message
    }

    constructor(obj: String, field: String, rejectedValue: Any, message: String) {
      this.`object` = obj
      this.field = field
      this.rejectedValue = rejectedValue
      this.message = message
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is ApiValidationError) return false

      if (`object` != other.`object`) return false
      if (field != other.field) return false
      if (rejectedValue != other.rejectedValue) return false
      if (message != other.message) return false

      return true
    }

    override fun hashCode(): Int {
      var result = `object`?.hashCode() ?: 0
      result = 31 * result + (field?.hashCode() ?: 0)
      result = 31 * result + (rejectedValue?.hashCode() ?: 0)
      result = 31 * result + (message?.hashCode() ?: 0)
      return result
    }

    override fun toString(): String {
      return "ApiValidationError(`object`=$`object`, field=$field, rejectedValue=$rejectedValue, message=$message)"
    }

  }
}

internal class LowerCaseClassNameResolver : TypeIdResolverBase() {

  override fun idFromValue(value: Any): String {
    return value.javaClass.simpleName.toLowerCase()
  }

  override fun idFromValueAndType(value: Any, suggestedType: Class<*>): String {
    return idFromValue(value)
  }

  override fun getMechanism(): JsonTypeInfo.Id {
    return JsonTypeInfo.Id.CUSTOM
  }
}
