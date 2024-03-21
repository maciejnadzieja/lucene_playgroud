package com.github.maciejnadzieja

import com.fasterxml.jackson.annotation.JsonProperty

data class CsvLine(
    @field:JsonProperty("id") val id: Int,
    @field:JsonProperty("title") val title: String,
    @field:JsonProperty("ingredients") val ingredients: String,
    @field:JsonProperty("directions") val directions: String,
    @field:JsonProperty("link") val link: String,
    @field:JsonProperty("source") val source: String,
    @field:JsonProperty("ner") val ner: String,
) {
    fun toRecipe(): Recipe {
        return Recipe(
            id,
            title,
            asList(ingredients),
            asList(directions),
            link,
            source,
            asList(ner)
        )
    }

    companion object {
        private val listRegex = "[\\[\\]\\\"]".toRegex()
    }

    private fun asList(columnValue: String): List<String> {
        return columnValue.replace(listRegex, "").split(",").map { it.trim() }
    }

    @Suppress("unused")
    constructor() : this(-1, "missing", "[]", "[]", "missing", "missing", "[]")
}