package ml.jonah.customerdetailsservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.*
import java.util.*
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.task.CustomerImportTask
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ApplyExtension(SpringExtension::class)
class CustomerImportControllerTest(
    @MockkBean private val importCustomersUseCase: ImportCustomersUseCase,
    @MockkBean(relaxed = true) private val customerImportTask: CustomerImportTask,
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) :
    DescribeSpec({
        beforeTest { every { customerImportTask.importCustomersOnApplicationReady() } just runs }

        afterTest { clearAllMocks() }

        describe("importCustomers") {
            it("should import customers when requesting through endpoint") {
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

            it("should return an error when the customers cannot be imported") {
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
    })
