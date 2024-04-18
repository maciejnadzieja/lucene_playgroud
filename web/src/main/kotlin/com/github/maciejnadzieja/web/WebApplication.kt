package com.github.maciejnadzieja.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
open class WebApplication

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}
