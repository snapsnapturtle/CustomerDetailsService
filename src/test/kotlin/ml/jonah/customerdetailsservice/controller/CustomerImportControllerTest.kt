package ml.jonah.customerdetailsservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import java.util.*
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(CustomerImportController::class)
internal class CustomerImportControllerTest {
    @Autowired private lateinit var mockMvc: MockMvc

    @Autowired private lateinit var objectMapper: ObjectMapper

    @MockkBean private lateinit var importCustomersUseCase: ImportCustomersUseCase

    @Test
    internal fun `should import customers when requesting through endpoint`() {
        val customersFile =
            CustomersFile(
                customers =
                    listOf(
                        CustomersFile.Customer(
                            id = UUID.randomUUID(),
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = "Berliner Strasse 1, 13189 Berlin, Germany",
                            storeNumber = 20,
                            number = 100
                        )
                    )
            )

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)

        every { importCustomersUseCase.invoke(request) } returns
            ImportCustomersUseCase.Response.Success

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/import-customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(customersFile))
            )

        result.andExpect(status().isOk)

        verify { importCustomersUseCase.invoke(request) }
    }

    @Test
    internal fun `should return an error when the customers cannot be imported`() {
        val customersFile =
            CustomersFile(
                customers =
                    listOf(
                        CustomersFile.Customer(
                            id = UUID.randomUUID(),
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = "Berliner Strasse 1, 13189 Berlin, Germany",
                            storeNumber = 20,
                            number = 100
                        )
                    )
            )

        val request = ImportCustomersUseCase.Request.FromFile(customersFile)
        val expectedException = RuntimeException("Failed to process required step")

        every { importCustomersUseCase.invoke(request) } returns
            ImportCustomersUseCase.Response.Failure(expectedException)

        val result =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/import-customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(customersFile))
            )

        result.andExpect(status().isInternalServerError)

        verify { importCustomersUseCase.invoke(request) }
    }
}
