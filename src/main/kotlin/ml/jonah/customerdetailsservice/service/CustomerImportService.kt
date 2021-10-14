package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.controller.CustomerImportController
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class CustomerImportService(
    private val customerRepository: CustomerRepository
) {
    val logger: Logger = LoggerFactory.getLogger(CustomerImportController::class.java)

    fun importCustomers(customersFile: CustomersFile) {
        val customers = customersFile.customers.map {
            CustomerEntity(
                id = it.id,
                name = it.name,
                commercialName = it.commercialName,
                address = it.address,
                storeNumber = it.storeNumber,
                number = it.number,
                geoLocation = null
            )
        }

        customerRepository.saveAll(customers)

        logger.info("Successfully imported ${customers.size} customer(s).")
    }
}
