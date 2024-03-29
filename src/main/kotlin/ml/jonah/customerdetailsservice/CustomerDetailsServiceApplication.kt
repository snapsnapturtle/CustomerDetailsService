package ml.jonah.customerdetailsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class CustomerDetailsServiceApplication

fun main(args: Array<String>) {
    runApplication<CustomerDetailsServiceApplication>(*args)
}
