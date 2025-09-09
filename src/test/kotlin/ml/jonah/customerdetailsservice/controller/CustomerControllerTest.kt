package ml.jonah.customerdetailsservice.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.extensions.ApplyExtension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import java.util.*
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.service.CustomerService
import ml.jonah.customerdetailsservice.task.CustomerImportTask
import org.hamcrest.Matchers
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ApplyExtension(SpringExtension::class)
class CustomerControllerTest(
    @MockkBean private val customerService: CustomerService,
    @MockkBean(relaxed = true) private val customerImportTask: CustomerImportTask,
    private val mockMvc: MockMvc
) :
    DescribeSpec({
        beforeTest { every { customerImportTask.importCustomersOnApplicationReady() } just runs }

        afterTest { clearAllMocks() }

        describe("getCustomers") {
            it("should return a paged list of customers") {
                val customerId = UUID.randomUUID()
                val customerEntities =
                    mutableListOf(
                        CustomerEntity(
                            id = customerId,
                            name = "Pizzeria Luigi Gmbh",
                            commercialName = "Tratoria Luigi",
                            address = null,
                            storeNumber = 20,
                            number = 100,
                            coordinates = null
                        )
                    )

                val pageable = PageRequest.of(0, 15)

                val customerEntitiesPage = PageImpl(customerEntities, pageable, 1)

                every { customerService.getAllCustomers(pageable) } returns customerEntitiesPage

                val result =
                    mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/customers")
                            .param("page", "0")
                            .param("size", "15")
                    )

                result
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(MockMvcResultMatchers.jsonPath("content.length()", Matchers.`is`(1)))
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "content[0].id",
                            Matchers.`is`(customerId.toString())
                        )
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "content[0].name",
                            Matchers.`is`("Pizzeria Luigi Gmbh")
                        )
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "content[0].commercialName",
                            Matchers.`is`("Tratoria Luigi")
                        )
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("content[0].storeNumber", Matchers.`is`(20))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("content[0].number", Matchers.`is`(100))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("pageMetadata.size", Matchers.`is`(15))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "pageMetadata.totalElements",
                            Matchers.`is`(1)
                        )
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("pageMetadata.totalPages", Matchers.`is`(1))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("pageMetadata.number", Matchers.`is`(0))
                    )
            }
        }

        describe("getCustomerById") {
            it("should return customer response for a single customer") {
                val customerId = UUID.randomUUID()
                val customerEntity =
                    CustomerEntity(
                        id = customerId,
                        name = "Pizzeria Luigi Gmbh",
                        commercialName = "Tratoria Luigi",
                        address = "Berliner Straße 1, 22102 Hamburg, Germany",
                        storeNumber = 20,
                        number = 100,
                        coordinates = CustomerEntity.Coordinates(latitude = 20.0, longitude = 30.0)
                    )

                every { customerService.getCustomerById(customerId) } returns customerEntity

                val result =
                    mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/customers/{customerId}", customerId)
                    )

                result
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("id", Matchers.`is`(customerId.toString()))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("name", Matchers.`is`("Pizzeria Luigi Gmbh"))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "commercialName",
                            Matchers.`is`("Tratoria Luigi")
                        )
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath(
                            "address",
                            Matchers.`is`("Berliner Straße 1, 22102 Hamburg, Germany")
                        )
                    )
                    .andExpect(MockMvcResultMatchers.jsonPath("storeNumber", Matchers.`is`(20)))
                    .andExpect(MockMvcResultMatchers.jsonPath("number", Matchers.`is`(100)))
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("coordinates.latitude", Matchers.`is`(20.0))
                    )
                    .andExpect(
                        MockMvcResultMatchers.jsonPath("coordinates.longitude", Matchers.`is`(30.0))
                    )
            }
        }

        describe("handleCustomerNotFoundException") {
            it("should return response status 404 when customer does not exist") {
                val customerId = UUID.randomUUID()

                every { customerService.getCustomerById(customerId) } throws
                    CustomerNotFoundException(customerId)

                val result =
                    mockMvc.perform(
                        MockMvcRequestBuilders.get("/v1/customers/{customerId}", customerId)
                    )

                result.andExpect(MockMvcResultMatchers.status().isNotFound)
            }
        }
    })
