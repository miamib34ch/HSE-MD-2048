package org.hse.hse_2048

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.hse.hse_2048.settings.PreferenceManager

class RatingActivity : AppCompatActivity() {

    lateinit var preferenceManager: PreferenceManager

    private lateinit var nameText: TextView
    private lateinit var scoreText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        preferenceManager = PreferenceManager(this)

        nameText = findViewById(R.id.your_name)
        scoreText = findViewById(R.id.your_score)

        nameText.text = preferenceManager.getValue("name", "Player")
        scoreText.text = preferenceManager.getValue("max_score", 0).toString()
    }
}