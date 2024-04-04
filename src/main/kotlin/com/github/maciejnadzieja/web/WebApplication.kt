package com.github.maciejnadzieja.web

import com.github.maciejnadzieja.SearchService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@SpringBootApplication
@ConfigurationPropertiesScan
open class WebApplication

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}

@Configuration
open class Configuration {
    @Bean
    open fun search(): SearchService {
        return SearchService()
    }
}