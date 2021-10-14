package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class CustomerImportServiceTest {
    @InjectMocks
    private lateinit var customerImportService: CustomerImportService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Test
    internal fun `should import customers to the database`() {
        val customerId = UUID.randomUUID()

        val customersFile = CustomersFile(
            customers = listOf(
                CustomersFile.Customer(
                    id = customerId,
                    name = "Pizzeria Luigi Gmbh",
                    commercialName = "Tratoria Luigi",
                    address = "Berliner Strasse 1, 13189 Berlin, Germany",
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
                address = "Berliner Strasse 1, 13189 Berlin, Germany",
                storeNumber = 20,
                number = 100,
                geoLocation = null
            )
        )

        customerImportService.importCustomers(customersFile)

        verify(customerRepository).saveAll(expectedCustomerEntities)
    }
}
