package ml.jonah.customerdetailsservice.controller

import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class CustomerImportController(private val importCustomersUseCase: ImportCustomersUseCase) {

    @PostMapping("/v1/import-customers")
    fun importCustomers(@RequestBody customersFileRequest: CustomersFile): ResponseEntity<Unit> {
        val request = ImportCustomersUseCase.Request.FromFile(customersFileRequest)

        return when (val response = importCustomersUseCase.invoke(request)) {
            is ImportCustomersUseCase.Response.Success ->
                ResponseEntity.status(HttpStatus.OK).build()
            is ImportCustomersUseCase.Response.Failure -> {
                logger.error {
                    "Failed to import customers for request <$customersFileRequest>: ${response.exception}"
                }

                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}
