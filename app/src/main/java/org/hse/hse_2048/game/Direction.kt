package org.hse.hse_2048.game

enum class Direction {
    NOWHERE,
    UP,
    DOWN,
    LEFT,
    RIGHT;

    companion object {
        fun get(angle: Double): Direction {
            return when (angle) {
                in 60.0..120.0 -> DOWN
                in 150.0..210.0 -> LEFT
                in 240.0..300.0 -> UP
                in 330.0..360.0 -> RIGHT
                in 0.0..30.0 -> RIGHT
                else -> NOWHERE
            }
        }
    }
}