package com.example.safezonesa.models

data class IncidentResponse(

    val incidentID: Int,

    val type: String,

    val description: String,

    val status: String,

    val priority: String,

    val submittedAt: String,

    val reporterName: String? = null,

    val location: String? = null,

    val suburb: String? = null,

    val city: String? = null,

    val anonymous: Boolean? = false
)