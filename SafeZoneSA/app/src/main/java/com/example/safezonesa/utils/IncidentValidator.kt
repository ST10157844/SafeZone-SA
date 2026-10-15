package com.example.safezonesa.utils

object IncidentValidator {

    fun validate(
        incidentTypePosition: Int,
        location: String,
        description: String
    ): String? {

        if (incidentTypePosition == 0) {
            return "Please select an incident type."
        }

        if (location.isBlank()) {
            return "Location is required"
        }

        if (description.isBlank()) {
            return "Description is required"
        }

        if (description.length < 10) {
            return "Description must contain at least 10 characters"
        }

        if (description.length > 500) {
            return "Description cannot exceed 500 characters"
        }

        return null
    }


    fun determinePriority(
        type: String
    ): String {

        return when (type) {

            "Assault",
            "Robbery" ->
                "critical"

            "Burglary",
            "Vehicle Break-in" ->
                "high"

            "Theft",
            "Suspicious Activity",
            "Vandalism" ->
                "medium"

            else ->
                "low"
        }
    }
}