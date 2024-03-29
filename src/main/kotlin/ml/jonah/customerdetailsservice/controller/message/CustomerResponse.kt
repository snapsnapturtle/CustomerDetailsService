package ml.jonah.customerdetailsservice.controller.message

import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val name: String?,
    val commercialName: String?,
    val address: String?,
    val storeNumber: Int?,
    val number: Int?,
    val coordinates: Coordinates?
) {
    data class Coordinates(val latitude: Double?, val longitude: Double?)
}
