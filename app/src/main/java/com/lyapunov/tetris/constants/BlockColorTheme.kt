package com.lyapunov.tetris.constants

object BlockColorTheme {
    private val MODERN: Array<IntArray> = arrayOf(
            intArrayOf(255, 4, 125, 104),
            intArrayOf(255, 193, 215, 48),
            intArrayOf(255, 1, 43, 92),
            intArrayOf(255, 0, 181, 204),
            intArrayOf(255, 253, 185, 19),
            intArrayOf(255, 244, 121, 31),
            intArrayOf(255, 113, 112, 155),
            intArrayOf(255, 245, 248, 251))
    private val MONALISA: Array<IntArray> = arrayOf (
            intArrayOf(255, 52, 28, 15),
            intArrayOf(255, 153, 42, 33),
            intArrayOf(255, 100, 97, 54),
            intArrayOf(255, 193, 110, 48),
            intArrayOf(255, 82, 92, 93),
            intArrayOf(255, 52, 28, 15),
            intArrayOf(255, 153, 32, 45),
            intArrayOf(255, 255, 251, 230))
    private val BELAFONTE: Array<IntArray> = arrayOf (
            intArrayOf(255, 188, 19, 26),
            intArrayOf(255, 133, 129, 100),
            intArrayOf(255, 232, 164, 82),
            intArrayOf(255, 67, 106, 120),
            intArrayOf(255, 150, 82, 48),
            intArrayOf(255, 153, 154, 156),
            intArrayOf(255, 151, 140, 132),
            intArrayOf(255, 235, 230, 221))
    private val ESPRESSO: Array<IntArray> = arrayOf (
            intArrayOf(255, 54, 54, 54),
            intArrayOf(255, 208, 83, 85),
            intArrayOf(255, 166, 193, 102),
            intArrayOf(255, 254, 197, 116),
            intArrayOf(255, 109, 154, 186),
            intArrayOf(255, 208, 153, 215),
            intArrayOf(255, 191, 215, 253),
            intArrayOf(255, 238, 238, 236))
    private val SPECTRUM: Array<IntArray> = arrayOf (
            intArrayOf(255, 251, 99, 142),
            intArrayOf(255, 127, 215, 146),
            intArrayOf(255, 251, 228, 111),
            intArrayOf(255, 251, 147, 90),
            intArrayOf(255, 149, 140, 124),
            intArrayOf(255, 96, 212, 229),
            intArrayOf(255, 51, 51, 52),
            intArrayOf(255, 247, 241, 255))
    fun getTheme(themeName: String): Array<IntArray> {
        return when (themeName){
            "MONALISA" -> MONALISA
            "BELAFONTE" -> BELAFONTE
            "ESPRESSO" -> ESPRESSO
            "SPECTRUM" -> SPECTRUM
            else -> MODERN
        }
    }
}