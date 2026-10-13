package com.example.safezonesa.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Security check:
        // If nobody is signed in, do not allow access to Dashboard.
        if (auth.currentUser == null) {

            goToLogin()

            return
        }

        setContentView(R.layout.activity_dashboard)

        val cardSubmit =
            findViewById<LinearLayout>(R.id.cardSubmit)

        val cardView =
            findViewById<LinearLayout>(R.id.cardView)

        val btnLogout =
            findViewById<Button>(R.id.btnLogout)

        // Open Submit Incident screen
        cardSubmit.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    SubmitIncidentActivity::class.java
                )
            )

            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }

        // Open My Incidents screen
        cardView.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    MyIncidentsActivity::class.java
                )
            )

            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }

        // Firebase Logout
        btnLogout.setOnClickListener {

            auth.signOut()

            goToLogin()
        }
    }

    override fun onStart() {
        super.onStart()

        // Check the session every time Dashboard becomes active
        if (::auth.isInitialized && auth.currentUser == null) {

            goToLogin()
        }
    }

    private fun goToLogin() {

        val intent =
            Intent(
                this,
                LoginActivity::class.java
            )

        // Clear Dashboard and all authenticated screens
        // from the back stack
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )

        finish()
    }
}