package com.github.maciejnadzieja.web

import com.github.maciejnadzieja.recipesearch.SearchService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SearchController(
    private val searchService: SearchService,
) {
    @GetMapping("/")
    fun index(model: Model): String {
        return "index"
    }

    @PostMapping("/search")
    fun search(
        model: Model,
        @RequestParam search: String,
    ): String {
        val recipes = searchService.runQuery(search)
        model.addAttribute("recipes", recipes)
        return "search"
    }

    @PostMapping("/buildIndex")
    fun search(model: Model): String {
        searchService.buildIndex()
        return "redirect:/"
    }
}
