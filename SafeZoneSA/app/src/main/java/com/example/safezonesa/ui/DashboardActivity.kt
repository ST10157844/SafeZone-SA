package com.example.safezonesa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val cardSubmit  = findViewById<LinearLayout>(R.id.cardSubmit)
        val cardView    = findViewById<LinearLayout>(R.id.cardView)
        val btnLogout   = findViewById<Button>(R.id.btnLogout)

        cardSubmit.setOnClickListener {
            startActivity(Intent(this, SubmitIncidentActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        cardView.setOnClickListener {
            startActivity(Intent(this, MyIncidentsActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        btnLogout.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
