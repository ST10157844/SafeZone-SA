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
    private lateinit var tvForgotPassword: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        tvError = findViewById(R.id.tvError)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

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

        btnRegister.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        tvForgotPassword.setOnClickListener {

            val email = edtEmail.text.toString().trim()

            tvError.visibility = View.GONE

            when {

                email.isEmpty() -> {
                    showError(
                        "Enter your email address first, then tap Forgot Password."
                    )
                    edtEmail.requestFocus()
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showError("Please enter a valid email address.")
                    edtEmail.requestFocus()
                }

                else -> {
                    sendPasswordReset(email)
                }
            }
        }
    }

    private fun loginWithFirebase(
        email: String,
        password: String
    ) {

        btnLogin.isEnabled = false
        btnRegister.isEnabled = false
        btnLogin.text = "Signing in..."

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

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)

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

    private fun sendPasswordReset(email: String) {

        tvForgotPassword.isEnabled = false
        tvForgotPassword.text = "Sending..."

        Toast.makeText(
            this,
            "Sending reset email...",
            Toast.LENGTH_SHORT
        ).show()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->

                tvForgotPassword.isEnabled = true
                tvForgotPassword.text = "Forgot Password?"

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Password reset email sent. Check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()

                    tvError.text =
                        "Password reset email sent. Check your inbox."

                    tvError.visibility = View.VISIBLE

                } else {

                    val message =
                        task.exception?.localizedMessage
                            ?: "Unable to send password reset email."

                    showError(message)

                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun showError(message: String) {

        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}