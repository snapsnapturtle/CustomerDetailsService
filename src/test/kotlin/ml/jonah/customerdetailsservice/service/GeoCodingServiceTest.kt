package ml.jonah.customerdetailsservice.service

import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingServiceClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GeoCodingServiceTest {
    @InjectMocks private lateinit var geoCodingService: GeoCodingService

    @Mock private lateinit var geoCodingServiceClient: GeoCodingServiceClient

    @Test
    internal fun `should use feign client to resolve an address`() {
        val address = "Große Elbstraße 273, 22767 Hamburg, Germany"
        val geoCodingResponse = GeoCodingResponse(latitude = 20.0, longitude = 30.0)

        val expectedGeoLocation = Coordinates(latitude = 20.0, longitude = 30.0)

        `when`(geoCodingServiceClient.getCoordinatesForAddress(AddressRequest(address)))
            .thenReturn(geoCodingResponse)

        val actualGeoLocation = geoCodingService.getCoordinatesForAddress(address)

        assertThat(actualGeoLocation).isEqualTo(expectedGeoLocation)
    }
}
