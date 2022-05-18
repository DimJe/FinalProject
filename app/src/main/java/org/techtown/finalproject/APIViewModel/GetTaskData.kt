package org.techtown.finalproject.APIViewModel

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.security.PrivateKey
import java.security.PublicKey

interface GetTaskData {
    @POST("api/lms-items/")
    fun sendData(@Body lmsItem : lmsItem): Call<APIdata>

    @GET("api/lms-items/")
    fun getDate() : Call<String>
}