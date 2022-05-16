package org.techtown.finalproject

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
        val sharedPreferences = getSharedPreferences("sFile1",MODE_PRIVATE)
        var tokenNew = sharedPreferences.getString("Token1","null")
        Log.d(TAG, "토큰토큰 new: ${tokenNew} ")
        //getFCMToken()
        Log.d(TAG, "onCreate: called")
        db = UserDb.getInstance(applicationContext)!!
        login.setOnClickListener {
            getFCMToken()
            if (checked.isChecked) {
                Log.d(TAG, "login-data is saved")
                val data = User(user.text.toString(), password.text.toString())
                Log.d(TAG, "${data.passWord}, ${data.userNumber}")

            }
            api.getTask(user.text.toString(), password.text.toString(),tokenNew!!)
            val intent = Intent(this, TaskViewWithCal::class.java)
            startActivity(intent)
        }
    }
    fun getFCMToken(){
        var token: String = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result.toString()
            val sharedPreferences = getSharedPreferences("sFile1",MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            // key, value를 이용하여 저장하는 형태 editor.commit();
            editor.putString("Token1",token)
            editor.commit()
            // Log and toast
            Log.d(TAG, "FCM Token is ${token} size : ${token.length}")
        })
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
        user.text.clear()
        password.text.clear()
        api.data.value!!.clear()
    }
}