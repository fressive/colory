package org.int100.colory.core

import org.int100.colory.core.objects.ColoryObject
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ColoryRender (private val template: ColoryTemplate, private val objects: List<ColoryObject>) {
    private val fontCache = mutableMapOf<String, Font>()

    fun renderImage(): BufferedImage {
        if (template.getWidth() == -1 || template.getHeight()  == -1)
            throw Exception("Width or height of image was not initialized.")

        for (f in template.getFonts())
            fontCache[f.fontName] = Font.createFont(Font.TRUETYPE_FONT, File(f.fontPath))

        val image = BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB)

        var g2d = image.createGraphics()
        objects.forEach {
            g2d = it.render(g2d, fontCache)
        }

        image.flush()
        g2d.dispose()
        return image
    }

    fun saveImage(path: String, formatName: String = "png") {
        ImageIO.write(renderImage(), formatName, File(path))
    }
}