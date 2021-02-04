package com.lyapunov.tetris

import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ImageButton
import com.lyapunov.tetris.databinding.ActivityMainBinding
import com.lyapunov.tetris.game.Board
import com.lyapunov.tetris.game.BoardObserver
import com.lyapunov.tetris.game.Game

class MainActivity : AppCompatActivity(), BoardObserver {
        private var surfaceHolder: SurfaceHolder? = null
        private var nextSurfaceHolder: SurfaceHolder? = null
        private var paintArray: Array<Paint>? = null
        private var canvasHeight: Float = 0F
        private var canvasWidth: Float = 0F
        private var lineWidth: Float = 0F
        private var blockWidth: Float = 0F
        private var nextCanvasHeight: Float = 0F
        private var nextCanvasWidth: Float = 0F

        private var lastClickLeft: Long = 0
        private var lastClickRight: Long = 0
        private var lastClickRotate: Long = 0
        private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

        binding.rotateButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRotate > 300) {
                game.rotateBlock()
            }
            lastClickRotate = System.currentTimeMillis()
        }

        binding.leftButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickLeft > 300) {
                game.moveBlockLeft()
            }
            lastClickLeft = System.currentTimeMillis()
        }

        binding.rightButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRight > 300) {
                game.moveBlockRight()
            }
            lastClickRight = System.currentTimeMillis()
        }

        surfaceHolder = binding.board.holder
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

        nextSurfaceHolder = binding.nextBoard.holder
        nextSurfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val canvas = nextSurfaceHolder!!.lockCanvas()

                //Initialize canvas measure
                nextCanvasHeight = canvas.height.toFloat()
                nextCanvasWidth = canvas.width.toFloat()
                drawInitialNextBoard(canvas)
                nextSurfaceHolder!!.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
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
     * Draw initial background and lines for the board showing next block
     */
    private fun drawInitialNextBoard(canvas: Canvas) {
        canvas.drawRGB(245,248,251)
        val paint: Paint = Paint()
        paint.setARGB(255,255,255,255)
        for (i in 0..4) {
            canvas.drawRect(i * (blockWidth + lineWidth), 0F, i * (blockWidth + lineWidth) + lineWidth, nextCanvasHeight, paint)
        }
        for (i in 0..4) {
            canvas.drawRect(0F, i * (blockWidth + lineWidth), nextCanvasWidth, i * (blockWidth + lineWidth) + lineWidth, paint)
        }
    }

    /**
     * Draw current status of a canvas for the board showing next block
     */
    private fun drawInstantNextBoard(canvas: Canvas, nextMatrix: Array<IntArray>, paintArray: Array<Paint>) {
        for (i in 0..3) {
            for (j in 0.. 3) {
                if (nextMatrix[i][j] == 0) {
                    continue
                }
                var left = j * blockWidth + (j + 1) * lineWidth
                var top = i * blockWidth + (i + 1) * lineWidth
                var right = left + blockWidth
                var bottom = top + blockWidth
                canvas.drawRect(left, top, right, bottom, paintArray[nextMatrix[i][j] - 1])
            }
        }
    }

    /**
     * Get board update
     */
    override fun update() {
        val matrix: Array<IntArray> = Board.getBoard().boardMatrix
        val canvas = surfaceHolder!!.lockCanvas()
        drawInitialBoard(canvas)
        paintArray?.let { drawInstantBoard(canvas, matrix, it) }
        surfaceHolder!!.unlockCanvasAndPost(canvas)
    }

    /**
     * Get update, when a new block is generated, its next block shall change
     */
    override fun generateNew(shapeNum: Array<IntArray>) {
        val canvas = nextSurfaceHolder!!.lockCanvas()
        drawInitialNextBoard(canvas)
        paintArray?.let { drawInstantNextBoard(canvas, shapeNum, it) }
        nextSurfaceHolder!!.unlockCanvasAndPost(canvas)
    }


}


