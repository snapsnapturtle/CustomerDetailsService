package ml.jonah.customerdetailsservice.task

import com.fasterxml.jackson.databind.ObjectMapper
import ml.jonah.customerdetailsservice.datatransfer.CustomersFile
import ml.jonah.customerdetailsservice.exception.CustomersFileNotFound
import ml.jonah.customerdetailsservice.service.CustomerImportService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils

@Component
class CustomerImportTask(
    private val customerImportService: CustomerImportService,
    private val objectMapper: ObjectMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(CustomerImportTask::class.java)
    private val customersFileLocation = "classpath:static/customers.json"

    @EventListener(ApplicationReadyEvent::class)
    fun importCustomersOnApplicationReady() {
        logger.info("Importing customers from file on application start")
        val customersFile = loadCustomersFromFile()
        customerImportService.importCustomers(customersFile)
    }

    // load customers from file, this can be replaced with a call to a remote service
    private fun loadCustomersFromFile(): CustomersFile {
        val file = ResourceUtils.getFile(customersFileLocation)

        if (!file.exists()) {
            throw CustomersFileNotFound(customersFileLocation)
        }

        return objectMapper.readValue(file, CustomersFile::class.java)
    }
}
