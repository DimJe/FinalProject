package org.techtown.finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.APIViewModel.Taskinfo
import org.techtown.finalproject.Calendar.ScheduleItem
import java.util.*
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
                Toast.makeText(this, "학번과 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
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
                }
                tokenNew = sharedPreferences.getString("token","null")
                api.getTask(user.text.toString(), password.text.toString(),tokenNew!!)
                val intent = Intent(this, TaskViewWithCal::class.java)
                startActivity(intent)
            }
        }
        main.setOnClickListener {
            if(currentFocus != null) HideKeyBoard()
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
    fun HideKeyBoard(){
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

}