package org.techtown.finalproject.APIViewModel

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface GetTaskData {
    @POST("api/lms-items/")
    fun sendData(@Body lmsItem : lmsItem): Call<APIdata>

}