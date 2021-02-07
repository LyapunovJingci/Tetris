package com.lyapunov.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.lyapunov.tetris.constants.BlockColorTheme
import com.lyapunov.tetris.database.BlockThemeManager
import com.lyapunov.tetris.databinding.ActivitySettingBinding
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val blockThemeManager = BlockThemeManager(applicationContext)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.levelInstant.text = (progress + 1).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        lifecycleScope.launch {
            when (blockThemeManager.getTheme()) {
                BlockColorTheme.THEME_MONALISA -> binding.radioTheme2.isChecked = true
                BlockColorTheme.THEME_BELAFONTE -> binding.radioTheme3.isChecked = true
                BlockColorTheme.THEME_ESPRESSO -> binding.radioTheme4.isChecked = true
                BlockColorTheme.THEME_SPECTRUM -> binding.radioTheme5.isChecked = true
                BlockColorTheme.THEME_RAINBOW -> binding.radioTheme6.isChecked = true
                else -> binding.radioTheme1.isChecked = true
            }
        }
        binding.radioTheme1.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_MODERN)
            }
            binding.radioTheme1.isChecked = true
        }
        binding.radioTheme2.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_MONALISA)
            }
            binding.radioTheme2.isChecked = true
        }
        binding.radioTheme3.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_BELAFONTE)
            }
            binding.radioTheme3.isChecked = true
        }
        binding.radioTheme4.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_ESPRESSO)
            }
            binding.radioTheme4.isChecked = true
        }
        binding.radioTheme5.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_SPECTRUM)
            }
            binding.radioTheme5.isChecked = true
        }
        binding.radioTheme6.setOnClickListener {
            lifecycleScope.launch {
                blockThemeManager.setTheme(BlockColorTheme.THEME_RAINBOW)
            }
            binding.radioTheme6.isChecked = true
        }
    }
}

