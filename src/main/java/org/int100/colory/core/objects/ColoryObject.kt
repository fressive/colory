package org.int100.colory.core.objects

import java.awt.Font
import java.awt.Graphics2D

abstract class ColoryObject {
    abstract fun render(graphic: Graphics2D, fontCache: Map<String, Font>): Graphics2D
}