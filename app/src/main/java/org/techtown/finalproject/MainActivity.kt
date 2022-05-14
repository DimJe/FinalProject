package org.techtown.finalproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ScheduleItem
import org.techtown.finalproject.Room.User
import org.techtown.finalproject.Room.UserDb

class MainActivity : AppCompatActivity() {

    companion object{
        val api = ApiViewModel()
        val TAG: String = "로그"
        lateinit var db : UserDb
        val lineColor = arrayOfNulls<Int>(6)
        val scheduleList = arrayOfNulls<TextView>(6)
        var dayTask = Array<ArrayList<Taskinfo>>(42){ArrayList<Taskinfo>()}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")
        db = UserDb.getInstance(applicationContext)!!
        var token : String = ""
        login.setOnClickListener {
            token = "cMv4gx_4SOe1lWq-zq5BWH:APA91bGVcvcwCKiPav6JuGoddKFmRmkMVryc10YZ9Z1XZeKO3KzW4OOj8jvIB-5Lwhpz2jkfm3Il4lNOda9p955MdPY2o0q9QdcvARhrQUfmMrlC9lcQaRl8Zhukzo7EkwWwU3BUHsXA"
            if (checked.isChecked) {
                Log.d(TAG, "login-data is saved")
                val data = User(user.text.toString(), password.text.toString())
                Log.d(TAG, "${data.passWord}, ${data.userNumber}")

            }
            Log.d(TAG, "토큰토큰: $token")
            api.getTask(user.text.toString(), password.text.toString(),token)
            val intent = Intent(this, TaskViewWithCal::class.java)
            startActivity(intent)
        }
    //                CoroutineScope(Dispatchers.IO).launch {
//                    //db!!.userDao().insert(data)
//                    Log.d(TAG, "data-save1")
//                }
//                Log.d(TAG, "data-save2")
//            CoroutineScope(Dispatchers.Main).launch {
//                val data = CoroutineScope(Dispatchers.IO).async {
//                    db!!.userDao().get()
//                }.await()
//                CoroutineScope(Dispatchers.IO).launch {
//                    db!!.userDao().delete()
//                }
//                data.forEach {
//                    Log.d(TAG, "onCreate: ${it.userNumber} , ${it.passWord}")
//                }
//            }
    }
    fun getFCMToken(): String{
        var token: String = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result.toString()
            //return@OnCompleteListener
            // Log and toast
            Log.d(TAG, "FCM Token is ${token} size : ${token.length}")
        })
        Log.d(TAG, "getFCMToken: ${token}")
        return token
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
        user.text.clear()
        password.text.clear()
    }
}