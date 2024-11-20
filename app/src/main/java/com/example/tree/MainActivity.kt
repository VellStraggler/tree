package com.example.tree

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tree.ui.theme.TreeTheme
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import android.graphics.BitmapFactory
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.MultiParagraph
import androidx.compose.ui.text.MultiParagraphIntrinsics
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import android.util.Log
import androidx.compose.ui.platform.LocalDensity

var bounds = Size(1080F, 1980F)
var tree = Branch(Offset(bounds.width/2, bounds.height/1.7f), -90.0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpriteCanvasWithTapDetection()
        }

    }
}

@Composable
fun SpriteCanvasWithTapDetection() {
    var tappedSprite by remember { mutableStateOf<String?>(null) }
    var time by remember { mutableIntStateOf(0) }

    val sunImage = ImageBitmap.imageResource(id = R.drawable.sun)
    val sunOffset = Offset(50f, 50f)
    val cloudImage = ImageBitmap.imageResource(id = R.drawable.cloud)
    val cloudOffset = Offset(50f, 200f)

    val sun = (SkyObject("sun", sunImage, sunOffset, 400))

    val cloud = (SkyObject("cloud", cloudImage, cloudOffset, 400))



    val landOffset = Offset(20f,1000f)
    val islandBackground = PlainSkyObject("back", ImageBitmap.imageResource(id = R.drawable.island_back), landOffset)
    val islandForeground = PlainSkyObject("fore", ImageBitmap.imageResource(id = R.drawable.island_fore),landOffset)
    val basicSprites = mutableListOf(islandBackground,islandForeground)
    val movingSprites = mutableListOf(sun,cloud)
    val allSprites = basicSprites + movingSprites

    val leaf = PlainSkyObject("leaf", ImageBitmap.imageResource(id = R.drawable.leaf),Offset(0f,0f))

    LaunchedEffect(Unit) {
        while (true) {
            // Update sprite positions here
            // Optional: Update other state like tappedSprite or any interaction flag
            // tappedSprite = "some new value" (if necessary for other UI changes)
            time += 1
            tree.update(1.0)
            if(time%60 == 0) {
                Log.i("trunk thickness", tree.getThickness().toString())
            }
            if(sun.offset.x == cloud.offset.x) delay(33) // Update at ~30 FPS (33ms per frame)
            else delay(13)
        }
    }
    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { tapOffset ->
//                Log.i("tap",tapOffset.toString())
                tappedSprite = ""
                movingSprites.forEach { sprite ->
                    val inX = sprite.offset.x<= tapOffset.x &&
                            tapOffset.x <= sprite.offset.x + sprite.diam
                    val inY = sprite.offset.y <= tapOffset.y &&
                            tapOffset.y <= sprite.offset.y + sprite.diam
                    if(inX && inY) tappedSprite = sprite.name
                }
                movingSprites.forEach { sprite ->
                    if(tappedSprite.equals(sprite.name)) {
                        sprite.move()
//                        Log.i("moving", sprite.name)
                    }
                }
            }
        }) {
        drawRect(Color(0xFF87CEEB),Offset.Zero,bounds) // the SKY
        allSprites.forEach { sprite ->
            drawImage(sprite.image, sprite.offset)
        }
        movingSprites.forEachIndexed { index, sprite ->
            if(tappedSprite.equals(sprite.name)) { // Screen redraws only because we check this
//                Log.i("hit", index.toString() + " " + basicSprites[index].offset.toString())
            } else {
//                Log.i("miss",tappedSprite + " does not equal " + sprite.name)
            }
        }
        if(time > -1) {
//            Log.i("test",time.toString())
        }
        val treeParts = collectBranches(tree)
        treeParts.forEach { branch ->

            val mult = .3f
            val rootOffsetOne = Offset(
                tree.getOffsetOne().x - (tree.getOffsetOne().x - branch.getOffsetOne().x) * mult,
                tree.getOffsetOne().y + (tree.getOffsetOne().y - branch.getOffsetOne().y) * mult
            )
            val rootOffsetTwo = Offset(
                tree.getOffsetOne().x - (tree.getOffsetOne().x - branch.getOffsetTwo().x) * mult,
                tree.getOffsetOne().y + (tree.getOffsetOne().y - branch.getOffsetTwo().y) * mult
            )
            drawLine( // ROOT
                color = Color.Black,
                rootOffsetOne,
                rootOffsetTwo,
                branch.getThickness() / 2
            )
            drawLine( // BRANCH
                color = Color(0xFF483020),
                branch.getOffsetOne(),
                branch.getOffsetTwo(),
                branch.getThickness()
            )
            drawLine( // BRANCH
                color = Color(0xFF654321),
                branch.getOffsetOne(),
                branch.getOffsetTwo(),
                branch.getThickness() / 2
            )
        }
        treeParts.forEach() { branch ->
            if(branch.left == null || branch.right == null) {
                drawImage(
                    leaf.image, Offset(
                        leaf.offset.x + branch.getOffsetTwo().x,
                        leaf.offset.y + branch.getOffsetTwo().y
                    )
                )
            }

        }
        drawImage(islandForeground.image,islandForeground.offset)
    }
}
fun collectBranches(branch: Branch?): List<Branch> {
    if (branch == null) {
        return emptyList()
    }

    val branches = mutableListOf(branch)
    if (branch.left != null) {
        branches.addAll(collectBranches(branch.left))
    }
    if (branch.right != null) {
        branches.addAll(collectBranches(branch.right))
    }
    return branches
}