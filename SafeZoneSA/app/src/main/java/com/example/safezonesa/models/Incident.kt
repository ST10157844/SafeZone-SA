package com.example.safezonesa.models

import com.google.gson.annotations.SerializedName

data class Incident(
    @SerializedName("incidentID")  val incidentID: Int? = null,
    @SerializedName("type")        val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("location")    val location: String? = null,
    @SerializedName("status")      val status: String? = "Under Review",
    @SerializedName("time")        val time: String? = null
)
