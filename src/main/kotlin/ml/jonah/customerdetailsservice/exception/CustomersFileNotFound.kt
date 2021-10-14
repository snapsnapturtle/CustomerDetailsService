package ml.jonah.customerdetailsservice.exception

class CustomersFileNotFound(location: String) : RuntimeException("Customers file <$location> was not found.")
