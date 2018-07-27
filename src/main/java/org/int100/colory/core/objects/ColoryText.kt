package org.int100.colory.core.objects

import org.int100.colory.core.entity.Vec2
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D

class ColoryText (
        private val text: String,
        private val pos: Vec2,
        private val color: Color = Color.BLACK,

        private val fontName: String = "Microsoft YaHei",
        private val fontSize: Float = 16F
) : ColoryObject() {

    override fun render(graphic: Graphics2D, fontCache: Map<String, Font>): Graphics2D {
        graphic.color = this.color

        // custom font
        if (fontName.startsWith("#")) {
            for (k in fontCache.keys) {
                if (k == fontName.substring(1)) graphic.font = fontCache[fontName.substring(1)]!!.deriveFont(fontSize)
            }
        } else {
            graphic.font = Font(fontName, 0, 1).deriveFont(fontSize)
        }

        graphic.drawString(text, pos.x, pos.y)

        return graphic
    }
}