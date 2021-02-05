package com.lyapunov.tetris

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.lyapunov.tetris.databinding.ActivityMainBinding
import com.lyapunov.tetris.game.GameObserver
import com.lyapunov.tetris.game.Game

class MainActivity : AppCompatActivity(), GameObserver {
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
        private var lines: TextView? = null
        private var levels: TextView? = null
        private var scores: TextView? = null
        private var builder: AlertDialog.Builder? = null
        private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("eee", "Create")
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

        binding.rotateButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRotate > 100) {
                Game.getGame().rotateBlock()
            }
            lastClickRotate = System.currentTimeMillis()
        }

        binding.leftButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickLeft > 100) {
                Game.getGame().moveBlockLeft()
            }
            lastClickLeft = System.currentTimeMillis()
        }

        binding.rightButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRight > 100) {
                Game.getGame().moveBlockRight()
            }
            lastClickRight = System.currentTimeMillis()
        }
        lines = binding.RealTimeLines
        levels = binding.LevelRealTime
        scores = binding.ScoreRealTime
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
        builder = AlertDialog.Builder(this)
        builder!!.setTitle("Game End")
        builder?.apply {
            setPositiveButton("Restart"
            ) { dialog, id ->
                Game.getGame().end()
                Game.getGame().start()
            }
            setNegativeButton("Back"
            ) { dialog, id ->
                Game.getGame().end()
                val intent = Intent(this@MainActivity, StartActivity::class.java)
                startActivity(intent)
            }
        }

        // Create the AlertDialog
        builder!!.create()

        //Attach listener
        Game.getGame().attach(this)
        Game.getGame().start()

    }

    override fun onStop() {
        Game.getGame().detach(this)
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        Game.getGame().detach(this)
        Game.getGame().end()
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
    override fun updateCanvas() {
        val matrix: Array<IntArray> = Game.getGame().currentBoardMatrix
        val canvas = surfaceHolder!!.lockCanvas()
        drawInitialBoard(canvas)
        paintArray?.let { drawInstantBoard(canvas, matrix, it) }
        surfaceHolder!!.unlockCanvasAndPost(canvas)
    }

    /**
     * Get update, when a new block is generated, its next block shall change
     */
    override fun generateNewBlock(shapeNum: Array<IntArray>) {
        val canvas = nextSurfaceHolder!!.lockCanvas()
        drawInitialNextBoard(canvas)
        paintArray?.let { drawInstantNextBoard(canvas, shapeNum, it) }
        nextSurfaceHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun updateGameInfo(totalClearedLines: Int, score: Int, level: Int) {
        runOnUiThread{
            lines?.text = totalClearedLines.toString()
            scores?.text = score.toString()
            levels?.text = level.toString()
        }
    }

    override fun gameEnd() {
        Log.e("Game end", "Game End")
        runOnUiThread { builder?.show() }
    }

    override fun gameRestart() {
        runOnUiThread {
            lines?.text = "0"
            scores?.text = "0"
            levels?.text = "1"
        }
    }

}


