package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCaseTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.io.File


@ExtendWith(MockitoExtension::class)
internal class CustomerImportTaskTest {
    @InjectMocks
    private lateinit var customerImportTask: CustomerImportTask

    @Mock
    private lateinit var importCustomersUseCase: ImportCustomersUseCase

    @Mock
    private lateinit var objectMapper: ObjectMapper

    @Test
    internal fun `should call the service layer to load and import customers`() {
        val customersFile = CustomersFile(
                customers = emptyList()
        )

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        `when`(objectMapper.readValue(any<File>(), any<Class<CustomersFile>>())).thenReturn(customersFile)
        `when`(importCustomersUseCase.invoke(request)).thenReturn(ImportCustomersUseCase.Response.Success)

        customerImportTask.importCustomersOnApplicationReady()

        verify(importCustomersUseCase).invoke(request)
    }

    @Test
    internal fun `should not throw an error when customer import use-case fails`() {
        val customersFile = CustomersFile(
                customers = emptyList()
        )

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        val expectedException = RuntimeException("Failed to process required step")

        `when`(objectMapper.readValue(any<File>(), any<Class<CustomersFile>>())).thenReturn(customersFile)
        `when`(importCustomersUseCase.invoke(request)).thenReturn(ImportCustomersUseCase.Response.Failure(expectedException))

        customerImportTask.importCustomersOnApplicationReady()

        verify(importCustomersUseCase).invoke(request)
    }
}
