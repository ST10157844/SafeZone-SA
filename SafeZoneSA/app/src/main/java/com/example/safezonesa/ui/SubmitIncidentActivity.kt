//package com.example.safezonesa.ui
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.example.safezonesa.R
//import com.example.safezonesa.models.Incident
//import com.example.safezonesa.network.RetrofitClient
//import okhttp3.ResponseBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class SubmitIncidentActivity : AppCompatActivity() {
//
//    private val incidentTypes = arrayOf(
//        "Select incident type",
//        "Robbery", "Vandalism", "Suspicious Activity",
//        "Fire Hazard", "Road Accident", "Assault",
//        "Noise Complaint", "Break-In", "Other"
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_submit_incident)
//
//        val spinnerType     = findViewById<Spinner>(R.id.spinnerType)
//        val edtLocation     = findViewById<EditText>(R.id.edtLocation)
//        val edtDesc         = findViewById<EditText>(R.id.edtDesc)
//        val tvCharCount     = findViewById<TextView>(R.id.tvCharCount)
//        val switchAnonymous = findViewById<Switch>(R.id.switchAnonymous)
//        val btnSubmit       = findViewById<Button>(R.id.btnSubmit)
//        val btnBack         = findViewById<Button>(R.id.btnBack)
//
//        // Spinner adapter with dark style
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incidentTypes)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerType.adapter = adapter
//
//        // Live character counter
//        edtDesc.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                tvCharCount.text = "${s?.length ?: 0} / 500"
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//
//        btnBack.setOnClickListener {
//            finish()
//            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//        }
//
//        btnSubmit.setOnClickListener {
//            val selectedType = spinnerType.selectedItem?.toString() ?: ""
//            val desc         = edtDesc.text.toString().trim()
//            val location     = edtLocation.text.toString().trim()
//
//            if (selectedType == "Select incident type" || selectedType.isEmpty()) {
//                Toast.makeText(this, "Please select an incident type.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (desc.isEmpty()) {
//                edtDesc.error = "Description is required"
//                return@setOnClickListener
//            }
//
//            btnSubmit.isEnabled = false
//            btnSubmit.text = "Submitting…"
//
//            val incident = Incident(
//                type        = selectedType,
//                description = desc,
//                location    = location
//            )
//
//            RetrofitClient.api.submitIncident(incident)
//                .enqueue(object : Callback<ResponseBody> {
//                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                        Toast.makeText(
//                            this@SubmitIncidentActivity,
//                            "✅ Incident reported successfully!",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        finish()
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//                    }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                        // For prototype/demo: still show success (no live server)
//                        Toast.makeText(
//                            this@SubmitIncidentActivity,
//                            "✅ Incident logged (offline mode).",
//                            Toast.LENGTH_LONG
//                        ).show()
//                        btnSubmit.isEnabled = true
//                        btnSubmit.text = getString(R.string.submit)
//                        finish()
//                    }
//                })
//        }
//    }
//}
package com.example.safezonesa.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safezonesa.R
import com.example.safezonesa.models.SubmitIncidentRequest
import com.example.safezonesa.models.SubmitIncidentResponse
import com.example.safezonesa.network.RetrofitClient
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.safezonesa.utils.IncidentValidator

class SubmitIncidentActivity : AppCompatActivity() {

    private lateinit var spinnerType: Spinner
    private lateinit var edtLocation: EditText
    private lateinit var edtDesc: EditText
    private lateinit var tvCharCount: TextView
    private lateinit var switchAnonymous: Switch
    private lateinit var btnSubmit: Button
    private lateinit var btnBack: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_submit_incident
        )

        auth =
            FirebaseAuth.getInstance()

        spinnerType =
            findViewById(R.id.spinnerType)

        edtLocation =
            findViewById(R.id.edtLocation)

        edtDesc =
            findViewById(R.id.edtDesc)

        tvCharCount =
            findViewById(R.id.tvCharCount)

        switchAnonymous =
            findViewById(R.id.switchAnonymous)

        btnSubmit =
            findViewById(R.id.btnSubmit)

        btnBack =
            findViewById(R.id.btnBack)


        setupIncidentTypes()

        setupCharacterCounter()


        btnBack.setOnClickListener {
            finish()
        }


        btnSubmit.setOnClickListener {
            validateAndSubmit()
        }
    }


    private fun setupIncidentTypes() {

        val incidentTypes = listOf(
            "Select incident type",
            "Robbery",
            "Suspicious Activity",
            "Vandalism",
            "Theft",
            "Vehicle Break-in",
            "Streetlight Failure",
            "Assault",
            "Noise Disturbance",
            "Burglary",
            "Illegal Dumping",
            "Other"
        )


        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                incidentTypes
            )


        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )


        spinnerType.adapter =
            adapter
    }


    private fun setupCharacterCounter() {

        edtDesc.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }


                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    val length =
                        s?.length ?: 0

                    tvCharCount.text =
                        "$length / 500"
                }


                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }


    private fun validateAndSubmit() {

        val selectedType =
            spinnerType.selectedItem
                ?.toString()
                ?.trim()
                .orEmpty()


        val location =
            edtLocation.text
                .toString()
                .trim()


        val description =
            edtDesc.text
                .toString()
                .trim()


        when {

            spinnerType.selectedItemPosition == 0 -> {

                Toast.makeText(
                    this,
                    "Please select an incident type.",
                    Toast.LENGTH_SHORT
                ).show()

                spinnerType.requestFocus()
            }


            location.isEmpty() -> {

                edtLocation.error =
                    "Location is required"

                edtLocation.requestFocus()
            }


            description.isEmpty() -> {

                edtDesc.error =
                    "Description is required"

                edtDesc.requestFocus()
            }


            description.length < 10 -> {

                edtDesc.error =
                    "Please provide at least 10 characters"

                edtDesc.requestFocus()
            }


            description.length > 500 -> {

                edtDesc.error =
                    "Description cannot exceed 500 characters"

                edtDesc.requestFocus()
            }


            else -> {

                submitIncident(
                    type = selectedType,
                    location = location,
                    description = description
                )
            }
        }
    }


    private fun submitIncident(
        type: String,
        location: String,
        description: String
    ) {

        val firebaseUser =
            auth.currentUser


        if (firebaseUser == null) {

            Toast.makeText(
                this,
                "Your session has expired. Please sign in again.",
                Toast.LENGTH_LONG
            ).show()

            return
        }


        val priority =
            IncidentValidator.determinePriority(type)

        android.util.Log.d(
            "SafeZoneSubmit",
            "Firebase UID available = ${firebaseUser.uid.isNotBlank()}"
        )

        val request =
            SubmitIncidentRequest(
                firebaseUid = firebaseUser.uid,
                type = type,
                description = description,
                location = location,
                priority = priority,
                anonymous = switchAnonymous.isChecked
            )


        setSubmittingState(true)


        RetrofitClient.api
            .submitIncident(request)
            .enqueue(
                object : Callback<SubmitIncidentResponse> {


                    override fun onResponse(
                        call:
                        Call<SubmitIncidentResponse>,

                        response:
                        Response<SubmitIncidentResponse>
                    ) {

                        setSubmittingState(false)


                        val body =
                            response.body()


                        if (
                            response.isSuccessful &&
                            body?.success == true
                        ) {

                            Toast.makeText(
                                this@SubmitIncidentActivity,
                                "Incident #${body.incidentID} submitted successfully.",
                                Toast.LENGTH_LONG
                            ).show()


                            finish()

                        } else {

                            Toast.makeText(
                                this@SubmitIncidentActivity,
                                body?.message
                                    ?: "Unable to submit incident. Server error ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }


                    override fun onFailure(
                        call:
                        Call<SubmitIncidentResponse>,

                        t: Throwable
                    ) {

                        setSubmittingState(false)


                        Toast.makeText(
                            this@SubmitIncidentActivity,
                            "Connection failed: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
    }


//    private fun determinePriority(
//        type: String
//    ): String {
//
//        return when (type) {
//
//            "Assault",
//            "Robbery" ->
//                "critical"
//
//
//            "Burglary",
//            "Vehicle Break-in" ->
//                "high"
//
//
//            "Theft",
//            "Suspicious Activity",
//            "Vandalism" ->
//                "medium"
//
//
//            else ->
//                "low"
//        }
//    }


    private fun setSubmittingState(
        submitting: Boolean
    ) {

        btnSubmit.isEnabled =
            !submitting


        btnBack.isEnabled =
            !submitting


        spinnerType.isEnabled =
            !submitting


        edtLocation.isEnabled =
            !submitting


        edtDesc.isEnabled =
            !submitting


        switchAnonymous.isEnabled =
            !submitting


        btnSubmit.text =
            if (submitting) {
                "Submitting..."
            } else {
                getString(R.string.submit)
            }


        btnSubmit.visibility =
            View.VISIBLE
    }
}