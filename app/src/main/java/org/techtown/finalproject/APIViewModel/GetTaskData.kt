package org.techtown.finalproject.APIViewModel

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GetTaskData {
    @POST("api/lms-items/")
    fun sendData(@Body lmsItem : lmsItem

    ): Call<String>
}