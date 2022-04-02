package org.techtown.finalproject.APIViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import org.techtown.finalproject.MainActivity.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiViewModel : ViewModel() {

    var retrofit : Retrofit
    init {
        retrofit = Retrofit.Builder()
            .baseUrl("http://54.221.144.212:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getTask(id:String,pw:String){
        val api = retrofit.create(GetTaskData::class.java)

        val result = api.sendData(lmsItem(id,pw))
        result.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG,"CallAPI - onResponse() called")
                Log.d(TAG, "onResponse: ${response.body()}")

            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG,"CallAPI - onFailure() called ${t.localizedMessage}")
            }
        })
    }
}