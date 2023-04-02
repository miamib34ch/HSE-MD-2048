package org.hse.hse_2048.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.hse.hse_2048.R
import org.hse.hse_2048.settings.PreferenceManager
import org.hse.hse_2048.settings.SettingsDelegate
import org.hse.hse_2048.settings.SettingsService
import java.lang.Integer.max
import java.lang.reflect.Type

class GameActivity : AppCompatActivity() {

    private lateinit var gameField: GameField
    private lateinit var frame: FrameLayout
    private lateinit var scoreText: TextView
    private lateinit var finalGameMessage: TextView
    private lateinit var finalGameButton: Button
    private lateinit var finalScreen: ConstraintLayout
    private lateinit var gameScreen: ConstraintLayout

    lateinit var preferenceManager: PreferenceManager

    var gameRows: Int = 0

    var delegate: SettingsDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this)

        delegate = SettingsService(this)
        if (delegate?.isMusicActive == true){
            delegate?.gameBackgroundMusic?.playMusic(this)
            delegate?.gameBackgroundMusic?.setVolume(preferenceManager.getValue("volume",0.5f))
        }

        setContentView(R.layout.activity_game)
        gameScreen = findViewById(R.id.game_layout)
        finalGameMessage = findViewById(R.id.game_final_message)
        finalGameButton = findViewById(R.id.game_final_button)
        finalScreen = findViewById(R.id.final_screen)
        scoreText = findViewById(R.id.score)

        finalGameButton.setOnClickListener(this::onFinalGameButtonClick)

        gameRows = intent.extras?.get("gameRows") as Int
        val isNewGame = intent.extras?.get("newGame") as Boolean
        gameField = GameField(this, this)

        if (!isNewGame) {
            val gameFieldJson = preferenceManager.getValue("gameField", "")
            val gson = Gson()
            val type: Type = object : TypeToken<Grid>() {}.type
            val grid = gson.fromJson<Grid>(gameFieldJson, type)
            gameField.grid = grid
        }

        val directionDetector = object : DirectionDetector() {
            override fun onDirectionDetected(direction: Direction) {
                makeMove(direction)
                scoreText.text = gameField.grid.getSum().toString()
            }
        }

        frame = findViewById(R.id.gameField)
        frame.addView(gameField)

        frame.setOnTouchListener(directionDetector)

        startGame()
    }

    override fun onStop() {
        super.onStop()

        delegate?.gameBackgroundMusic?.stopMusic()

        val field = preferenceManager.getValue("gameField", "")
        if (field.isNullOrEmpty()) {
            return
        }

        val sum = gameField.grid.getSum()
        val gson = Gson()
        val jsonString = gson.toJson(gameField.grid)

        preferenceManager.saveValue("gameField", jsonString)
        preferenceManager.saveValue("max_score", max(preferenceManager.getValue("max_score", sum), sum))
    }

    fun endGame(isPlayerWon: Boolean) {
        val message = when (isPlayerWon) {
            true -> getString(R.string.won)
            false -> getString(R.string.lose)
        }
        frame.setOnTouchListener(null)

        finalGameMessage.text = message

        val fadeAnimation = Fade()
        fadeAnimation.duration = 600
        fadeAnimation.addTarget(finalScreen)
        TransitionManager.beginDelayedTransition(gameScreen, fadeAnimation)

        finalScreen.visibility = View.VISIBLE
        preferenceManager.removeKey("gameField")
    }

    private fun onFinalGameButtonClick(view: View?) {
        this.finish()
    }

    private fun startGame() {
        gameField.startGame()
        val sum = gameField.grid.getSum()
        val gson = Gson()
        val jsonString = gson.toJson(gameField.grid)

        scoreText.text = sum.toString()

        preferenceManager.saveValue("gameField", jsonString)
        preferenceManager.saveValue("max_score", max(preferenceManager.getValue("max_score", sum), sum))
    }

    private fun makeMove(direction: Direction) {
        gameField.makeMove(direction)
    }
}
