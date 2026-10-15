package com.example.safezonesa.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.safezonesa.models.SyncUserRequest
import com.example.safezonesa.models.SyncUserResponse
import com.google.firebase.auth.FirebaseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserSyncManager {

    fun syncFirebaseUser(
        context: Context,
        user: FirebaseUser,
        phone: String? = null,
        onComplete: (Boolean) -> Unit
    ) {

        val request = SyncUserRequest(
            firebaseUid = user.uid,
            name = user.displayName ?: "SafeZone User",
            email = user.email ?: "",
            phone = phone
        )

        if (request.email.isBlank()) {
            onComplete(false)
            return
        }

        RetrofitClient.api
            .syncUser(request)
            .enqueue(
                object : Callback<SyncUserResponse> {

                    override fun onResponse(
                        call: Call<SyncUserResponse>,
                        response: Response<SyncUserResponse>
                    ) {

                        if (
                            response.isSuccessful &&
                            response.body()?.success == true
                        ) {

                            Log.d(
                                "SafeZoneSync",
                                "Firebase user synchronized with MySQL"
                            )

                            onComplete(true)

                        } else {

                            Log.e(
                                "SafeZoneSync",
                                "User sync failed: ${response.code()}"
                            )

                            Toast.makeText(
                                context,
                                "Account signed in, but profile synchronization failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                            onComplete(false)
                        }
                    }

                    override fun onFailure(
                        call: Call<SyncUserResponse>,
                        t: Throwable
                    ) {

                        Log.e(
                            "SafeZoneSync",
                            "Synchronization error: ${t.message}",
                            t
                        )

                        Toast.makeText(
                            context,
                            "Unable to synchronize SafeZone profile.",
                            Toast.LENGTH_SHORT
                        ).show()

                        onComplete(false)
                    }
                }
            )
    }
}