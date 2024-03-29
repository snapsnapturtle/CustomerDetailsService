package ml.jonah.customerdetailsservice.repository

import java.util.UUID
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository :
    PagingAndSortingRepository<CustomerEntity, UUID>, CrudRepository<CustomerEntity, UUID>
