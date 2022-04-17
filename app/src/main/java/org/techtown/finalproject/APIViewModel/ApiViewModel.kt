package org.techtown.finalproject.APIViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import org.techtown.finalproject.MainActivity.Companion.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


class ApiViewModel : ViewModel() {

    var retrofit : Retrofit
    var data : MutableLiveData<ArrayList<Taskinfo>> = MutableLiveData()
    var temp = ArrayList<Taskinfo>()
    init {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(80, TimeUnit.SECONDS)
            .readTimeout(80, TimeUnit.SECONDS)
            .writeTimeout(80, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("http://3.85.10.166:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @OptIn(ExperimentalTime::class)
    fun getTask(id:String, pw:String){
        var mark = TimeSource.Monotonic.markNow()
        val api = retrofit.create(GetTaskData::class.java)
        Log.d(TAG, "getTask: called")
        val result = api.sendData(lmsItem(id,pw))
        result.enqueue(object : Callback<APIdata>{
            override fun onResponse(call: Call<APIdata>, response: Response<APIdata>) {
                Log.d(TAG,"CallAPI - onResponse() called")
                temp.clear()
                response.body()!!.task.forEach{
                    temp.add(Taskinfo(it.d_day_start,it.d_day_end,it.title,it.course,it.content))
                }
                temp.forEach {
                    Log.d(TAG, "onResponse: ${it.startDay}, ${it.startMonth}, ${it.endDay}, ${it.endMonth}")
                }
                Log.d(TAG, "onResponse: ${mark.elapsedNow()}")
                data.value = temp

            }
            override fun onFailure(call: Call<APIdata>, t: Throwable) {
                Log.d(TAG,"CallAPI - onFailure() called ${t.localizedMessage}")
            }
        })
    }
}