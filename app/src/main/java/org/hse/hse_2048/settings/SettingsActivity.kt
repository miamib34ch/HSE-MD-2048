package org.hse.hse_2048.settings

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.hse.hse_2048.R
import java.util.*

class SettingsActivity: AppCompatActivity(){

    lateinit var preferenceManager: PreferenceManager
    var isMusicActive: Boolean
        get() = preferenceManager.getValue("isMusicActive", true)
        set(value){
            preferenceManager.saveValue("isMusicActive", value)
        }
    var language: String?
        get() = preferenceManager.getValue("language", "ru")
        set(value){
            preferenceManager.saveValue("language", value)
        }
    var nameEdit: String?
        get() = preferenceManager.getValue("name", "Player")
        set(value){
            preferenceManager.saveValue("name", value)
        }
    var music: Float
        get() = preferenceManager.getValue("volume", 0.5f)
        set(value){
            preferenceManager.saveValue("volume", value)
        }

    lateinit var music_button: Button
    lateinit var language_button: Button
    lateinit var name_edit: EditText
    lateinit var seekBar: SeekBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferenceManager = PreferenceManager(this)


        music_button = findViewById(R.id.music_button)
        language_button = findViewById(R.id.language_button)
        name_edit = findViewById(R.id.name_edit)
        var save_button = findViewById<Button>(R.id.save_button)
        seekBar = findViewById(R.id.seekBar)

        getSettings()
        seekBar.progress = (music * 100).toInt()

        music_button.setOnClickListener(this::onMusicButtonClick)
        language_button.setOnClickListener(this::onLanguageButtonClick)
        save_button.setOnClickListener(this::onSaveButtonClick)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val vol = progress.toFloat() / seekBar.max.toFloat()
                music = vol
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun onMusicButtonClick(view: View) {
        isMusicActive = !isMusicActive
        getSettings();
    }

    private fun onLanguageButtonClick(view: View) {
        when (language){
            "ru" -> {language = "en"; changeLanguage("en")}
            "en" -> {language = "ru"; changeLanguage("ru")}
        }
        getSettings()
        recreate()
    }
    private fun onSaveButtonClick(view: View) {
        nameEdit = name_edit.text.toString()
        getSettings()
        Toast.makeText(applicationContext, getString(R.string.saved_message), Toast.LENGTH_SHORT).show();
    }

    fun getSettings(){
        if (isMusicActive)
            music_button.text = resources.getString(R.string.music_button_on)
        else
            music_button.text = resources.getString(R.string.music_button_off)
        if (language == "ru")
            language_button.text = resources.getString(R.string.language_button_ru)
        else
            language_button.text = resources.getString(R.string.language_button_en)
        name_edit.setText(nameEdit)
    }

    fun changeLanguage(languageToLoad: String){
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config,
            resources.displayMetrics)
    }
}
