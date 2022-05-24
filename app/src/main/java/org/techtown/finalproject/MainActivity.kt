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
import com.google.android.gms.common.util.Base64Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ScheduleItem
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object{
        val api = ApiViewModel()
        val TAG: String = "로그"
        val lineColor = arrayOfNulls<Int>(6)
        val scheduleList = arrayOfNulls<TextView>(6)
        var dayTask = Array<ArrayList<Taskinfo>>(42){ArrayList<Taskinfo>()}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")
        val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var tokenNew : String? = ""
        Log.d(TAG, "토큰이 저장 됨?:  ${tokenNew}")
        if(sharedPreferences.getBoolean("auto",false)){
            val id = sharedPreferences.getString("id","null")
            val pw = sharedPreferences.getString("pw","null")
            tokenNew = sharedPreferences.getString("token","null")
            api.getTask(id!!,pw!!,tokenNew!!)
            val intent = Intent(this, TaskViewWithCal::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            if(user.text.toString()=="" || password.text.toString()==""){
                Log.d(TAG, "onCreate: login click1")
                Toast.makeText(this, "학번과 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                Log.d(TAG, "onCreate: login click2")
                Log.d(TAG, "onCreate: ${password.text.toString()}")
                if (checked.isChecked) {
                    Log.d(TAG, "login-data is saved")
                    editor.putBoolean("auto",true)
                    editor.putString("id",user.text.toString())
                    editor.putString("pw",password.text.toString())
                    editor.commit()
                }
                else{
                    editor.putBoolean("auto",false)
                    editor.apply()
                    editor.commit()
                    Log.d(TAG, "shared: ${sharedPreferences.getBoolean("auto",true)}")
                }
                tokenNew = sharedPreferences.getString("token","null")
                api.getTask(user.text.toString(), password.text.toString(),tokenNew!!)
                val intent = Intent(this, TaskViewWithCal::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
        user.text.clear()
        password.text.clear()
        if(api.data.hasObservers()) {
            api.data.value!!.clear()
        }
        dayTask.forEach {
            it.clear()
        }
    }

}