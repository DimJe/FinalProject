package org.techtown.finalproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.Calendar.ScheduleItem
import org.techtown.finalproject.Room.User
import org.techtown.finalproject.Room.UserDb

class MainActivity : AppCompatActivity() {

    companion object{
        val api = ApiViewModel()
        val TAG: String = "로그"
        lateinit var db : UserDb
        val schedule = MutableList<ScheduleItem>(6, init = {ScheduleItem(false,"","")})
        val lineColor = arrayOfNulls<Int>(6)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: called")
        db = UserDb.getInstance(applicationContext)!!

        login.setOnClickListener {
            if(checked.isChecked){
                Log.d(TAG, "login-data is saved")
                val data = User(user.text.toString(),password.text.toString())
                Log.d(TAG, "${data.passWord}, ${data.userNumber}")
//                CoroutineScope(Dispatchers.IO).launch {
//                    //db!!.userDao().insert(data)
//                    Log.d(TAG, "data-save1")
//                }
//                Log.d(TAG, "data-save2")
            }
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
            api.getTask(user.text.toString(),password.text.toString())
            Toast.makeText(this, "test-text", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,TaskViewWithCal::class.java)
            startActivity(intent)
            finish()
        }
    }
}