package com.example.safezonesa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchNotifications: Switch
    private lateinit var switchLocation: Switch
    private lateinit var switchAutoRefresh: Switch
    private lateinit var tvAccountEmail: TextView
    private lateinit var tvAppVersion: TextView
    private lateinit var btnSaveSettings: Button
    private lateinit var btnBack: Button

    private val preferencesName = "SafeZoneSettings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switchNotifications =
            findViewById(R.id.switchNotifications)

        switchLocation =
            findViewById(R.id.switchLocation)

        switchAutoRefresh =
            findViewById(R.id.switchAutoRefresh)

        tvAccountEmail =
            findViewById(R.id.tvAccountEmail)

        tvAppVersion =
            findViewById(R.id.tvAppVersion)

        btnSaveSettings =
            findViewById(R.id.btnSaveSettings)

        btnBack =
            findViewById(R.id.btnBackSettings)

        loadSettings()
        displayAccountInformation()

        btnSaveSettings.setOnClickListener {
            saveSettings()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadSettings() {

        val preferences =
            getSharedPreferences(
                preferencesName,
                MODE_PRIVATE
            )

        switchNotifications.isChecked =
            preferences.getBoolean(
                "notifications",
                true
            )

        switchLocation.isChecked =
            preferences.getBoolean(
                "location_sharing",
                true
            )

        switchAutoRefresh.isChecked =
            preferences.getBoolean(
                "auto_refresh",
                true
            )
    }

    private fun saveSettings() {

        val preferences =
            getSharedPreferences(
                preferencesName,
                MODE_PRIVATE
            )

        preferences.edit()
            .putBoolean(
                "notifications",
                switchNotifications.isChecked
            )
            .putBoolean(
                "location_sharing",
                switchLocation.isChecked
            )
            .putBoolean(
                "auto_refresh",
                switchAutoRefresh.isChecked
            )
            .apply()

        Toast.makeText(
            this,
            "Settings saved successfully",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun displayAccountInformation() {

        val user =
            FirebaseAuth.getInstance().currentUser

        tvAccountEmail.text =
            user?.email ?: "No account information"

        tvAppVersion.text =
            "SafeZone SA Version 1.0"
    }
}