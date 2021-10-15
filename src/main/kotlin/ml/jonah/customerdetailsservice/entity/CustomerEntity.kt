package ml.jonah.customerdetailsservice.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "customers")
data class CustomerEntity(
    @Id
    @Column(name = "id")
    val id: UUID,

    @Column(name = "name")
    val name: String?,

    @Column(name = "commercial_name")
    val commercialName: String?,

    @Column(name = "address")
    val address: String?,

    @Column(name = "store_number")
    val storeNumber: Int?,

    @Column(name = "number")
    val number: Int?,

    @Embedded
    val coordinates: Coordinates?
) {
    @Embeddable
    data class Coordinates(
        @Column(name = "latitude")
        var latitude: Double?,

        @Column(name = "longitude")
        var longitude: Double?
    )
}
