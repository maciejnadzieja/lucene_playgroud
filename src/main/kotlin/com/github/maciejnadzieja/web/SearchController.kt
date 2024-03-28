package com.github.maciejnadzieja.web

import com.github.maciejnadzieja.Recipe
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SearchController {
    @GetMapping("/")
    fun index(model: Model): String {
        return "index"
    }

    @PostMapping("/search")
    fun search(model: Model, @RequestParam search: String): String {
        model.addAttribute("recipes", listOf(Recipe(0, "pasta", listOf("onion"), emptyList(), "", "", emptyList())))
        return "search"
    }
}