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
import org.apache.lucene.store.ByteBuffersDirectory


fun main() {
    val analyzer = StandardAnalyzer()
    val index = ByteBuffersDirectory()

    val config = IndexWriterConfig(analyzer)
    val docs = listOf(
        Document().apply {
            add(TextField("title", "waffles with smoked salmon", Field.Store.YES))
        },
        Document().apply {
            add(TextField("title", "irish potato soup", Field.Store.YES))
        },
        Document().apply {
            add(TextField("title", "sweet potato curry", Field.Store.YES))
        },
        Document().apply {
            add(TextField("title", "mashed potatos", Field.Store.YES))
        }
    )

    IndexWriter(index, config).apply {
        docs.forEach { addDocument(it) }
        close()
    }

    val queryString = "potato"
    val query = QueryParser("title", analyzer).parse(queryString)
    val searcher = IndexSearcher(DirectoryReader.open(index))
    val result = searcher.search(query, 10)
    result.scoreDocs.forEach {
        println(searcher.storedFields().document(it.doc).get("title"))
    }
}
