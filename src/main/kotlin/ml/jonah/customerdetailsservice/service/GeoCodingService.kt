package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingServiceClient
import org.springframework.stereotype.Service

@Service
class GeoCodingService(private val geoCodingServiceClient: GeoCodingServiceClient) {
    fun getCoordinatesForAddress(address: String): Coordinates {
        val addressRequest = AddressRequest(address = address)

        // future: handle errors when the address cannot be resolved i.e. service returns 404
        return geoCodingServiceClient.getCoordinatesForAddress(addressRequest).toGeoLocation()
    }

    private fun GeoCodingResponse.toGeoLocation() =
        Coordinates(latitude = latitude, longitude = longitude)
}
