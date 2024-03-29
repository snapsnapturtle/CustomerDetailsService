package ml.jonah.customerdetailsservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "customers")
data class CustomerEntity(
    @Id @Column(name = "id") val id: UUID,
    @Column(name = "name") val name: String?,
    @Column(name = "commercial_name") val commercialName: String?,
    @Column(name = "address") val address: String?,
    @Column(name = "store_number") val storeNumber: Int?,
    @Column(name = "number") val number: Int?,
    @Embedded val coordinates: Coordinates?
) {
    @Embeddable
    data class Coordinates(
        @Column(name = "latitude") var latitude: Double?,
        @Column(name = "longitude") var longitude: Double?
    )
}
