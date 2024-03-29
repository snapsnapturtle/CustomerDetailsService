package ml.jonah.customerdetailsservice.usecase

import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.entity.CustomerEntity
import ml.jonah.customerdetailsservice.repository.CustomerRepository
import ml.jonah.customerdetailsservice.service.GeoCodingService
import mu.KotlinLogging
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class ImportCustomersUseCase(
        private val customerRepository: CustomerRepository,
        private val geoCodingService: GeoCodingService
) {
    operator fun invoke(request: Request): Response {
        try {
            val customersToImport = when (request) {
                is Request.FromFile -> request.customersFile.customers
            }

            val customers = customersToImport.map { customerEntry ->
                val resolvedLocation = customerEntry.address?.let {
                    val address = geoCodingService.getCoordinatesForAddress(it)
                    logger.info { "Resolved address for customer <${customerEntry.id}> to <$address>" }

                    address
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

            logger.info { "Importing <${customers.size}> customer(s) to database" }

            customerRepository.saveAll(customers)

            return Response.Success
        } catch (exception: Exception) {
            return Response.Failure(exception)
        }
    }

    private fun Coordinates.toCoordinates() = CustomerEntity.Coordinates(
            latitude = latitude,
            longitude = longitude
    )

    sealed interface Request {
        data class FromFile(val customersFile: CustomersFile) : Request
    }

    sealed interface Response {
        data object Success : Response
        data class Failure(val exception: Exception) : Response
    }
}
