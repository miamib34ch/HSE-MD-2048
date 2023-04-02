package org.hse.hse_2048.settings

import android.content.Context
import org.hse.hse_2048.R

class SettingsService: SettingsDelegate {
    var preferenceManager: PreferenceManager

    override var gameBackgroundMusic: Music = Music(R.raw.backgroundmusic)

    override var isMusicActive: Boolean
        get() = preferenceManager.getValue("isMusicActive", true)
        set(value) { preferenceManager.saveValue("isMusicActive", value) }

    constructor(context: Context) {
        preferenceManager = PreferenceManager(context)
    }
}
