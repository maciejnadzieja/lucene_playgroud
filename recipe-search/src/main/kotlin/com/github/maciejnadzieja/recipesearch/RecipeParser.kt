package com.github.maciejnadzieja.recipesearch

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import java.io.InputStream

class RecipeParser(inputStream: InputStream) : Iterator<Recipe> {
    private val mapper = CsvMapper().apply {
        enable(CsvParser.Feature.TRIM_SPACES)
        enable(CsvParser.Feature.SKIP_EMPTY_LINES)
    }

    private val schema = CsvSchema.builder()
        .addNumberColumn("id")
        .addColumn("title")
        .addColumn("ingredients")
        .addColumn("directions")
        .addColumn("link")
        .addColumn("source")
        .addColumn("ner")
        .build()

    private val reader = mapper.readerFor(CsvLine::class.java)
        .with(schema.withSkipFirstDataRow(true))
        .readValues<CsvLine>(inputStream)

    override fun hasNext(): Boolean {
        return reader.hasNext()
    }

    override fun next(): Recipe {
        return reader.next().toRecipe()
    }
}