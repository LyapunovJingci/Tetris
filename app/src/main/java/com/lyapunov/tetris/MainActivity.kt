package com.lyapunov.tetris

import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val surfaceView: SurfaceView = findViewById(R.id.board)
        val surfaceHolder: SurfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(object: SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val canvas = surfaceHolder.lockCanvas()
                drawInitialBoard(canvas)
                surfaceHolder.unlockCanvasAndPost(canvas)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                Log.d("ddd", "changed")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                Log.d("ddd", "destroyed")
            }

        })



    }

    private fun drawInitialBoard(canvas: Canvas) {
        val paint: Paint = Paint()
        paint.setARGB(255,255,255,255)
        canvas.drawRGB(245, 248, 251)
        val height:Float = canvas.height.toFloat()
        val width:Float = canvas.width.toFloat()
        val lineWidth = width / 61
        val blockWidth = lineWidth * 5
        for (i in 0..10) {
            canvas.drawRect(i * (blockWidth + lineWidth), 0F, i * (blockWidth + lineWidth) + lineWidth, height, paint)
        }
        for (i in 0..20) {
            canvas.drawRect(0F, i * (blockWidth + lineWidth), width, i * (blockWidth + lineWidth) + lineWidth, paint)
        }
        val painttmp: Paint = Paint()
        painttmp.setARGB(255, 4, 125, 104)
        canvas.drawRect(lineWidth, lineWidth, blockWidth + lineWidth, blockWidth + lineWidth, painttmp)
        canvas.drawRect(2* lineWidth + blockWidth , lineWidth, 2 * blockWidth + 2 * lineWidth, blockWidth + lineWidth, painttmp)
        canvas.drawRect(3* lineWidth + 2 * blockWidth, lineWidth, 3 * blockWidth + 3* lineWidth, blockWidth + lineWidth, painttmp)


    }






}


