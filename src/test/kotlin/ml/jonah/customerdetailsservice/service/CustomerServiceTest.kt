package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class CustomerServiceTest {
    @InjectMocks
    private lateinit var customerService: CustomerService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Test
    internal fun `should return existing customer by id`() {
        val customerId = UUID.randomUUID()

        val expectedCustomerEntity = CustomerEntity(
            id = customerId,
            name = "Pizzeria Luigi Gmbh",
            commercialName = "Tratoria Luigi",
            address = null,
            storeNumber = 20,
            number = 100,
            coordinates = null
        )

        `when`(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomerEntity))

        val actualCustomerEntity = customerService.getCustomerById(customerId)

        assertThat(actualCustomerEntity).isEqualTo(expectedCustomerEntity)
    }

    @Test
    internal fun `should throw CustomerNotFoundException when customer cannot be found in the database`() {
        val customerId = UUID.randomUUID()

        `when`(customerRepository.findById(customerId)).thenReturn(Optional.empty())

        assertThrows<CustomerNotFoundException> {
            customerService.getCustomerById(customerId)
        }
    }
}
