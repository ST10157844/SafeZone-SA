package com.example.safezonesa.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var tvError: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialise Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Connect Kotlin variables to XML controls
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        tvError = findViewById(R.id.tvError)

        // Login button
        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            // Hide previous error
            tvError.visibility = View.GONE

            when {

                email.isEmpty() -> {
                    showError("Email address is required.")
                    edtEmail.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showError("Please enter a valid email address.")
                    edtEmail.requestFocus()
                }

                password.isEmpty() -> {
                    showError("Password is required.")
                    edtPassword.requestFocus()
                }

                password.length < 6 -> {
                    showError("Password must be at least 6 characters.")
                    edtPassword.requestFocus()
                }

                else -> {
                    loginWithFirebase(email, password)
                }
            }
        }

        // Open registration screen
        btnRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )

            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }
    }

    private fun loginWithFirebase(
        email: String,
        password: String
    ) {

        btnLogin.isEnabled = false
        btnRegister.isEnabled = false
        btnLogin.text = "Signing in..."

        tvError.visibility = View.GONE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent =
                        Intent(
                            this,
                            DashboardActivity::class.java
                        )

                    // Prevent Back button from returning
                    // to the login screen after successful login
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)

                    overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )

                } else {

                    btnLogin.isEnabled = true
                    btnRegister.isEnabled = true
                    btnLogin.text = "Login"

                    showError(
                        "Incorrect email or password. Please try again."
                    )
                }
            }
    }

    private fun showError(message: String) {

        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}