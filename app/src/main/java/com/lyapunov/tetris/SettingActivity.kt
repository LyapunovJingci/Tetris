package com.lyapunov.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.lyapunov.tetris.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.levelInstant.text = (progress + 1).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        binding.radioTheme1.setOnClickListener { Log.d("checked", "theme1") }
        binding.radioTheme2.setOnClickListener { Log.d("checked", "theme2") }
        binding.radioTheme3.setOnClickListener { Log.d("checked", "theme3") }
        binding.radioTheme4.setOnClickListener { Log.d("checked", "theme4") }
        binding.radioTheme5.setOnClickListener { Log.d("checked", "theme5") }
    }
}

