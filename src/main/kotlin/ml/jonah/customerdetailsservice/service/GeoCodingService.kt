package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingServiceClient
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import org.springframework.stereotype.Service

@Service
class GeoCodingService(
    private val geoCodingServiceClient: GeoCodingServiceClient
) {
    fun getCoordinatesForAddress(address: String): Coordinates {
        val addressRequest = AddressRequest(
            address = address
        )

        // future: add error handling when the address cannot be resolved
        return geoCodingServiceClient.getCoordinatesForAddress(addressRequest).toGeoLocation()
    }

    private fun GeoCodingResponse.toGeoLocation() = Coordinates(
        latitude = latitude,
        longitude = longitude
    )
}
