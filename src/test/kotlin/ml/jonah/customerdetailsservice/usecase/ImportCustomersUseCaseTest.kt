package ml.jonah.customerdetailsservice.usecase

import java.util.*
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import ml.jonah.customerdetailsservice.service.GeoCodingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ImportCustomersUseCaseTest {
    @InjectMocks private lateinit var importCustomersUseCase: ImportCustomersUseCase

    @Mock private lateinit var customerRepository: CustomerRepository

    @Mock private lateinit var geoCodingService: GeoCodingService

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

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val response = importCustomersUseCase.invoke(request)

        assertEquals(response, ImportCustomersUseCase.Response.Success)
        verify(customerRepository).saveAll(expectedCustomerEntities)
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

        `when`(geoCodingService.getCoordinatesForAddress(address))
            .thenReturn(Coordinates(latitude = 10.0, longitude = 20.0))

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val response = importCustomersUseCase.invoke(request)

        assertEquals(response, ImportCustomersUseCase.Response.Success)

        verify(customerRepository).saveAll(expectedCustomerEntities)
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

        `when`(geoCodingService.getCoordinatesForAddress(address)).thenThrow(expectedException)

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val response = importCustomersUseCase.invoke(request)
        // todo: add assertion about response being "Failure" with correct exception
    }
}
