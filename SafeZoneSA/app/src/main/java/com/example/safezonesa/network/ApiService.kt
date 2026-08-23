package com.example.safezonesa.network

import com.example.safezonesa.models.Incident
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("get_incidents.php")
    fun getIncidents(): Call<List<Incident>>

    @POST("post_incident.php")
    fun submitIncident(@Body incident: Incident): Call<ResponseBody>
}
