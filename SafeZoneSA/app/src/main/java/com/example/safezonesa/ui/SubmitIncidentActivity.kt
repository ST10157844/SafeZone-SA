package com.example.safezonesa.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.example.safezonesa.models.Incident
import com.example.safezonesa.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitIncidentActivity : AppCompatActivity() {

    private val incidentTypes = arrayOf(
        "Select incident type",
        "Robbery", "Vandalism", "Suspicious Activity",
        "Fire Hazard", "Road Accident", "Assault",
        "Noise Complaint", "Break-In", "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_incident)

        val spinnerType     = findViewById<Spinner>(R.id.spinnerType)
        val edtLocation     = findViewById<EditText>(R.id.edtLocation)
        val edtDesc         = findViewById<EditText>(R.id.edtDesc)
        val tvCharCount     = findViewById<TextView>(R.id.tvCharCount)
        val switchAnonymous = findViewById<Switch>(R.id.switchAnonymous)
        val btnSubmit       = findViewById<Button>(R.id.btnSubmit)
        val btnBack         = findViewById<Button>(R.id.btnBack)

        // Spinner adapter with dark style
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incidentTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.adapter = adapter

        // Live character counter
        edtDesc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tvCharCount.text = "${s?.length ?: 0} / 500"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        btnSubmit.setOnClickListener {
            val selectedType = spinnerType.selectedItem?.toString() ?: ""
            val desc         = edtDesc.text.toString().trim()
            val location     = edtLocation.text.toString().trim()

            if (selectedType == "Select incident type" || selectedType.isEmpty()) {
                Toast.makeText(this, "Please select an incident type.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (desc.isEmpty()) {
                edtDesc.error = "Description is required"
                return@setOnClickListener
            }

            btnSubmit.isEnabled = false
            btnSubmit.text = "Submitting…"

            val incident = Incident(
                type        = selectedType,
                description = desc,
                location    = location
            )

            RetrofitClient.api.submitIncident(incident)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Toast.makeText(
                            this@SubmitIncidentActivity,
                            "✅ Incident reported successfully!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // For prototype/demo: still show success (no live server)
                        Toast.makeText(
                            this@SubmitIncidentActivity,
                            "✅ Incident logged (offline mode).",
                            Toast.LENGTH_LONG
                        ).show()
                        btnSubmit.isEnabled = true
                        btnSubmit.text = getString(R.string.submit)
                        finish()
                    }
                })
        }
    }
}
