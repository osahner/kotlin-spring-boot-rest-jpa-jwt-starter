package osahner.domain

import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractJpaPersistable<T : Serializable> {

  companion object {
    private val serialVersionUID = -124340093482348L
  }

  @Id
  @GeneratedValue
  private var id: T? = null

  fun getId(): T? {
    return id
  }

  override fun equals(other: Any?): Boolean {
    other ?: return false

    if (this === other) return true

    if (javaClass != ProxyUtils.getUserClass(other)) return false

    other as AbstractJpaPersistable<*>

    return if (null == this.getId()) false else this.getId() == other.getId()
  }

  override fun hashCode(): Int {
    return 31
  }

  override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

}
