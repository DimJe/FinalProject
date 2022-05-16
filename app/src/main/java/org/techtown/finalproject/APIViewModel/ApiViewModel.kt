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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource


class ApiViewModel : ViewModel() {

    var retrofit : Retrofit
    var data : MutableLiveData<ArrayList<Taskinfo>> = MutableLiveData()
    var temp = ArrayList<Taskinfo>()
    var temp2 = ArrayList<Taskinfo>()
    init {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(50,TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.17.17:8001/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @OptIn(ExperimentalTime::class)
    fun getTask(id:String, pw:String,token:String){
        var mark = TimeSource.Monotonic.markNow()
        val api = retrofit.create(GetTaskData::class.java)
        Log.d(TAG, "getTask: called")
        val result = api.sendData(lmsItem(id,pw,token))
        result.enqueue(object : Callback<APIdata>{
            override fun onResponse(call: Call<APIdata>, response: Response<APIdata>) {
                Log.d(TAG,"CallAPI - onResponse() called")
                temp.clear()
                temp2.clear()
                if(response.isSuccessful){
                    response.body()!!.task.forEach{
                        temp.add(Taskinfo(it.d_day_start,it.d_day_end,it.title,it.course,it.content))
                    }
                    if(temp.isEmpty()){
                        temp.add(Taskinfo("6666-66-66","6666-66-66","","",""))
                    }
                    temp.forEach {
                        Log.d(TAG, "onResponse: ${it.taskName}")
                        if (it.startMonth != it.endMonth){
                            val cal = Calendar.getInstance()
                            cal.time = Date()
                            cal.set(Calendar.MONTH,it.startMonth-1)
                            val end : Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                            val str : String = it.startYear.toString() + if(it.startMonth+1>9) "-"+(it.startMonth+1).toString() else "-0"+(it.startMonth+1).toString() + "-" + "01"
                            val str2 : String = it.startYear.toString() + if(it.endMonth>9) "-" else "-0"+(it.endMonth).toString() + if(it.endDay>9) "-"+it.endDay.toString() else "-0" + (it.endDay).toString()
                            Log.d(TAG, "$str  $str2")
                            temp2.add(Taskinfo(str,str2,it.taskName,it.course,it.content))
                            it.endDay = end
                            it.endMonth = it.startMonth
                        }
                    }
                    temp.addAll(temp2)
                    Log.d(TAG, "onResponse: ${mark.elapsedNow()}")
                    data.value = temp
                }
                else{
                    Log.d(TAG, "onResponse: ${response.code()}")
                    temp.add(Taskinfo("9999-99-99","9999-99-99","","",""))
                    data.value = temp
                }

            }
            override fun onFailure(call: Call<APIdata>, t: Throwable) {
                Log.d(TAG,"CallAPI - onFailure() called ${t.localizedMessage}")
            }
        })
    }
}