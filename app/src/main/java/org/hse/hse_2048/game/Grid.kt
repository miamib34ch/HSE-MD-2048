package org.hse.hse_2048.game

class Grid(private var fieldSize: Int) {

    private var field: Array<Array<Tile>> = Array(fieldSize) { Array(fieldSize) { Tile(0, 0, 0) } }

    init {
        for (i in 0 until fieldSize)
            for (j in 0 until fieldSize)
                field[i][j] = Tile(i, j, 0)
    }

    fun getCellNumber(x: Int, y: Int): Int {
        return field[x][y].number
    }

    fun getSum(): Int {
        return field.sumOf { row -> row.sumOf { it.number } }
    }

    fun generateNewCell(): Cell? {
        var cell = Cell(0, 0)
        val randomX: Int = (0 until fieldSize).random()
        var x = randomX
        val randomY: Int = (0 until fieldSize).random()
        var y = randomY

        val number: Int = if ((0..100).random() == CHANCE_OF_LUCKY_SPAWN)
            LUCKY_INITIAL_CELL_STATE
        else
            INITIAL_CELL_STATE

        var isPlaced = false

        while (!isPlaced) {
            val canPlaceCell = field[x][y].number == 0

            if (canPlaceCell) {
                field[x][y].number = number
                isPlaced = true
                cell = Cell(x, y)
            } else {
                if (x + 1 < fieldSize) x++
                else {
                    x = 0
                    if (y + 1 < fieldSize) {
                        y++
                    } else {
                        y = 0
                    }
                }

                if (x == randomX && y == randomY)
                    return null
            }
        }

        return cell
    }

    fun makeMove(direction: Direction): Boolean {
        return when (direction) {
            Direction.UP, Direction.DOWN -> shiftByColumns(direction)
            Direction.RIGHT, Direction.LEFT -> shiftByRows(direction)
            else -> false
        }
    }

    fun checkFor2048Tile(): Boolean {
        return field.any { tiles -> tiles.any { tile -> tile.number == 2048 } }
    }

    fun checkForAnyTile(): Boolean {
        return field.any { tiles -> tiles.any { tile -> tile.number != 0 } }
    }

    fun canMakeMove(): Boolean {
        val gridContainsEmptyTiles = field.any { tiles -> tiles.any { tile -> tile.number == 0 } }

        if (gridContainsEmptyTiles)
            return true

        val firstTile = field[0][0]

        return canMergeTile(firstTile)
    }

    private fun canMergeTile(tile: Tile): Boolean {
        if (tile.x == fieldSize - 1 && tile.y == fieldSize - 1)
            return false

        val rightNeighbourExists = tile.x != fieldSize - 1
        val downNeighbourExists = tile.y != fieldSize - 1

        if (rightNeighbourExists && downNeighbourExists) {
            val rightNeighbour = field[tile.x + 1][tile.y]
            val downNeighbour = field[tile.x][tile.y + 1]

            val caMergeTiles =
                tile.number == downNeighbour.number || tile.number == rightNeighbour.number

            if (caMergeTiles)
                return true

            return (canMergeTile(rightNeighbour) || canMergeTile(downNeighbour))
        }

        if (downNeighbourExists) {
            val downNeighbour = field[tile.x][tile.y + 1]

            if (tile.number == downNeighbour.number)
                return true

            return canMergeTile(downNeighbour)
        }

        if (rightNeighbourExists) {
            val rightNeighbour = field[tile.x + 1][tile.y]

            if (tile.number == rightNeighbour.number)
                return true

            return canMergeTile(rightNeighbour)
        }

        return false
    }

    private fun shiftByColumns(direction: Direction): Boolean {
        var isAnythingMoved = false

        for (i in 0 until fieldSize) {
            var column = getColumn(i)

            if (direction == Direction.DOWN)
                column.reverse()

            val shiftedColumn = shift(column)

            val columnChanged = checkLinesEquality(column, shiftedColumn)
            if (!isAnythingMoved)
                isAnythingMoved = columnChanged

            column = shiftedColumn

            if (direction == Direction.DOWN)
                column.reverse()

            setColumn(i, column)
        }

        return isAnythingMoved
    }

    private fun shiftByRows(direction: Direction): Boolean {
        var isAnythingMoved = false

        for (i in 0 until fieldSize) {
            var row = getRow(i)

            if (direction == Direction.RIGHT)
                row.reverse()

            val shiftedRow = shift(row)

            val rowChanged = checkLinesEquality(row, shiftedRow)
            if (!isAnythingMoved)
                isAnythingMoved = rowChanged

            row = shiftedRow

            if (direction == Direction.RIGHT)
                row.reverse()

            setRow(i, row)
        }

        return isAnythingMoved
    }

    private fun shift(oldLine: Array<Tile>): Array<Tile> {
        val resultLine = Array(fieldSize) { Tile(0, 0, 0) }

        val oldLineWithoutZeroes = Array(fieldSize) { Tile(0, 0, 0) }
        var q = 0
        for (i in oldLine.indices) {
            if (oldLine[i].number != 0) {
                oldLineWithoutZeroes[q].number = oldLine[i].number
                q++
            }
        }
        var i = 0
        q = 0

        while (i < oldLineWithoutZeroes.size) {
            val canMergeTiles =
                (i + 1 < oldLineWithoutZeroes.size)
                        && (oldLineWithoutZeroes[i].number == oldLineWithoutZeroes[i + 1].number)
                        && (oldLineWithoutZeroes[i].number != 0)

            if (canMergeTiles) {
                resultLine[q].number = oldLineWithoutZeroes[i].number * 2
                i++
            } else
                resultLine[q].number = oldLineWithoutZeroes[i].number
            i++
            q++
        }

        for (j in 0 until fieldSize) {
            resultLine[j].x = oldLine[j].x
            resultLine[j].y = oldLine[j].y
        }

        return resultLine
    }

    private fun checkLinesEquality(line: Array<Tile>, shiftedLine: Array<Tile>): Boolean {
        var isLineChanged = false

        for (i in 0 until fieldSize) {
            if (line[i].number != shiftedLine[i].number)
                isLineChanged = true
        }

        return isLineChanged
    }

    private fun getRow(index: Int): Array<Tile> {
        val row = Array(fieldSize) { Tile(0, 0, 0) }

        for (j in 0 until fieldSize) {
            row[j] = field[j][index]
        }
        return row
    }

    private fun setRow(index: Int, newLine: Array<Tile>) {
        for (j in 0 until fieldSize) {
            field[j][index] = newLine[j]
        }
    }

    private fun getColumn(index: Int): Array<Tile> {
        val get = Array(fieldSize) { Tile(0, 0, 0) }

        for (j in 0 until fieldSize) {
            get[j] = field[index][j]
        }
        return get
    }

    private fun setColumn(index: Int, newLine: Array<Tile>) {
        for (j in 0 until fieldSize) {
            field[index][j] = newLine[j]
        }
    }

    companion object {
        private const val CHANCE_OF_LUCKY_SPAWN: Int = 25
        private const val LUCKY_INITIAL_CELL_STATE: Int = 4
        private const val INITIAL_CELL_STATE: Int = 2
    }
}