package ml.jonah.customerdetailsservice.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import ml.jonah.customerdetailsservice.service.GeoCodingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ImportCustomersUseCaseTest {

    private val customerRepository = mockk<CustomerRepository>()

    private val geoCodingService = mockk<GeoCodingService>()

    private val importCustomersUseCase =
        ImportCustomersUseCase(
            customerRepository = customerRepository,
            geoCodingService = geoCodingService
        )

    @Test
    internal fun `should import customers without address to the database`() {
        val customerId = UUID.randomUUID()

        val customersFile =
            CustomersFile(
                customers =
                    listOf(
                        CustomersFile.Customer(
                            id = customerId,
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = null,
                            storeNumber = 20,
                            number = 100
                        )
                    )
            )

        val expectedCustomerEntities =
            listOf(
                CustomerEntity(
                    id = customerId,
                    name = "Pizzeria Luigi Gmbh",
                    commercialName = "Tratoria Luigi",
                    address = null,
                    storeNumber = 20,
                    number = 100,
                    coordinates = null
                )
            )

        every { customerRepository.saveAll(any<List<CustomerEntity>>()) } returns emptyList()

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)
        val response = importCustomersUseCase.invoke(request)

        assertEquals(response, ImportCustomersUseCase.Response.Success)

        verify { customerRepository.saveAll(expectedCustomerEntities) }
    }

    @Test
    internal fun `should import customers to the database including enriched coordinates`() {
        val customerId = UUID.randomUUID()
        val address = "Berliner Strasse 1, 13189 Berlin, Germany"

        val customersFile =
            CustomersFile(
                customers =
                    listOf(
                        CustomersFile.Customer(
                            id = customerId,
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = address,
                            storeNumber = 20,
                            number = 100
                        )
                    )
            )

        val expectedCustomerEntities =
            listOf(
                CustomerEntity(
                    id = customerId,
                    name = "Pizzeria Luigi Gmbh",
                    commercialName = "Tratoria Luigi",
                    address = address,
                    storeNumber = 20,
                    number = 100,
                    coordinates = CustomerEntity.Coordinates(latitude = 10.0, longitude = 20.0)
                )
            )

        every { geoCodingService.getCoordinatesForAddress(address) } returns
            Coordinates(latitude = 10.0, longitude = 20.0)
        every { customerRepository.saveAll(any<List<CustomerEntity>>()) } returns emptyList()

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)
        val response = importCustomersUseCase.invoke(request)

        assertEquals(response, ImportCustomersUseCase.Response.Success)

        verify { geoCodingService.getCoordinatesForAddress(address) }
        verify { customerRepository.saveAll(expectedCustomerEntities) }
    }

    @Test
    internal fun `should return a failure response when an exception occurs`() {
        val customerId = UUID.randomUUID()
        val address = "Berliner Strasse 1, 13189 Berlin, Germany"

        val customersFile =
            CustomersFile(
                customers =
                    listOf(
                        CustomersFile.Customer(
                            id = customerId,
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = address,
                            storeNumber = 20,
                            number = 100
                        )
                    )
            )

        val expectedException = RuntimeException("Failed to load coordinates")

        every { geoCodingService.getCoordinatesForAddress(address) } throws expectedException

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val response = importCustomersUseCase.invoke(request)
        // todo: add assertion about response being "Failure" with correct exception
    }
}
