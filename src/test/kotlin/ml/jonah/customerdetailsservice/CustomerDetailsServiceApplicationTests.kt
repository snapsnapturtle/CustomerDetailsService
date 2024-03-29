package ml.jonah.customerdetailsservice

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import ml.jonah.customerdetailsservice.task.CustomerImportTask
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CustomerDetailsServiceApplicationTests {

    @MockkBean(relaxed = true) private lateinit var customerImportTask: CustomerImportTask

    @Test internal fun contextLoads() {}

    @Test
    internal fun `should load and import customer file on application ready`() {
        verify { customerImportTask.importCustomersOnApplicationReady() }
    }
}
