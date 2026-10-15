package com.example.safezonesa.models

data class SubmitIncidentRequest(
    val firebaseUid: String,
    val type: String,
    val description: String,
    val location: String,
    val priority: String,
    val anonymous: Boolean
)