package org.int100.colory.core.objects

import org.int100.colory.core.entity.Vec2
import java.awt.*
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

class ColoryImage (
        private val source: URL,
        private val pos: Vec2,
        private val alpha: Int = 255,
        private val width: Int = -1,
        private val height: Int = -1,
        private val scale: Float = 1.0F,
        private val cached: Boolean = true
) : ColoryObject () {
    companion object {
        val imageCache = mutableMapOf<URL, BufferedImage>()
    }

    override fun render(graphic: Graphics2D, fontCache: Map<String, Font>): Graphics2D {

        var image = if (imageCache.containsKey(source)) imageCache[source]!! else ImageIO.read(source) as BufferedImage

        if (cached)
            imageCache.replace(source, image)

        if (alpha != 255)
            image = transparent(image, alpha)


        if ((width > 0 && width != image.width) || (height > 0 && height != image.height))
            image = resize(image, width, height)
        else if (scale != 1.0f)
            image = resize(image, scale)

        graphic.drawImage(image, pos.x, pos.y, null)

        return graphic
    }

    private fun transparent(image: BufferedImage, alpha: Int): BufferedImage {
        val new = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for (j in 0 until image.height) {
            for (i in 0 until image.width) {
                val old = Color(image.getRGB(i, j))
                val newc = Color(old.red, old.green, old.blue, alpha)
                new.setRGB(i, j, newc.rgb)
            }
        }
        return new
    }

    private fun resize(image: BufferedImage, width: Int, height: Int): BufferedImage {
        return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB).apply {
            this.graphics.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null)
        }
    }

    private fun resize(image: BufferedImage, size: Float): BufferedImage {
        val w = (image.width * size).toInt()
        val h = (image.height * size).toInt()
        return resize(image, w, h)
    }
}