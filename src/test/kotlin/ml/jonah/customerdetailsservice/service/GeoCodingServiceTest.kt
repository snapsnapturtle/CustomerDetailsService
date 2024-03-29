package ml.jonah.customerdetailsservice.service

import io.mockk.every
import io.mockk.mockk
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingServiceClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GeoCodingServiceTest {
    private val geoCodingServiceClient = mockk<GeoCodingServiceClient>()

    private val geoCodingService = GeoCodingService(geoCodingServiceClient = geoCodingServiceClient)

    @Test
    internal fun `should use feign client to resolve an address`() {
        val address = "Große Elbstraße 273, 22767 Hamburg, Germany"
        val geoCodingResponse = GeoCodingResponse(latitude = 20.0, longitude = 30.0)

        val expectedGeoLocation = Coordinates(latitude = 20.0, longitude = 30.0)

        every { geoCodingServiceClient.getCoordinatesForAddress(AddressRequest(address)) } returns
            geoCodingResponse

        val actualGeoLocation = geoCodingService.getCoordinatesForAddress(address)

        assertThat(actualGeoLocation).isEqualTo(expectedGeoLocation)
    }
}
