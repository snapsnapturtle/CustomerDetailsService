package ml.jonah.customerdetailsservice.controller

import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.service.CustomerService
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@ExtendWith(SpringExtension::class)
@WebMvcTest(CustomerController::class)
internal class CustomerControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var customerService: CustomerService

    @Test
    internal fun `should return customer response for a single customer`() {
        val customerId = UUID.randomUUID()
        val customerEntity = CustomerEntity(
            id = customerId,
            name = "Pizzeria Luigi Gmbh",
            commercialName = "Tratoria Luigi",
            address = "Berliner Straße 1, 22102 Hamburg, Germany",
            storeNumber = 20,
            number = 100,
            coordinates = CustomerEntity.Coordinates(
                latitude = 20.0,
                longitude = 30.0
            )
        )

        `when`(customerService.getCustomerById(customerId)).thenReturn(customerEntity)

        val result = mockMvc.perform(get("/v1/customers/{customerId}", customerId))

        result.andExpect(status().isOk)
            .andExpect(jsonPath("id", `is`(customerId.toString())))
            .andExpect(jsonPath("name", `is`("Pizzeria Luigi Gmbh")))
            .andExpect(jsonPath("commercialName", `is`("Tratoria Luigi")))
            .andExpect(jsonPath("address", `is`("Berliner Straße 1, 22102 Hamburg, Germany")))
            .andExpect(jsonPath("storeNumber", `is`(20)))
            .andExpect(jsonPath("number", `is`(100)))
            .andExpect(jsonPath("coordinates.latitude", `is`(20.0)))
            .andExpect(jsonPath("coordinates.longitude", `is`(30.0)))

    }

    @Test
    internal fun `should return response status 404 when customer does not exist`() {
        val customerId = UUID.randomUUID()

        `when`(customerService.getCustomerById(customerId)).thenThrow(CustomerNotFoundException(customerId))

        val result = mockMvc.perform(get("/v1/customers/{customerId}", customerId))

        result.andExpect(status().isNotFound)
    }
}
