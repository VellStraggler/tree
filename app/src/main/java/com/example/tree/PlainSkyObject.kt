package com.example.tree

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap

open class PlainSkyObject(var name: String, var image: ImageBitmap, var offset: Offset) {
    operator fun component1(): ImageBitmap {
        return image
    }
    operator fun component2(): Offset {
        return offset
    }
    fun updateOffset(x: Float, y: Float) {
        offset = Offset(x,y)
    }
    open fun move() {
        //do nothing
    }
}