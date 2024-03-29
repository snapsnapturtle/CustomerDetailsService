package ml.jonah.customerdetailsservice.controller

import ml.jonah.customerdetailsservice.controller.message.CustomerResponse
import ml.jonah.customerdetailsservice.controller.message.CustomersResponse
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.exception.CustomerNotFoundException
import ml.jonah.customerdetailsservice.service.CustomerService
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

private val logger = KotlinLogging.logger {}

@RestController
class CustomerController(
    private val customerService: CustomerService
) {
    @GetMapping("/v1/customers")
    fun getCustomers(@PageableDefault(size = 10) pageable: Pageable): CustomersResponse {
        val customersPage = customerService.getAllCustomers(pageable)

        return CustomersResponse(
            content = customersPage.content.map { it.toCustomerResponse() },
            pageMetadata = CustomersResponse.PageMetadata(
                size = customersPage.size,
                totalElements = customersPage.totalElements,
                totalPages = customersPage.totalPages,
                number = customersPage.number
            )
        )
    }

    @GetMapping("/v1/customers/{customerId}")
    fun getCustomerById(@PathVariable customerId: UUID): CustomerResponse {
        val customer = customerService.getCustomerById(customerId)

        return customer.toCustomerResponse()
    }

    @ExceptionHandler(CustomerNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleCustomerNotFoundException(ex: CustomerNotFoundException) {
        logger.info(ex.message)
    }

    private fun CustomerEntity.toCustomerResponse() = CustomerResponse(
        id = id,
        name = name,
        commercialName = commercialName,
        address = address,
        storeNumber = storeNumber,
        number = number,
        coordinates = coordinates?.let {
            CustomerResponse.Coordinates(
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    )
}
