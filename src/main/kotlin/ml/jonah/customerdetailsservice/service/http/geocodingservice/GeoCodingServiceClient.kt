package ml.jonah.customerdetailsservice.service.http.geocodingservice

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("GeoCodingService", url = "\${http.geocodingservice.url}")
interface GeoCodingServiceClient {

    @PostMapping("/geocode")
    fun getCoordinatesForAddress(@RequestBody addressRequest: AddressRequest): GeoCodingResponse
}
