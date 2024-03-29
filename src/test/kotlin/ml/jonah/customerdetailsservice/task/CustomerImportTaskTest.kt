package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import org.junit.jupiter.api.Test

internal class CustomerImportTaskTest {
    private val importCustomersUseCase = mockk<ImportCustomersUseCase>()

    private val objectMapper = mockk<ObjectMapper>()

    private val customerImportTask =
        CustomerImportTask(
            importCustomersUseCase = importCustomersUseCase,
            objectMapper = objectMapper
        )

    @Test
    internal fun `should call the service layer to load and import customers`() {
        val customersFile = CustomersFile(customers = emptyList())

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        every { objectMapper.readValue(any<File>(), any<Class<CustomersFile>>()) } returns
            customersFile
        every { importCustomersUseCase.invoke(request) } returns
            ImportCustomersUseCase.Response.Success

        customerImportTask.importCustomersOnApplicationReady()

        verify { importCustomersUseCase.invoke(request) }
    }

    @Test
    internal fun `should not throw an error when customer import use-case fails`() {
        val customersFile = CustomersFile(customers = emptyList())

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val expectedException = RuntimeException("Failed to process required step")

        every { objectMapper.readValue(any<File>(), any<Class<CustomersFile>>()) } returns
            customersFile
        every { importCustomersUseCase.invoke(request) } returns
            ImportCustomersUseCase.Response.Failure(expectedException)

        customerImportTask.importCustomersOnApplicationReady()

        verify { importCustomersUseCase.invoke(request) }
    }
}
