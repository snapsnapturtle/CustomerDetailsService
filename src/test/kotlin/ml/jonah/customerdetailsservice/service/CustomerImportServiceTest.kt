package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class CustomerImportServiceTest {
    @InjectMocks
    private lateinit var customerImportService: CustomerImportService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Mock
    private lateinit var geoCodingService: GeoCodingService

    @Test
    internal fun `should import customers to the database`() {
        val customerId = UUID.randomUUID()

        val customersFile = CustomersFile(
            customers = listOf(
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

        val expectedCustomerEntities = listOf(
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

        customerImportService.importCustomers(customersFile)

        verify(customerRepository).saveAll(expectedCustomerEntities)
    }

    @Test
    internal fun `should import customers to the database including the coordinates`() {
        val customerId = UUID.randomUUID()
        val address = "Berliner Strasse 1, 13189 Berlin, Germany"

        val customersFile = CustomersFile(
            customers = listOf(
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

        val expectedCustomerEntities = listOf(
            CustomerEntity(
                id = customerId,
                name = "Pizzeria Luigi Gmbh",
                commercialName = "Tratoria Luigi",
                address = address,
                storeNumber = 20,
                number = 100,
                coordinates = CustomerEntity.Coordinates(
                    latitude = 10.0,
                    longitude = 20.0
                )
            )
        )

        `when`(geoCodingService.getCoordinatesForAddress(address)).thenReturn(
            Coordinates(
                latitude = 10.0,
                longitude = 20.0
            )
        )

        customerImportService.importCustomers(customersFile)

        verify(customerRepository).saveAll(expectedCustomerEntities)
    }
}
