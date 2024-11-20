package com.example.tree

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture

class MyGameOLD : ApplicationListener {

    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture

    override fun create() {
        // Set up your resources
        batch = SpriteBatch()        // For drawing sprites
        img = Texture("myimage.png") // Load a texture or any resources
    }

    override fun render() {
        // This method is called every frame
        // Clear the screen to prepare for the next frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Start drawing
        batch.begin()
        batch.draw(img, 0f, 0f)  // Draw your image at position (0, 0)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        // Called when the window is resized
        // You can adjust the viewport here if necessary
    }

    override fun pause() {
        // Called when the app is paused
    }

    override fun resume() {
        // Called when the app is resumed
    }

    override fun dispose() {
        // Clean up resources when done
        batch.dispose()
        img.dispose()
    }
}
