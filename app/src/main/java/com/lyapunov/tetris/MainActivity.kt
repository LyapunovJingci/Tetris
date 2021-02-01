package com.lyapunov.tetris

import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.lyapunov.tetris.components.Board
import com.lyapunov.tetris.components.BoardObserver
import com.lyapunov.tetris.game.Game

class MainActivity : AppCompatActivity(), BoardObserver{
        private var surfaceHolder: SurfaceHolder? = null
        private var paintArray: Array<Paint>? = null
        private var canvasHeight: Float = 0F
        private var canvasWidth: Float = 0F
        private var lineWidth: Float = 0F
        private var blockWidth: Float = 0F
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize colors of each block
        val paintI: Paint = Paint()
        paintI.setARGB(255, 4, 125, 104)
        val paintJ: Paint = Paint()
        paintJ.setARGB(255, 193, 215, 48)
        val paintL: Paint = Paint()
        paintL.setARGB(255, 1, 43, 92)
        val paintO: Paint = Paint()
        paintO.setARGB(253, 0, 181, 204)
        val paintS: Paint = Paint()
        paintS.setARGB(255, 253, 185, 19)
        val paintT: Paint = Paint()
        paintT.setARGB(255, 244, 121, 31)
        val paintZ: Paint = Paint()
        paintZ.setARGB(255, 113, 112, 115)
        paintArray = arrayOf(paintI, paintJ, paintL, paintO, paintS, paintT, paintZ)

        //Attach listener
        Board.getBoard().attach(this)

        //Initialize new game, need refactor later
        val game: Game = Game(1)
        game.start()

        //Bind SurfaceView
        val surfaceView: SurfaceView = findViewById(R.id.board)
        surfaceHolder = surfaceView.holder
        surfaceHolder?.addCallback(object: SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val canvas = surfaceHolder!!.lockCanvas()

                //Initialize canvas measure
                canvasHeight = canvas.height.toFloat()
                canvasWidth = canvas.width.toFloat()
                lineWidth = canvasWidth / 61
                blockWidth = lineWidth * 5
                Log.e("data", canvasHeight.toString())

                //Draw background and lines
                drawInitialBoard(canvas)
                surfaceHolder!!.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
            ) {
                    Log.d("dddffff", "changed")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.d("dddfff", "destroyed")
            }

        })




    }

    /**
     * Draw initial background and lines
     */
    private fun drawInitialBoard(canvas: Canvas) {
        canvas.drawRGB(245,248,251)
        val paint: Paint = Paint()
        paint.setARGB(255,255,255,255)
        for (i in 0..10) {
            canvas.drawRect(i * (blockWidth + lineWidth), 0F, i * (blockWidth + lineWidth) + lineWidth, canvasHeight, paint)
        }
        for (i in 0..20) {
            canvas.drawRect(0F, i * (blockWidth + lineWidth), canvasWidth, i * (blockWidth + lineWidth) + lineWidth, paint)
        }
    }

    /**
     * Draw current status of a canvas
     */
    private fun drawInstantBoard(canvas: Canvas, boardMatrix: Array<IntArray>, paintArray: Array<Paint>) {
        for (i in 0..19) {
            for (j in 0..9) {
                if (boardMatrix[i][j] == 0) {
                    continue
                }
                var left = j * blockWidth + (j + 1) * lineWidth
                var top = i * blockWidth + (i + 1) * lineWidth
                var right = left + blockWidth
                var bottom = top + blockWidth
                canvas.drawRect(left, top, right, bottom, paintArray[boardMatrix[i][j] - 1])
            }
        }
    }

    /**
     * Get board update
     */
    override fun update() {
        Log.e("sksskksks", "Changed")
        val matrix: Array<IntArray> = Board.getBoard().boardMatrix
        val canvas = surfaceHolder!!.lockCanvas()
        drawInitialBoard(canvas)
        paintArray?.let { drawInstantBoard(canvas, matrix, it) }
        surfaceHolder!!.unlockCanvasAndPost(canvas)
    }


}


