package ml.jonah.customerdetailsservice.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.util.*
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class CustomerServiceTest :
    DescribeSpec({
        val customerRepository = mockk<CustomerRepository>()

        val customerService = CustomerService(customerRepository = customerRepository)

        afterTest { clearAllMocks() }

        describe("getCustomerById") {
            it("should return existing customer by id") {
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

                actualCustomerEntity shouldBe expectedCustomerEntity
            }

            it(
                "should throw CustomerNotFoundException when customer cannot be found in the database"
            ) {
                val customerId = UUID.randomUUID()

                every { customerRepository.findById(customerId) } returns Optional.empty()

                val thrownException =
                    shouldThrow<CustomerNotFoundException> {
                        customerService.getCustomerById(customerId)
                    }

                thrownException.message shouldBe "Customer with id <$customerId> does not exist."
            }
        }

        describe("getAllCustomers") {
            it("should return paged list of customer entities") {
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

                val expectedCustomerEntitiesPage = PageImpl(customerEntities, pageable, 2)

                every { customerRepository.findAll(pageable) } returns expectedCustomerEntitiesPage

                val actualCustomerEntitiesPage = customerService.getAllCustomers(pageable)

                actualCustomerEntitiesPage shouldBe expectedCustomerEntitiesPage
            }
        }
    })
