package ml.jonah.customerdetailsservice.datatransfer

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class CustomersFile(val customers: List<Customer>) {
    data class Customer(
        @JsonProperty("Customer UUID", required = true) val id: UUID,
        @JsonProperty("Customer Name") val name: String?,
        @JsonProperty("Customer Commercial Name") val commercialName: String?,
        @JsonProperty("Customer Address") val address: String?,
        @JsonProperty("Customer Store Number") val storeNumber: Int?,
        @JsonProperty("Customer Number") val number: Int?
    )
}
