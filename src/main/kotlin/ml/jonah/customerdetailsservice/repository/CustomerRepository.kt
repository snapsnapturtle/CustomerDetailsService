package ml.jonah.customerdetailsservice.repository

import ml.jonah.customerdetailsservice.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerRepository : JpaRepository<CustomerEntity, UUID>
