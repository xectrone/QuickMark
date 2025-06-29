package com.xectrone.quickmark.domain

import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import java.util.*

object MarkdownRenderer {
    private val extensions = Arrays.asList(
        TablesExtension.create(),
        StrikethroughExtension.create()
    )
    
    private val parser = Parser.builder()
        .extensions(extensions)
        .build()
    
    private val renderer = HtmlRenderer.builder()
        .extensions(extensions)
        .build()
    
    fun renderToHtml(markdown: String): String {
        val document = parser.parse(markdown)
        return renderer.render(document)
    }
    
    fun parseMarkdown(markdown: String): Node {
        return parser.parse(markdown)
    }
} 