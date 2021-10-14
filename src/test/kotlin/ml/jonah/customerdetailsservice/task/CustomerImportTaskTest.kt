package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import ml.jonah.customerdetailsservice.service.CustomerImportService
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired


@ExtendWith(MockitoExtension::class)
internal class CustomerImportTaskTest {
    @InjectMocks
    private lateinit var customerImportTask: CustomerImportTask

    @Mock
    private lateinit var customerImportService: CustomerImportService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    @Disabled
    internal fun `should call the service layer to load and import customers`() {
        customerImportTask.importCustomersOnApplicationReady()

        TODO("complete test and mock object mapper")
    }
}
