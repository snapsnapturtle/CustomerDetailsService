package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository
) {
    fun getCustomerById(customerId: UUID): CustomerEntity {
        return customerRepository.findById(customerId).orElseThrow {
            CustomerNotFoundException(customerId)
        }
    }

    fun getAllCustomers(pageable: Pageable): Page<CustomerEntity> {
        return customerRepository.findAll(pageable)
    }
}
