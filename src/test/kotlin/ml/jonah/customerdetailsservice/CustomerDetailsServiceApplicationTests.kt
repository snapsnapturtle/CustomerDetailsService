package ml.jonah.customerdetailsservice

import ml.jonah.customerdetailsservice.task.CustomerImportTask
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class CustomerDetailsServiceApplicationTests {

    @MockBean
    private lateinit var customerImportTask: CustomerImportTask

    @Test
    internal fun contextLoads() {
    }

    @Test
    internal fun `should load and import customer file on application ready`() {
        verify(customerImportTask).importCustomersOnApplicationReady()
    }
}
