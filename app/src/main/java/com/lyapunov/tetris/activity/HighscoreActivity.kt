package com.lyapunov.tetris.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lyapunov.tetris.R
import com.lyapunov.tetris.database.AppDatabase
import com.lyapunov.tetris.database.Score
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class HighscoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_highscore)

        val recyclerView: RecyclerView = findViewById(R.id.highscore_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        lifecycleScope.launch {
            val dao = AppDatabase(application).scoreDao()
            val scoreList  = dao.getTopTen()
            recyclerView.adapter = HighScoreRecyclerAdapter(scoreList)
        }
        lifecycleScope.launch {
            val viewKonfetti = findViewById<KonfettiView>(R.id.viewKonfetti)
            viewKonfetti.build()
                .addColors(Color.rgb(255,78,80), Color.rgb(252,145,58), Color.rgb(249,214,46))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square)
                .addSizes(Size(12))
                .setPosition(-50f, 2000 + 50f, -50f, -50f)
                .streamFor(200, 3000L)
        }

    }
}