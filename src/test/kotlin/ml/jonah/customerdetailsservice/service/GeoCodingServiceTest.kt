package ml.jonah.customerdetailsservice.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import ml.jonah.customerdetailsservice.datatransfer.Coordinates
import ml.jonah.customerdetailsservice.service.http.geocodingservice.AddressRequest
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingResponse
import ml.jonah.customerdetailsservice.service.http.geocodingservice.GeoCodingServiceClient

class GeoCodingServiceTest :
    DescribeSpec({
        val geoCodingServiceClient = mockk<GeoCodingServiceClient>()

        val geoCodingService = GeoCodingService(geoCodingServiceClient = geoCodingServiceClient)

        afterTest { clearAllMocks() }

        describe("getCoordinatesForAddress") {
            it("should use feign client to resolve addresses") {
                val address = "Große Elbstraße 273, 22767 Hamburg, Germany"
                val geoCodingResponse = GeoCodingResponse(latitude = 20.0, longitude = 30.0)

                val expectedGeoLocation = Coordinates(latitude = 20.0, longitude = 30.0)

                every {
                    geoCodingServiceClient.getCoordinatesForAddress(AddressRequest(address))
                } returns geoCodingResponse

                val actualGeoLocation = geoCodingService.getCoordinatesForAddress(address)

                actualGeoLocation shouldBe expectedGeoLocation
            }
        }
    })
