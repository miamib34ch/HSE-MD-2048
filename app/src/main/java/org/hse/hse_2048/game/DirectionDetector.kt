package org.hse.hse_2048.game

import android.view.MotionEvent
import android.view.View
import org.hse.hse_2048.game.Direction
import kotlin.math.atan2

open class DirectionDetector : View.OnTouchListener {

    private var x: Double = 0.0
    private var y: Double = 0.0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> onActionDown(event)
            MotionEvent.ACTION_UP -> onActionUp(event)
            else -> {}
        }
        return true
    }

    open fun onDirectionDetected(direction: Direction) {}

    private fun onActionUp(event: MotionEvent?) {
        val currentX = if (event?.x == null) 0.0
        else event.x.toDouble()

        val currentY = if (event?.y == null) 0.0
        else event.y.toDouble()

        val direction = Direction.get(getAngle(x, y, currentX, currentY))
        onDirectionDetected(direction)
    }

    private fun onActionDown(event: MotionEvent?) {
        x = if (event?.x == null) 0.0
        else event.x.toDouble()

        y = if (event?.y == null) 0.0
        else event.y.toDouble()
    }

    private fun getAngle(x: Double, y: Double, currentX: Double, currentY: Double): Double {
        var angle = Math.toDegrees(atan2(currentY - y, currentX - x))

        if (angle < 0)
            angle += 360

        return angle
    }
}