package com.example.safezonesa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnBack: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialise Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Connect Kotlin variables to XML controls
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnBack = findViewById(R.id.btnBack)

        // Back button
        btnBack.setOnClickListener {
            finish()

            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }

        // Register button
        btnRegister.setOnClickListener {

            val name = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val password = edtPassword.text.toString()

            when {

                name.isEmpty() -> {
                    edtName.error = "Full name is required"
                    edtName.requestFocus()
                }

                email.isEmpty() -> {
                    edtEmail.error = "Email is required"
                    edtEmail.requestFocus()
                }

                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches() -> {

                    edtEmail.error = "Enter a valid email address"
                    edtEmail.requestFocus()
                }

                phone.isEmpty() -> {
                    edtPhone.error = "Phone number is required"
                    edtPhone.requestFocus()
                }

                phone.length < 10 -> {
                    edtPhone.error = "Enter a valid phone number"
                    edtPhone.requestFocus()
                }

                password.isEmpty() -> {
                    edtPassword.error = "Password is required"
                    edtPassword.requestFocus()
                }

                password.length < 6 -> {
                    edtPassword.error =
                        "Password must be at least 6 characters"

                    edtPassword.requestFocus()
                }

                else -> {
                    createFirebaseAccount(
                        name,
                        email,
                        password
                    )
                }
            }
        }
    }

    private fun createFirebaseAccount(
        name: String,
        email: String,
        password: String
    ) {

        // Prevent the user pressing Register many times
        btnRegister.isEnabled = false
        btnRegister.text = "Creating account..."

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {

                    val user = auth.currentUser

                    // Store the user's full name in the Firebase profile
                    val profileUpdates =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener {

                            Toast.makeText(
                                this,
                                "Account created successfully!",
                                Toast.LENGTH_LONG
                            ).show()

                            // Firebase automatically signs in a newly
                            // registered user. Sign out so that we can
                            // properly test the Login screen next.
                            auth.signOut()

                            finish()

                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        }

                } else {

                    btnRegister.isEnabled = true
                    btnRegister.text = "Register"

                    when (val exception = task.exception) {

                        is FirebaseAuthUserCollisionException -> {

                            edtEmail.error =
                                "An account already exists with this email"

                            edtEmail.requestFocus()
                        }

                        is FirebaseAuthWeakPasswordException -> {

                            edtPassword.error =
                                "Please choose a stronger password"

                            edtPassword.requestFocus()
                        }

                        else -> {

                            Toast.makeText(
                                this,
                                exception?.localizedMessage
                                    ?: "Registration failed. Please try again.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
    }
}