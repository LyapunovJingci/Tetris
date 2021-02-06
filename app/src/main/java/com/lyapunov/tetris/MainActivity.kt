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
import com.lyapunov.tetris.constants.BlockColorTheme

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
        private var lastClickDown: Long = 0
        private var lines: TextView? = null
        private var levels: TextView? = null
        private var scores: TextView? = null
        private var alertBuilder: AlertDialog.Builder? = null
        private lateinit var binding: ActivityMainBinding
        private var themeName: String = "ESPRESSO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("eee", "Create")
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setPaint()
        binding.rotateButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRotate > 200) {
                Game.getGame().rotateBlock()
            }
            lastClickRotate = System.currentTimeMillis()
        }

        binding.leftButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickLeft > 200) {
                Game.getGame().moveBlockLeft()
            }
            lastClickLeft = System.currentTimeMillis()
        }

        binding.rightButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickRight > 200) {
                Game.getGame().moveBlockRight()
            }
            lastClickRight = System.currentTimeMillis()
        }

        binding.downButton.setOnClickListener {
            if (System.currentTimeMillis() - lastClickDown > 200) {
                Game.getGame().moveBlockDown()
            }
                lastClickDown = System.currentTimeMillis()
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
        alertBuilder = AlertDialog.Builder(this)
        alertBuilder!!.setTitle("Game End")
        alertBuilder?.apply {
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
        alertBuilder!!.create()

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
        canvas.drawRGB(BlockColorTheme.getTheme(themeName)[7][1], BlockColorTheme.getTheme(themeName)[7][2], BlockColorTheme.getTheme(themeName)[7][3])
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
        canvas.drawRGB(BlockColorTheme.getTheme(themeName)[7][1], BlockColorTheme.getTheme(themeName)[7][2], BlockColorTheme.getTheme(themeName)[7][3])
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

    private fun setPaint() {
        // Initialize colors of each block
        val paintI: Paint = Paint()
        paintI.setARGB(BlockColorTheme.getTheme(themeName)[0][0], BlockColorTheme.getTheme(themeName)[0][1], BlockColorTheme.getTheme(themeName)[0][2], BlockColorTheme.getTheme(themeName)[0][3])
        val paintJ: Paint = Paint()
        paintJ.setARGB(BlockColorTheme.getTheme(themeName)[1][0], BlockColorTheme.getTheme(themeName)[1][1], BlockColorTheme.getTheme(themeName)[1][2], BlockColorTheme.getTheme(themeName)[1][3])
        val paintL: Paint = Paint()
        paintL.setARGB(BlockColorTheme.getTheme(themeName)[2][0], BlockColorTheme.getTheme(themeName)[2][1], BlockColorTheme.getTheme(themeName)[2][2], BlockColorTheme.getTheme(themeName)[2][3])
        val paintO: Paint = Paint()
        paintO.setARGB(BlockColorTheme.getTheme(themeName)[3][0], BlockColorTheme.getTheme(themeName)[3][1], BlockColorTheme.getTheme(themeName)[3][2], BlockColorTheme.getTheme(themeName)[3][3])
        val paintS: Paint = Paint()
        paintS.setARGB(BlockColorTheme.getTheme(themeName)[4][0], BlockColorTheme.getTheme(themeName)[4][1], BlockColorTheme.getTheme(themeName)[4][2], BlockColorTheme.getTheme(themeName)[4][3])
        val paintT: Paint = Paint()
        paintT.setARGB(BlockColorTheme.getTheme(themeName)[5][0], BlockColorTheme.getTheme(themeName)[5][1], BlockColorTheme.getTheme(themeName)[5][2], BlockColorTheme.getTheme(themeName)[5][3])
        val paintZ: Paint = Paint()
        paintZ.setARGB(BlockColorTheme.getTheme(themeName)[6][0], BlockColorTheme.getTheme(themeName)[6][1], BlockColorTheme.getTheme(themeName)[6][2], BlockColorTheme.getTheme(themeName)[6][3])
        paintArray = arrayOf(paintI, paintJ, paintL, paintO, paintS, paintT, paintZ)
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
        runOnUiThread { alertBuilder?.show() }
    }

    override fun gameRestart() {
        runOnUiThread {
            lines?.text = "0"
            scores?.text = "0"
            levels?.text = "1"
        }
    }

}


