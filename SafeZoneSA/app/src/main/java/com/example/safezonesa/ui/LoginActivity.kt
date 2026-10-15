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
import androidx.core.content.ContextCompat
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.safezonesa.R
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
//import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException

import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import com.example.safezonesa.network.UserSyncManager

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnGoogleSignIn: Button
    private lateinit var tvError: TextView
    private lateinit var tvForgotPassword: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn)
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

        btnGoogleSignIn.setOnClickListener {
            startGoogleSignIn()
        }
    }

    private fun loginWithFirebase(
        email: String,
        password: String
    ) {

        btnLogin.isEnabled = false
        btnRegister.isEnabled = false
        btnGoogleSignIn.isEnabled = false
        btnLogin.text = "Signing in..."

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

//                if (task.isSuccessful) {
//
//                    openDashboard()
//
//                } else {
//
//                    btnLogin.isEnabled = true
//                    btnRegister.isEnabled = true
//                    btnGoogleSignIn.isEnabled = true
//                    btnLogin.text = "Login"
//
//                    showError(
//                        "Incorrect email or password. Please try again."
//                    )
//                }
                if (task.isSuccessful) {

                    val firebaseUser =
                        auth.currentUser

                    if (firebaseUser != null) {

                        UserSyncManager.syncFirebaseUser(
                            context = this,
                            user = firebaseUser
                        ) { syncSuccessful ->

                            if (syncSuccessful) {

                                android.util.Log.d(
                                    "SafeZoneLogin",
                                    "User synchronized successfully"
                                )

                            } else {

                                android.util.Log.w(
                                    "SafeZoneLogin",
                                    "Database synchronization failed"
                                )
                            }
                        }
                    }

                    openDashboard()

                } else {

                    btnLogin.isEnabled = true
                    btnLogin.text = "Sign In"

                    tvError.visibility =
                        android.view.View.VISIBLE

                    tvError.text =
                        task.exception?.localizedMessage
                            ?: "Unable to sign in. Check your email and password."
                }

            }
    }

    private fun startGoogleSignIn() {

        btnGoogleSignIn.isEnabled = false
        btnGoogleSignIn.text = "Connecting..."
        tvError.visibility = View.GONE

        val googleOption =
            GetSignInWithGoogleOption.Builder(
                getString(R.string.default_web_client_id)
            )
                .build()

        val request =
            GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .build()

        credentialManager.getCredentialAsync(
            this,
            request,
            null,
            ContextCompat.getMainExecutor(this),
            object :
                CredentialManagerCallback<
                        GetCredentialResponse,
                        GetCredentialException
                        > {

                override fun onResult(
                    result: GetCredentialResponse
                ) {
                    handleGoogleCredential(
                        result.credential
                    )
                }

                override fun onError(
                    e: GetCredentialException
                ) {

                    resetGoogleButton()

                    Log.e(
                        "SafeZoneGoogle",
                        "Google sign-in error: ${e::class.java.simpleName}: ${e.message}",
                        e
                    )

                    when (e) {

                        is NoCredentialException -> {
                            showError(
                                "No Google account is available. Add a Google account to this device and try again."
                            )
                        }

                        is GetCredentialCancellationException -> {
                            showError(
                                "Google sign-in was cancelled."
                            )
                        }

                        else -> {
                            showError(
                                "Google sign-in failed: ${e.message ?: e::class.java.simpleName}"
                            )
                        }
                    }
                }
            }
        )
    }

    private fun handleGoogleCredential(
        credential: Credential
    ) {

        if (
            credential is CustomCredential &&
            credential.type ==
            TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            try {

                val googleIdTokenCredential =
                    GoogleIdTokenCredential
                        .createFrom(
                            credential.data
                        )

                firebaseAuthWithGoogle(
                    googleIdTokenCredential.idToken
                )

            } catch (e: Exception) {

                resetGoogleButton()

                showError(
                    "Unable to read Google account information."
                )
            }

        } else {

            resetGoogleButton()

            showError(
                "The selected credential is not a Google account."
            )
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String
    ) {

        val firebaseCredential =
            GoogleAuthProvider.getCredential(
                idToken,
                null
            )

        auth.signInWithCredential(
            firebaseCredential
        )
            .addOnCompleteListener(this) { task ->

//                if (task.isSuccessful) {
//
//                    Toast.makeText(
//                        this,
//                        "Google sign-in successful!",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    openDashboard()
//
//                } else {
//
//                    resetGoogleButton()
//
//                    showError(
//                        task.exception?.localizedMessage
//                            ?: "Google sign-in failed."
//                    )
//                }
                if (task.isSuccessful) {

                    val firebaseUser =
                        auth.currentUser

                    if (firebaseUser != null) {

                        UserSyncManager.syncFirebaseUser(
                            this,
                            firebaseUser
                        ) {
                            openDashboard()
                        }

                    } else {
                        openDashboard()
                    }
                }
            }
    }

    private fun sendPasswordReset(
        email: String
    ) {

        tvForgotPassword.isEnabled = false
        tvForgotPassword.text = "Sending..."

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->

                tvForgotPassword.isEnabled = true
                tvForgotPassword.text =
                    "Forgot Password?"

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Password reset email sent. Check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()

                    tvError.text =
                        "Password reset email sent. Check your inbox."

                    tvError.visibility =
                        View.VISIBLE

                } else {

                    showError(
                        task.exception?.localizedMessage
                            ?: "Unable to send password reset email."
                    )
                }
            }
    }

    private fun openDashboard() {

        val intent =
            Intent(
                this,
                DashboardActivity::class.java
            )

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

    private fun resetGoogleButton() {

        btnGoogleSignIn.isEnabled = true
        btnGoogleSignIn.text =
            "Continue with Google"
    }

    private fun showError(
        message: String
    ) {

        tvError.text = message
        tvError.visibility = View.VISIBLE
    }
}