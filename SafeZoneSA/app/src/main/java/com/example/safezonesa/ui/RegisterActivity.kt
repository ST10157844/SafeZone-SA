package com.example.safezonesa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.example.safezonesa.network.UserSyncManager
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

        auth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)

        btnRegister = findViewById(R.id.btnRegister)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {

            finish()

            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }

        btnRegister.setOnClickListener {

            val name =
                edtName.text.toString().trim()

            val email =
                edtEmail.text.toString().trim()

            val phone =
                edtPhone.text.toString().trim()

            val password =
                edtPassword.text.toString()

            when {

                name.isEmpty() -> {

                    edtName.error =
                        "Full name is required"

                    edtName.requestFocus()
                }

                email.isEmpty() -> {

                    edtEmail.error =
                        "Email is required"

                    edtEmail.requestFocus()
                }

                !android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email)
                    .matches() -> {

                    edtEmail.error =
                        "Enter a valid email address"

                    edtEmail.requestFocus()
                }

                phone.isEmpty() -> {

                    edtPhone.error =
                        "Phone number is required"

                    edtPhone.requestFocus()
                }

                phone.length < 10 -> {

                    edtPhone.error =
                        "Enter a valid phone number"

                    edtPhone.requestFocus()
                }

                password.isEmpty() -> {

                    edtPassword.error =
                        "Password is required"

                    edtPassword.requestFocus()
                }

                password.length < 6 -> {

                    edtPassword.error =
                        "Password must be at least 6 characters"

                    edtPassword.requestFocus()
                }

                else -> {

                    createFirebaseAccount(
                        name = name,
                        email = email,
                        phone = phone,
                        password = password
                    )
                }
            }
        }
    }

    private fun createFirebaseAccount(
        name: String,
        email: String,
        phone: String,
        password: String
    ) {

        btnRegister.isEnabled = false
        btnRegister.text = "Creating account..."

        auth.createUserWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val user = auth.currentUser

                if (user == null) {

                    resetRegisterButton()

                    Toast.makeText(
                        this,
                        "Account created, but Firebase user could not be loaded.",
                        Toast.LENGTH_LONG
                    ).show()

                    return@addOnCompleteListener
                }

                val profileUpdates =
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                user.updateProfile(profileUpdates)
                    .addOnCompleteListener { profileTask ->

                        if (profileTask.isSuccessful) {

                            syncUserWithSafeZoneDatabase(
                                phone = phone
                            )

                        } else {

                            /*
                             * The Firebase account still exists even if
                             * updating the display name fails.
                             *
                             * We allow the user to continue and the
                             * account can be synchronized again at login.
                             */

                            auth.signOut()

                            Toast.makeText(
                                this,
                                "Account created successfully. Please sign in.",
                                Toast.LENGTH_LONG
                            ).show()

                            finish()

                            overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                            )
                        }
                    }

            } else {

                resetRegisterButton()

                when (
                    val exception = task.exception
                ) {

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

    private fun syncUserWithSafeZoneDatabase(
        phone: String
    ) {

        val firebaseUser =
            auth.currentUser

        if (firebaseUser == null) {

            resetRegisterButton()

            Toast.makeText(
                this,
                "Unable to synchronize account.",
                Toast.LENGTH_LONG
            ).show()

            return
        }

        btnRegister.text =
            "Saving profile..."

        UserSyncManager.syncFirebaseUser(
            context = this,
            user = firebaseUser,
            phone = phone
        ) { syncSuccessful ->

            /*
             * Firebase automatically signs the user in
             * immediately after registration.
             *
             * We sign out so the Login screen can be tested
             * normally.
             */
            auth.signOut()

            if (syncSuccessful) {

                Toast.makeText(
                    this,
                    "Account created and SafeZone profile saved successfully!",
                    Toast.LENGTH_LONG
                ).show()

            } else {

                /*
                 * The Firebase account was still created.
                 * Login synchronization will try again later.
                 */
                Toast.makeText(
                    this,
                    "Account created successfully. Profile will synchronize when you sign in.",
                    Toast.LENGTH_LONG
                ).show()
            }

            finish()

            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }

    private fun resetRegisterButton() {

        btnRegister.isEnabled = true
        btnRegister.text = "Register"
    }
}