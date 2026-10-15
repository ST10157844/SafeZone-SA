package com.example.safezonesa.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class IncidentValidatorTest {

    @Test
    fun validIncident_returnsNoError() {

        val result =
            IncidentValidator.validate(
                incidentTypePosition = 1,
                location = "Test Community Park",
                description =
                    "A suspicious incident was observed near the entrance."
            )

        assertNull(result)
    }


    @Test
    fun noIncidentType_returnsError() {

        val result =
            IncidentValidator.validate(
                incidentTypePosition = 0,
                location = "Pretoria",
                description =
                    "A valid description of the incident."
            )

        assertEquals(
            "Please select an incident type.",
            result
        )
    }


    @Test
    fun emptyLocation_returnsError() {

        val result =
            IncidentValidator.validate(
                incidentTypePosition = 1,
                location = "",
                description =
                    "A valid incident description."
            )

        assertEquals(
            "Location is required",
            result
        )
    }


    @Test
    fun shortDescription_returnsError() {

        val result =
            IncidentValidator.validate(
                incidentTypePosition = 1,
                location = "Pretoria",
                description = "Short"
            )

        assertEquals(
            "Description must contain at least 10 characters",
            result
        )
    }


    @Test
    fun robbery_returnsCriticalPriority() {

        assertEquals(
            "critical",
            IncidentValidator.determinePriority(
                "Robbery"
            )
        )
    }


    @Test
    fun burglary_returnsHighPriority() {

        assertEquals(
            "high",
            IncidentValidator.determinePriority(
                "Burglary"
            )
        )
    }


    @Test
    fun theft_returnsMediumPriority() {

        assertEquals(
            "medium",
            IncidentValidator.determinePriority(
                "Theft"
            )
        )
    }


    @Test
    fun streetlightFailure_returnsLowPriority() {

        assertEquals(
            "low",
            IncidentValidator.determinePriority(
                "Streetlight Failure"
            )
        )
    }
}