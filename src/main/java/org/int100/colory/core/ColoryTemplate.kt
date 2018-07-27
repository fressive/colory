package org.int100.colory.core

import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.int100.colory.core.entity.Font
import org.int100.colory.core.entity.Vec2
import org.int100.colory.core.objects.ColoryImage
import org.int100.colory.core.objects.ColoryObject
import org.int100.colory.core.objects.ColoryText
import org.int100.colory.core.util.TextFormatter
import java.awt.Color
import java.net.URI
import java.net.URL

class ColoryTemplate (val path: String) {
    private var width = -1
    private var height = -1

    private var fonts = mutableListOf<Font>()

    private var root: Element? = null

    fun parseTemplate(params: Map<String, Any> = mapOf()): ColoryTemplate {
        val reader = SAXReader().read(path)
        this.root = reader.rootElement

        readPreparations(this.root!!.element("preparations"))

        this.width = TextFormatter.format(root!!.element("width").text, params).toInt()
        this.height = TextFormatter.format(root!!.element("height").text, params).toInt()

        return this
    }

    fun getRender(params: Map<String, Any> = mapOf()): ColoryRender {
        val objects = mutableListOf<ColoryObject>()
        this.root!!.element("objects").elements().forEach {
            val name = (it as Element).qualifiedName
            val pos = Vec2(it.formattedGet("x", params = params).toInt(), it.formattedGet("y", params = params).toInt())

            when (name) {
                "text" ->
                    objects.add(ColoryText(
                            TextFormatter.format(it.text.trim().trim(' '), params),
                            pos,
                            Color(it.formattedGet("color", params, "#00000000").substring(1).toInt(16)),
                            it.formattedGet("font", params, "Microsoft YaHei"),
                            it.formattedGet("fontsize", params, "16").toFloat()))
                "image" ->
                    objects.add(ColoryImage(
                            URI(TextFormatter.format(it.text.trim().trim(' '), params)).toURL(),
                            pos,
                            it.formattedGet("alpha", params, "0").toInt(),
                            it.formattedGet("width", params, "-1").toInt(),
                            it.formattedGet("height", params, "-1").toInt(),
                            it.formattedGet("scale", params, "1.0").toFloat(),
                            it.formattedGet("cached", params, "true").toBoolean()
                    ))
            }
        }

        return ColoryRender(this, objects)
    }

    private fun Element.formattedGet(name: String, params: Map<String, Any>, default: String = ""): String {
        return TextFormatter.format(this.attributeValue(name, default), params)
    }

    private fun readPreparations(element: Element) {
        element.elements().forEach {
            when ((it as Element).qName.name) {
                "font" -> { fonts.add(Font(it.attributeValue("path"), it.attributeValue("name"))) }
            }
        }
    }


    fun getWidth() = this.width
    fun getHeight() = this.height
    fun getFonts() = this.fonts
}