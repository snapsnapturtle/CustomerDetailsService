package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.exception.CustomersFileNotFound
import ml.jonah.customerdetailsservice.usecase.ImportCustomersUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils

@Component
class CustomerImportTask(
        private val importCustomersUseCase: ImportCustomersUseCase,
        private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(CustomerImportTask::class.java)
    private val customersFileLocation = "classpath:static/customers.json"

    @EventListener(ApplicationReadyEvent::class)
    fun importCustomersOnApplicationReady() {
        logger.info("Importing customers from file on application ready")

        val customersFile = loadCustomersFromFile()

        val request = ImportCustomersUseCase.Request.FromFile(
                customersFile
        )

        when (val response = importCustomersUseCase.invoke(request)) {
            is ImportCustomersUseCase.Response.Success -> logger.info("Completed customer import on application ready event")
            is ImportCustomersUseCase.Response.Failure -> logger.error("Failed to import customers on application ready event", response.exception)
        }
    }

    private fun loadCustomersFromFile(): CustomersFile {
        val file = ResourceUtils.getFile(customersFileLocation)

        if (!file.exists()) {
            throw CustomersFileNotFound(customersFileLocation)
        }

        return objectMapper.readValue(file, CustomersFile::class.java)
    }
}
