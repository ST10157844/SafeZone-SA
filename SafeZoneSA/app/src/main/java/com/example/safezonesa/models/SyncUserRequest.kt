package com.example.safezonesa.models

data class SyncUserRequest(
    val firebaseUid: String,
    val name: String,
    val email: String,
    val phone: String?
)