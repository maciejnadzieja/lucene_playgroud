package com.github.maciejnadzieja.web

import com.github.maciejnadzieja.recipesearch.SearchService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class WebConfiguration {
    @Bean
    open fun search(): SearchService {
        return SearchService()
    }
}
