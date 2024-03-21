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


class App {

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

    fun runQuery() {
        val analyzer = StandardAnalyzer()
        val index = MMapDirectory(Path("index"))

        val queryString = "onion"
        val query = QueryParser("ner", analyzer).parse(queryString)
        val searcher = IndexSearcher(DirectoryReader.open(index))
        val result = searcher.search(query, 10)
        result.scoreDocs.forEach {
            println(searcher.storedFields().document(it.doc).get("title"))
        }
    }
}

fun main() {
    val app = App()
    app.buildIndex()
    app.runQuery()
}