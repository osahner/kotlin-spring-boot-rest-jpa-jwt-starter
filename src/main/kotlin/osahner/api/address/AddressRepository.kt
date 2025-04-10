package osahner.api.address

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Transactional(isolation = Isolation.SERIALIZABLE)
interface AddressRepository : JpaRepository<Address, Int>
