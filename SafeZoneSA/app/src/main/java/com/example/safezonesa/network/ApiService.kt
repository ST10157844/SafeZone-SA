package com.example.safezonesa.network

import com.example.safezonesa.models.IncidentResponse
import com.example.safezonesa.models.SubmitIncidentRequest
import com.example.safezonesa.models.SubmitIncidentResponse
import com.example.safezonesa.models.SyncUserRequest
import com.example.safezonesa.models.SyncUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("get_incidents.php")
    fun getIncidents(): Call<List<IncidentResponse>>

    @POST("sync_user.php")
    fun syncUser(
        @Body request: SyncUserRequest
    ): Call<SyncUserResponse>

    @POST("post_incident.php")
    fun submitIncident(
        @Body request: SubmitIncidentRequest
    ): Call<SubmitIncidentResponse>

    @GET("get_my_incidents.php")
    fun getMyIncidents(
        @Query("firebaseUid") firebaseUid: String
    ): Call<List<IncidentResponse>>
}