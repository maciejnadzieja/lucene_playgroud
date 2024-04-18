package com.github.maciejnadzieja.recipesearch

import mu.KotlinLogging
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.IntField
import org.apache.lucene.document.StringField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.BoostQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.MMapDirectory
import kotlin.io.path.Path

class SearchService {
    private val log = KotlinLogging.logger {}

    fun buildIndex() {
        val analyzer = StandardAnalyzer()
        val index = MMapDirectory(Path("index"))

        this::class.java.getResourceAsStream("/full_dataset.csv")!!.use { inputStream ->
            IndexWriter(index, IndexWriterConfig(analyzer)).use { writer ->
                RecipeParser(inputStream).asSequence().forEach { recipe ->
                    writer.addDocument(
                        Document().apply {
                            log.info("indexing $recipe")
                            add(IntField("id", recipe.id, Field.Store.YES))
                            add(TextField("title", recipe.title, Field.Store.YES))
                            recipe.ingredients.forEach {
                                add(StringField("ingredients", it, Field.Store.YES))
                            }
                            recipe.directions.forEach {
                                add(StringField("directions", it, Field.Store.YES))
                            }
                            add(TextField("link", recipe.link, Field.Store.YES))
                            add(TextField("source", recipe.source, Field.Store.YES))
                            recipe.ner.forEach {
                                add(StringField("ner", it, Field.Store.YES))
                            }
                        },
                    )
                }
            }
        }
    }

    fun runQuery(queryString: String): List<Recipe> {
        val analyzer = StandardAnalyzer()
        val index = MMapDirectory(Path("index"))

        val query =
            BooleanQuery.Builder()
                .add(BoostQuery(QueryParser("title", analyzer).parse(queryString), 3.0f), BooleanClause.Occur.SHOULD)
                .add(QueryParser("ingredients", analyzer).parse(queryString), BooleanClause.Occur.SHOULD)
                .add(BoostQuery(QueryParser("ner", analyzer).parse(queryString), 2.0f), BooleanClause.Occur.SHOULD)
                .build()
        val searcher = IndexSearcher(DirectoryReader.open(index))
        val result = searcher.search(query, 20)
        return result.scoreDocs.map {
            val doc = searcher.storedFields().document(it.doc)
            Recipe(
                id = doc.get("id").toInt(),
                title = doc.get("title"),
                ingredients = doc.getValues("ingredients").toList(),
                directions = doc.getValues("directions").toList(),
                link = "https://${doc.get("link")}",
                source = doc.get("source"),
                ner = doc.getValues("ner").toList(),
            )
        }
    }
}

fun main() {
    SearchService().buildIndex()
}
