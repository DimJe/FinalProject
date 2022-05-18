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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ScheduleItem
import org.techtown.finalproject.Room.User
import org.techtown.finalproject.Room.UserDb
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
        lateinit var db : UserDb
        var keyString : String = ""
        val lineColor = arrayOfNulls<Int>(6)
        val scheduleList = arrayOfNulls<TextView>(6)
        var dayTask = Array<ArrayList<Taskinfo>>(42){ArrayList<Taskinfo>()}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")
        val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
        val key = sharedPreferences.getString("key","null")
        if(key.equals("null")){
            api.getKey()
            val editor = sharedPreferences.edit()
            editor.putString("key", keyString)
            editor.commit()
        }
        login.setOnClickListener {
            Log.d(TAG, "onCreate: login click")
            if (checked.isChecked) {
                Log.d(TAG, "login-data is saved")
                val data = User(user.text.toString(), password.text.toString())
                Log.d(TAG, "${data.passWord}, ${data.userNumber}")

            }
            var tokenNew = sharedPreferences.getString("token","null")
            val str = sharedPreferences.getString("key","null")
            val ukeySpec = X509EncodedKeySpec(Base64.getDecoder().decode(str!!.toByteArray()))
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKey = keyFactory.generatePublic(ukeySpec)
            val cipher = Cipher.getInstance("RSA")
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val encrypt = cipher.doFinal(password.text.toString().toByteArray())
            Log.d(TAG, "onResponse: ${Base64Utils.encode(encrypt)}")
            api.getTask(user.text.toString(), Base64Utils.encode(encrypt),tokenNew!!)
            val intent = Intent(this, TaskViewWithCal::class.java)
            startActivity(intent)
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
        user.text.clear()
        password.text.clear()
        api.data.value!!.clear()
    }

}