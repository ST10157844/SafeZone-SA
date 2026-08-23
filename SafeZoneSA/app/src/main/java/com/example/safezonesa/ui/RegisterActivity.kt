package com.example.safezonesa.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtName     = findViewById(R.id.edtName)
        edtEmail    = findViewById(R.id.edtEmail)
        edtPhone    = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnBack     = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        btnRegister.setOnClickListener {
            val name  = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val pass  = edtPassword.text.toString().trim()

            when {
                name.isEmpty()  -> { edtName.error = "Full name is required" }
                email.isEmpty() -> { edtEmail.error = "Email is required" }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    edtEmail.error = "Enter a valid email"
                }
                phone.isEmpty() -> { edtPhone.error = "Phone number is required" }
                pass.length < 6 -> { edtPassword.error = "Password must be at least 6 characters" }
                else -> {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        }
    }
}
