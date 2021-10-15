package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.controller.CustomerImportController
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class CustomerImportService(
    private val customerRepository: CustomerRepository,
    private val geoCodingService: GeoCodingService
) {
    val logger: Logger = LoggerFactory.getLogger(CustomerImportController::class.java)

    fun importCustomers(customersFile: CustomersFile) {
        val customers = customersFile.customers.map { customerEntry ->
            val resolvedLocation = customerEntry.address?.let {
                geoCodingService.getCoordinatesForAddress(it)
            }

            CustomerEntity(
                id = customerEntry.id,
                name = customerEntry.name,
                commercialName = customerEntry.commercialName,
                address = customerEntry.address,
                storeNumber = customerEntry.storeNumber,
                number = customerEntry.number,
                coordinates = resolvedLocation?.toCoordinates()
            )
        }

        customerRepository.saveAll(customers)

        logger.info("Successfully imported ${customers.size} customer(s).")
    }

    private fun Coordinates.toCoordinates() = CustomerEntity.Coordinates(
        latitude = latitude,
        longitude = longitude
    )
}
