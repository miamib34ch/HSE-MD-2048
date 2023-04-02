package org.hse.hse_2048.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.toBitmap
import org.hse.hse_2048.R

class GameField(context: Context, private val gameActivity: GameActivity) : View(context) {
    
    private lateinit var cellDrawables: HashMap<Int, Drawable?>

    private val gameRows: Int = gameActivity.gameRows
    private val charPaint: Paint = Paint()

    private var is2048reached = false
    private var fieldInitialized = false

    var grid: Grid = Grid(gameRows)

    init {
        charPaint.textAlign = Paint.Align.CENTER
        charPaint.isAntiAlias = true
        charPaint.typeface = Typeface.DEFAULT_BOLD
    }

    private val fieldBackground: Drawable? = getDrawable(context, R.drawable.field_background)

    override fun onDraw(canvas: Canvas?) {
        if (!fieldInitialized) {
            background = fieldBackground
        }
        drawCells(canvas)
    }

    fun makeMove(direction: Direction): Boolean {
        val isAnythingMoved = grid.makeMove(direction)

        is2048reached = grid.checkFor2048Tile()
        if (is2048reached)
            endGame(true)

        if (isAnythingMoved)
            generateNeededCountCells()

        this.invalidate()

        val isGameOver = !grid.canMakeMove()

        if (isGameOver)
            endGame(false)
        return true
    }

    fun startGame() {
        cellDrawables = initializeCells()

        val gameFieldHasCells = grid.checkForAnyTile()
        if (!gameFieldHasCells)
            generateNeededCountCells()

        this.invalidate()
    }

    private fun endGame(isPlayerWon: Boolean) {
        gameActivity.endGame(isPlayerWon)
    }

    private fun generateNeededCountCells() {
        val cellsCountToGenerate: Int = (1..gameRows / 2).random()

        for (i in 0 until cellsCountToGenerate) {
            grid.generateNewCell()
        }
    }

    private fun drawCells(canvas: Canvas?) {
        var cellSize = canvas?.width?.div(gameRows) ?: return
        cellSize -= cellSize / 10

        var x: Float = (cellSize / 12.5).toFloat()
        var y: Float = (cellSize / 12.5).toFloat()

        for (i in 0 until gameRows) {
            for (j in 0 until gameRows) {

                val cellNumber = grid.getCellNumber(i, j)
                val cellBitmap = getBitmapForNumber(cellNumber, cellSize)
                if (cellBitmap != null) {
                    canvas.drawBitmap(cellBitmap, x, y, null)
                }
                y += cellSize + (cellSize / 10).toFloat()
            }
            y = (cellSize / 10).toFloat()
            x += cellSize + (cellSize / 10).toFloat()
        }
    }

    private fun getBitmapForNumber(cellNumber: Int, cellSize: Int): Bitmap? {
        var number = cellNumber
        if (cellNumber > 4096)
            number = 4096

        val cell = cellDrawables.get(key = cellNumber)
        val cellBitmap = cell?.toBitmap(cellSize, cellSize)

        if (number == 0)
            return cellBitmap

        val canvas = cellBitmap?.let { Canvas(it) }
        canvas?.let { drawCellText(it, number, cellSize) }

        return cellBitmap
    }

    private fun drawCellText(canvas: Canvas, number: Int, cellSize: Int) {
        val textShiftY: Int = ((charPaint.descent() + charPaint.ascent()) / 2).toInt()
        if (number >= 8)
            charPaint.color = getColor(context, R.color.white)
        else
            charPaint.color = getColor(context, R.color.black)

        charPaint.textSize = cellSize.toFloat()
        charPaint.textSize = cellSize * cellSize / cellSize.toFloat()
            .coerceAtLeast(charPaint.measureText("0000"))

        val charTextSize: Float =
            charPaint.textSize * cellSize * 0.9f / (cellSize * 0.9f).coerceAtLeast(
                charPaint.measureText(number.toString()))
        charPaint.textSize = charTextSize

        canvas.drawText("" + number,
            (cellSize / 2).toFloat(),
            (cellSize / 2 - textShiftY).toFloat(),
            charPaint)
    }

    private fun initializeCells(): HashMap<Int, Drawable?> {
        val cellDrawables = HashMap<Int, Drawable?>()
        cellDrawables.set(key = 0, value = getDrawable(context, R.drawable.cell_rectangle))
        cellDrawables.set(key = 2, value = getDrawable(context, R.drawable.cell_rectangle_2))
        cellDrawables.set(key = 4, value = getDrawable(context, R.drawable.cell_rectangle_4))
        cellDrawables.set(key = 8, value = getDrawable(context, R.drawable.cell_rectangle_8))
        cellDrawables.set(key = 16, value = getDrawable(context, R.drawable.cell_rectangle_16))
        cellDrawables.set(key = 32, value = getDrawable(context, R.drawable.cell_rectangle_32))
        cellDrawables.set(key = 64, value = getDrawable(context, R.drawable.cell_rectangle_64))
        cellDrawables.set(key = 128, value = getDrawable(context, R.drawable.cell_rectangle_128))
        cellDrawables.set(key = 256, value = getDrawable(context, R.drawable.cell_rectangle_256))
        cellDrawables.set(key = 512, value = getDrawable(context, R.drawable.cell_rectangle_512))
        cellDrawables.set(key = 1024, value = getDrawable(context, R.drawable.cell_rectangle_1024))
        cellDrawables.set(key = 2048, value = getDrawable(context, R.drawable.cell_rectangle_2048))
        cellDrawables.set(key = 4096, value = getDrawable(context, R.drawable.cell_rectangle_4096))
        return cellDrawables
    }
}