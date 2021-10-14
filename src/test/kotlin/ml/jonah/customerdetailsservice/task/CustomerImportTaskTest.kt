package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.service.CustomerImportService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File


@ExtendWith(MockitoExtension::class)
internal class CustomerImportTaskTest {
    @InjectMocks
    private lateinit var customerImportTask: CustomerImportTask

    @Mock
    private lateinit var customerImportService: CustomerImportService

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `should call the service layer to load and import customers`() {
        val expectedCustomersFile = CustomersFile(
            customers = emptyList()
        )

        `when`(objectMapper.readValue(any<File>(), any<Class<CustomersFile>>())).thenReturn(expectedCustomersFile)

        customerImportTask.importCustomersOnApplicationReady()

        verify(customerImportService).importCustomers(expectedCustomersFile)
    }
}
