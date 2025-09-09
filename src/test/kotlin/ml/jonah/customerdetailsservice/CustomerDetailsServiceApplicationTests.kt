package ml.jonah.customerdetailsservice

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import ml.jonah.customerdetailsservice.task.CustomerImportTask
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ApplyExtension(SpringExtension::class)
class CustomerDetailsServiceApplicationTests(
    @MockkBean(relaxed = true) private val customerImportTask: CustomerImportTask
) :
    DescribeSpec({
        beforeTest { every { customerImportTask.importCustomersOnApplicationReady() } just runs }

        afterTest { clearAllMocks() }

        describe("application") { it("should load context") {} }
    })
