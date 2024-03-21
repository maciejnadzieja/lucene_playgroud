package com.github.maciejnadzieja

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String>,
    val directions: List<String>,
    val link: String,
    val source: String,
    val ner: List<String>
)