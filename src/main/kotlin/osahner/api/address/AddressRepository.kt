package osahner.api.address

import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Int>
