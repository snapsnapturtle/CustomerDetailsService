package ml.jonah.customerdetailsservice.controller

import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

/**
 * This is a mock endpoint used by the GeoCodingServiceClient to simulate http requests to a different service.
 * Therefore we are using the same data classes from the feign client (normally we would create specific data classes
 * for the controller, as seen for the CustomerController). Additionally, the endpoint design for this controller
 * was taken from the provided task pdf.
 */
@RestController
class GeoCodingController {
    private val logger: Logger = LoggerFactory.getLogger(GeoCodingController::class.java)

    @PostMapping("/geocode")
    fun getCoordinatesFromAddress(@RequestBody addressRequest: AddressRequest): GeoCodingResponse {
        logger.info("Generating random coordinates for address <${addressRequest.address}>")

        return GeoCodingResponse(
            latitude = Random.nextDouble(-90.0, 90.0),
            longitude = Random.nextDouble(-180.0, 180.0)
        )
    }
}
