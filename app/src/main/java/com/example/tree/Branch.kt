package com.example.tree

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlin.math.log
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class Branch(private var origin: Offset, rotation: Double, private var dad: Branch? = null) {
    private var originPlus = Offset(0f,0f)
    private var thickness = 7.0
    private var length = thickness * 8
    private var carbon = thickness * length*2
    private var age = 0.0

    private var rotation = rotation
    var left: Branch? = null
    var right: Branch? = null

    fun update(timePassed: Double) {
        age += timePassed * 300
        if(dad != null) {
            if (thickness + 1 < dad!!.thickness) {
                if (carbon > 1) carbon -= timePassed
                thickness += carbon/age
            } else {
                dad!!.carbon += timePassed
            }
        } else {
            if (carbon > 0) carbon -= timePassed
            thickness += carbon/age
        }
        if (left == null && right == null) length += (carbon * .005)/thickness
        if (right != null) {
            right!!.update(timePassed)
        }
        if (left != null) {
            left!!.update(timePassed)
        }
        if(thickness > 23 || length > 150) {
            growBranches()
        }
        thickness = min(thickness, 50.0)
    }
    fun getOffsetOne(): Offset {
        return origin
    }
    fun getOffsetTwo(): Offset {
        val radians = rotation * PI/180
        val x = origin.x + (length * cos(radians))
        val y = origin.y + (length * sin(radians))
        return Offset(x.toFloat(), y.toFloat())
    }
    fun getThickness(): Float {
        return thickness.toFloat()
    }
    private fun growBranches() {
        val random = Random.nextInt(0,100)
        if (random > 98) {
            if(left == null) {
                left = Branch(getOffsetTwo(),rotation - Random.nextInt(5,45), this)
                sendItDown()
            }

        }
        if (random < 2) {
            if (right == null) {
                right = Branch(getOffsetTwo(), rotation + Random.nextInt(5,45), this)
                sendItDown()
            }
        }
    }
    private fun sendItDown() {
        if(left != null) {
            if (thickness < left!!.thickness * 2) thickness += 1
            else if (thickness < left!!.thickness) thickness = left!!.thickness
        } else if (right != null) {
            if (thickness < right!!.thickness * 2) thickness += 1
            else if (thickness < right!!.thickness) thickness = right!!.thickness
        }
        dad?.sendItDown()
    }

}