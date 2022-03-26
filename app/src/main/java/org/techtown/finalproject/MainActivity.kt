package org.techtown.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.Room.User
import org.techtown.finalproject.Room.UserDb

class MainActivity : AppCompatActivity() {

    companion object{
        val api = ApiViewModel()
        val TAG: String = "로그"
        lateinit var db : UserDb
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = UserDb.getInstance(applicationContext)!!

        login.setOnClickListener {
            if(checked.isChecked){
                Log.d(TAG, "login-data is saved")
                db.userDao().insert(User(user.text.toString(),password.text.toString()))
            }
            Log.d(TAG, "user : ${db.userDao().get().userNumber}, ${db.userDao().check()}")

            Toast.makeText(this, "test-text", Toast.LENGTH_SHORT).show()
        }
    }
}