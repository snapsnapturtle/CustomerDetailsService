package ml.jonah.customerdetailsservice.task

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import tools.jackson.databind.json.JsonMapper

class CustomerImportTaskTest :
    DescribeSpec({
        val importCustomersUseCase = mockk<ImportCustomersUseCase>()
        val jsonMapper = mockk<JsonMapper>()

        val customerImportTask =
            CustomerImportTask(
                importCustomersUseCase = importCustomersUseCase,
                jsonMapper = jsonMapper,
            )

        afterTest { clearAllMocks() }

        describe("importCustomersOnApplicationReady") {
            it("should call the service layer to load and import customers") {
                val customersFile = CustomersFile(customers = emptyList())
                val request = ImportCustomersUseCase.Request.FromFile(customersFile)

                every { jsonMapper.readValue(any<File>(), any<Class<CustomersFile>>()) } returns
                    customersFile

                every { importCustomersUseCase.invoke(request) } returns
                    ImportCustomersUseCase.Response.Success

                customerImportTask.importCustomersOnApplicationReady()

                verify { importCustomersUseCase.invoke(request) }
            }

            it("should not throw an error when customer import use-case fails") {
                val customersFile = CustomersFile(customers = emptyList())
                val request = ImportCustomersUseCase.Request.FromFile(customersFile)

                val expectedException = RuntimeException("Failed to process required step")

                every { jsonMapper.readValue(any<File>(), any<Class<CustomersFile>>()) } returns
                    customersFile

                every { importCustomersUseCase.invoke(request) } returns
                    ImportCustomersUseCase.Response.Failure(expectedException)

                customerImportTask.importCustomersOnApplicationReady()

                verify { importCustomersUseCase.invoke(request) }
            }
        }
    })
