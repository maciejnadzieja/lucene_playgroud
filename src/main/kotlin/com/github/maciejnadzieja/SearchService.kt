package com.github.maciejnadzieja

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.MMapDirectory
import kotlin.io.path.Path


class SearchService {

    fun buildIndex() {
        val analyzer = StandardAnalyzer()
        val index = MMapDirectory(Path("index"))

        this::class.java.getResourceAsStream("/full_dataset.csv")!!.use { inputStream ->
            IndexWriter(index, IndexWriterConfig(analyzer)).use { writer ->
                RecipeParser(inputStream).asSequence().forEach { recipe ->
                    writer.addDocument(Document().apply {
                        add(TextField("title", recipe.title, Field.Store.YES))
                        add(TextField("ner", recipe.ner.joinToString(), Field.Store.YES))
                    })
                }
            }
        }
    }

    fun runQuery(queryString: String): List<Recipe> {
        val analyzer = StandardAnalyzer()
        val index = MMapDirectory(Path("index"))

        val query = QueryParser("ner", analyzer).parse(queryString)
        val searcher = IndexSearcher(DirectoryReader.open(index))
        val result = searcher.search(query, 20)
        return result.scoreDocs.map {
            Recipe(
                id = it.doc,
                title = searcher.storedFields().document(it.doc).get("title"),
                ingredients = emptyList(),
                directions = emptyList(),
                link = "link",
                source = "source",
                ner = emptyList()
            )
        }
    }
}