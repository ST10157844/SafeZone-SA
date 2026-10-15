package com.example.safezonesa.models

data class SubmitIncidentResponse(
    val success: Boolean,
    val message: String,
    val incidentID: Int?
)