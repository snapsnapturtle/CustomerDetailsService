package ml.jonah.customerdetailsservice.exception

import java.util.UUID

class CustomerNotFoundException(customerId: UUID) :
    RuntimeException("Customer with id <$customerId> does not exist.")
