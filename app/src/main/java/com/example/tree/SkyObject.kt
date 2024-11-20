package com.example.tree

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap

class SkyObject(name: String, image: ImageBitmap, offset: Offset, val diam: Int): PlainSkyObject(name, image, offset){
    /*Moves over one spot*/
    override fun move() {
        offset = if (offset.x > 350) Offset(50F, offset.y)
        else Offset(offset.x + 300, offset.y)
    }
    override fun toString(): String {
        return ("$name, $offset, $diam")
    }

}