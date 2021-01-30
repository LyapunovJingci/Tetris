package com.lyapunov.tetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val newGameButton = findViewById<Button>(R.id.NewGameButton)
        val settingButton = findViewById<Button>(R.id.settingButton)
        val highscoreButton = findViewById<Button>(R.id.HighScoreButton)

        newGameButton.setOnClickListener {
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
        }

        settingButton.setOnClickListener {
            val intent = Intent(this@StartActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        highscoreButton.setOnClickListener {
            val intent = Intent(this@StartActivity, HighscoreActivity::class.java)
            startActivity(intent)
        }


    }
}