package ml.jonah.customerdetailsservice.configuration

import ml.jonah.customerdetailsservice.CustomerDetailsServiceApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [CustomerDetailsServiceApplication::class])
class FeignConfiguration
