package ml.jonah.customerdetailsservice.controller

import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.service.CustomerImportService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomerImportController(
    private val customerImportService: CustomerImportService
) {

    @PostMapping("/v1/import-customers")
    fun importCustomers(@RequestBody customersFileRequest: CustomersFile) {
        customerImportService.importCustomers(customersFileRequest)
    }
}
