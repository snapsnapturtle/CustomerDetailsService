package ml.jonah.customerdetailsservice.controller.message

data class CustomersResponse(val content: List<CustomerResponse>, val pageMetadata: PageMetadata) {
    data class PageMetadata(
        val size: Int,
        val totalElements: Long,
        val totalPages: Int,
        val number: Int
    )
}
