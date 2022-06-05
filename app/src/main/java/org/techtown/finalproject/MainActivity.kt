package org.techtown.finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.techtown.finalproject.APIViewModel.ApiViewModel
import org.techtown.finalproject.APIViewModel.Taskinfo
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object{
        val api = ApiViewModel()
        const val TAG: String = "로그"
        val scheduleList = arrayOfNulls<TextView>(6)
        var dayTask = Array(42){ArrayList<Taskinfo>()}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: called")
        val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        var tokenNew: String?
        if(sharedPreferences.getBoolean("auto",false)){
            val id = sharedPreferences.getString("id","null")
            Log.d(TAG, "id: ${id}")
            val pw = sharedPreferences.getString("pw","null")
            Log.d(TAG, "pw: ${pw}")
            tokenNew = sharedPreferences.getString("token","null")
            Log.d(TAG, "token: ${tokenNew}")
            api.getTask(id.toString(),pw.toString(), tokenNew!!)
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
                    editor.apply()
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
            if(currentFocus != null) hideKeyBoard()
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: called")
        user.text.clear()
        password.text.clear()
        dayTask.forEach {
            it.clear()
        }
    }
    private fun hideKeyBoard(){
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

}