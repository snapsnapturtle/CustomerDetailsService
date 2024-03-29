package ml.jonah.customerdetailsservice.service

import io.mockk.every
import io.mockk.mockk
import java.util.*
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

internal class CustomerServiceTest {
    private val customerRepository = mockk<CustomerRepository>()

    private val customerService = CustomerService(customerRepository = customerRepository)

    @Test
    internal fun `should return existing customer by id`() {
        val customerId = UUID.randomUUID()

        val expectedCustomerEntity =
            CustomerEntity(
                id = customerId,
                name = "Pizzeria Luigi Gmbh",
                commercialName = "Tratoria Luigi",
                address = null,
                storeNumber = 20,
                number = 100,
                coordinates = null
            )

        every { customerRepository.findById(customerId) } returns
            Optional.of(expectedCustomerEntity)

        val actualCustomerEntity = customerService.getCustomerById(customerId)

        assertThat(actualCustomerEntity).isEqualTo(expectedCustomerEntity)
    }

    @Test
    internal fun `should throw CustomerNotFoundException when customer cannot be found in the database`() {
        val customerId = UUID.randomUUID()

        every { customerRepository.findById(customerId) } returns Optional.empty()

        assertThrows<CustomerNotFoundException> { customerService.getCustomerById(customerId) }
    }

    @Test
    internal fun `should return paged list of customer entities`() {
        val pageable = PageRequest.of(0, 15)

        val customerEntities =
            mutableListOf(
                CustomerEntity(
                    id = UUID.randomUUID(),
                    name = "Pizzeria Luigi Gmbh",
                    commercialName = "Tratoria Luigi",
                    address = null,
                    storeNumber = 20,
                    number = 100,
                    coordinates = null
                )
            )

        val customerEntitiesPage = PageImpl(customerEntities, pageable, 2)

        every { customerRepository.findAll(pageable) } returns customerEntitiesPage

        val actualCustomerEntitiesPage = customerService.getAllCustomers(pageable)

        assertThat(actualCustomerEntitiesPage).isEqualTo(customerEntitiesPage)
    }
}
