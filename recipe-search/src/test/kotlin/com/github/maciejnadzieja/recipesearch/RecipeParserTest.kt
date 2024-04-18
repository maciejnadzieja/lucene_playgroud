package com.github.maciejnadzieja.recipesearch

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RecipeParserTest {
    @Test
    fun `should parse recipes`() {
        // given
        val inputStream =
            """
            id,title,ingredients,directions,link,source,ner
            3,Chicken Funny,"[""1 large whole chicken"", ""2 (10 1/2 oz.) cans chicken gravy"", ""1 (10 1/2 oz.) can cream of mushroom soup"", ""1 (6 oz.) box Stove Top stuffing"", ""4 oz. shredded cheese""]","[""Boil and debone chicken."", ""Put bite size pieces in average size square casserole dish."", ""Pour gravy and cream of mushroom soup over chicken; level."", ""Make stuffing according to instructions on box (do not make too moist)."", ""Put stuffing on top of chicken and gravy; level."", ""Sprinkle shredded cheese on top and bake at 350° for approximately 20 minutes or until golden and bubbly.""]",www.cookbooks.com/Recipe-Details.aspx?id=897570,Gathered,"[""chicken"", ""chicken gravy"", ""cream of mushroom soup"", ""shredded cheese""]"
            """.trimIndent().byteInputStream()

        // when
        val recipes =
            inputStream.use {
                val parser = RecipeParser(it)
                return@use parser.asSequence().toList()
            }

        // then
        assertEquals(1, recipes.size)
        assertEquals(
            Recipe(
                3,
                "Chicken Funny",
                listOf(
                    "1 large whole chicken",
                    "2 (10 1/2 oz.) cans chicken gravy",
                    "1 (10 1/2 oz.) can cream of mushroom soup",
                    "1 (6 oz.) box Stove Top stuffing",
                    "4 oz. shredded cheese",
                ),
                listOf(
                    "Boil and debone chicken.",
                    "Put bite size pieces in average size square casserole dish.",
                    "Pour gravy and cream of mushroom soup over chicken; level.",
                    "Make stuffing according to instructions on box (do not make too moist).",
                    "Put stuffing on top of chicken and gravy; level.",
                    "Sprinkle shredded cheese on top and bake at 350° for approximately 20 minutes or until golden and bubbly.",
                ),
                "www.cookbooks.com/Recipe-Details.aspx?id=897570",
                "Gathered",
                listOf("chicken", "chicken gravy", "cream of mushroom soup", "shredded cheese"),
            ),
            recipes[0],
        )
    }
}
