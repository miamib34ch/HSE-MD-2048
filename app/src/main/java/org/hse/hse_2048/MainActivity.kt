package org.hse.hse_2048

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.hse.hse_2048.game.GameActivity
import org.hse.hse_2048.settings.PreferenceManager
import org.hse.hse_2048.settings.SettingsActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val gameRows: Int = 4
    lateinit var preferenceManager: PreferenceManager

    private lateinit var resumeButton: Button
    private lateinit var startButton: Button
    private lateinit var ratingButton: Button
    private lateinit var settingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this)
        changeLanguage()

        setContentView(R.layout.activity_main)
        resumeButton = findViewById(R.id.resume_button)
        startButton = findViewById(R.id.start_button)
        ratingButton = findViewById(R.id.rating_button)
        settingsButton = findViewById(R.id.settings_button)

        resumeButton.setOnClickListener(this::onResumeButtonClick)
        startButton.setOnClickListener(this::onStartButtonClick)
        ratingButton.setOnClickListener(this::onRatingButtonClick)
        settingsButton.setOnClickListener(this::onSettingsButtonClick)

    }

    private fun changeLanguage() {
        val languageToLoad =  preferenceManager.getValue("language", "ru")

        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config,
            baseContext.resources.displayMetrics)
    }

    private fun onSettingsButtonClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if (startButton.text == "Начать игру" && preferenceManager.getValue("language", "ru") == "en") {
            changeLanguage()
            recreate()
        }
        else if (startButton.text == "Start Game" && preferenceManager.getValue("language", "ru") == "ru") {
            changeLanguage()
            recreate()
        }

        val field = preferenceManager.getValue("gameField", "")
        if (field.isNullOrEmpty()) {
            resumeButton.visibility = View.GONE
        }
        else {
            resumeButton.visibility = View.VISIBLE
        }
    }

    private fun onResumeButtonClick(view: View) {

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("gameRows", gameRows)
        intent.putExtra("newGame", false)
        startActivity(intent)
    }

    private fun onStartButtonClick(view: View) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("newGame", true)
        intent.putExtra("gameRows", gameRows)
        startActivity(intent)
    }

    private fun onRatingButtonClick(view: View) {
        val intent = Intent(this, RatingActivity::class.java)
        startActivity(intent)
    }
}
