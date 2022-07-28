# 대학교 1학기 졸업 프로젝트
 - 팀 프로젝트
## 기능
 - 어플에서 입력 받은 로그인 정보로 학교 사이트를 크롤링 하여 과제를 어플로 확인 할 수 있는 앱
 - 캘린더 및 리스트로 확인 가능하며 신규 과제가 생길 시 푸시알림 전송 기능
## 사용한 기술 및 라이브러리
 - Retrofit
 - liveData
 - RecyclerView
 - firebase
 - SharedPreference
## 작동화면
<figure class="first" align = "center">
<img src="https://user-images.githubusercontent.com/41900899/181429118-a4b13c3d-adaf-46fd-96af-20a6919e38e1.gif" width = "30%"/>
</figure>

## 구현
 -  ### 1.MainActivity
    <figure class="first" align = "center"> 
    <img src="https://user-images.githubusercontent.com/41900899/181429639-58490e55-e2a4-4a84-b972-6d1a2df15c90.png" width = "30%"/>
    </figure>

    
    초기 로그인 화면이다. Activity.<br>
    먼저 자동로그인 상태를 SharedPreference에서 확인하고 아니라면
    간단하게 학생 학번과 패스워드를 입력하고 로그인 버튼을 클릭하면 로그인 정보가 서버로 전송되고 서버에서 로그인 및 크롤링을 진행한다.
    새로운 과제가 등록된다면 PUSH 알림을 보내야 하기때문에 토큰값도 함께 서버에 전송한다.
    로그인정보 기억하기 체크박스를 체크하고 로그인을 진행한다면 SharedPreference를 통해서 자동로그인 여부와 아이디 패스워드를 저장하고 그 다음부터 어플을 실행 하면 SharedPreference에서 아이디와 패스워드를 얻어서 로그인을 진행한다.
    <details>
    <summary>MainActivity 코드 보기</summary>
    <div markdown = "1">

    ```java
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
            val sharedPreferences = getSharedPreferences("token",MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            var tokenNew: String?
            if(sharedPreferences.getBoolean("auto",false)){
                val id = sharedPreferences.getString("id","null")
                val pw = sharedPreferences.getString("pw","null")
                tokenNew = sharedPreferences.getString("token","null")
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
    ```
    </div>
    </details>
  - ### 2.CalendarView
    <figure class="first" align = "center">
    <img src="https://user-images.githubusercontent.com/41900899/181429645-b2c2aa7b-3124-40bf-9af8-73dcfb4778c2.png" width = "30%"/>
    </figure>

    <br>로그인을 성공하고 과제정보를 서버로부터 얻어왔다면 그려지는 캘린더 Activity.<br>
    기본적으로 RecyclerView를 통해 캘린더를 구현하는 방법은 구글링을 통해 능력있는 귀인분이 올려주신 코드를 참고했고 RecyclerView의 itemView에 6개의 textView를 추가하여 과제들의 일정을 표시했다.
    Adapter 클래스의 onBindViewHolder()메소드에서 과제정보 리스트를 순회하며 시작날짜부터 종료날짜를 확인해 일정을 표시한다.
    <details>
    <summary>Adapter 코드 보기</summary>
    <div markdown = "1">

    ```java
        class RecyclerViewAdapter(val mainActivity: TaskViewWithCal, var taskList: ArrayList<Taskinfo>) : RecyclerView.Adapter<ViewHolderHelper>() {

        val baseCalendar = BaseCalendar()
        val schedule = MutableList<ScheduleItem>(6, init = {ScheduleItem(false,"","","",null)})
        init {
            baseCalendar.initBaseCalendar {
                refreshView(it)
            }
            repeat(dayTask.size) {
                ArrayList<Taskinfo>()
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_day, parent, false)
            return ViewHolderHelper(view)
        }

        override fun getItemCount(): Int {
            return BaseCalendar.LOW_OF_CALENDAR * BaseCalendar.DAYS_OF_WEEK
        }

        override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {
            scheduleList[0] = holder.itemView.one
            scheduleList[1] = holder.itemView.two
            scheduleList[2] = holder.itemView.three
            scheduleList[3] = holder.itemView.four
            scheduleList[4] = holder.itemView.five
            scheduleList[5] = holder.itemView.six

            scheduleList.forEach {
                it!!.visibility = View.INVISIBLE
            }

            if(baseCalendar.nowMonth==baseCalendar.month && baseCalendar.nowDate==baseCalendar.data[position]){
                holder.itemView.setBackgroundResource(R.drawable.round)
            }
            
            val dayRange : String = baseCalendar.month.toString()+
                    if(baseCalendar.data[position].toString().length==1) "0"+ baseCalendar.data[position].toString() else baseCalendar.data[position].toString()

            when {
                position % BaseCalendar.DAYS_OF_WEEK == 0 -> holder.itemView.item_day_text.setTextColor(Color.parseColor("#ff1200"))
                position % BaseCalendar.DAYS_OF_WEEK == 6 -> holder.itemView.item_day_text.setTextColor(Color.BLUE)
                else -> holder.itemView.item_day_text.setTextColor(Color.parseColor("#676d6e"))
            }

            if (position < baseCalendar.prevMonthTailOffset || position >= baseCalendar.prevMonthTailOffset + baseCalendar.currentMonthMaxDate) {
                //holder.itemView.item_day_text.alpha = 0.3f
                holder.itemView.item_day_layout.visibility = View.INVISIBLE
            } else {
                holder.itemView.item_day_layout.visibility = View.VISIBLE
                holder.itemView.item_day_text.alpha = 1f
            }
            holder.itemView.item_day_text.text = baseCalendar.data[position].toString()

            taskList.forEach {
                if(it.startMonth-1==baseCalendar.month && it.startDay==baseCalendar.data[position]){
                    Log.i("태그", "과제 매칭")
                    for(i in 0..5){
                        if(!schedule[i].check){
                            schedule[i].check = true
                            schedule[i].startRange = (it.startMonth-1).toString() +
                                    if(it.startDay.toString().length==1) "0"+it.startDay.toString() else it.startDay
                            schedule[i].endRange = (it.endMonth-1).toString() +
                                    if(it.endDay.toString().length==1) "0"+it.endDay.toString() else it.endDay
                            it.taskLine = i
                            schedule[i].title = it.taskName
                            schedule[i].item = it
                            break
                        }
                    }
                }
            }
            for(i in 0..5){
                if(schedule[i].check && (schedule[i].startRange <= dayRange) && (dayRange <= schedule[i].endRange)){
                    dayTask[position].add(schedule[i].item!!)
                    scheduleList[i]!!.visibility = View.VISIBLE
                    if(schedule[i].title.length>8) {
                        scheduleList[i]!!.text = schedule[i].title.substring(0 until 8)
                        schedule[i].title = schedule[i].title.substring(8)
                    }
                    else {
                        scheduleList[i]!!.text = schedule[i].title.substring(0)
                        schedule[i].title = ""
                    }
                }
            }
            taskList.forEach {
                if (it.endMonth-1==baseCalendar.month && it.endDay==baseCalendar.data[position]){
                    schedule[it.taskLine].check = false
                    schedule[it.taskLine].startRange = ""
                    schedule[it.taskLine].endRange = ""
                    schedule[it.taskLine].title = ""
                    schedule[it.taskLine].item = null
                }
            }
            holder.itemView.setOnClickListener{
                Intent(mainActivity,TaskViewWithList::class.java).apply {
                    putExtra("data", dayTask[position])
                    putExtra("month",(baseCalendar.month+1).toString())
                    putExtra("day",baseCalendar.data[position].toString())
                }.run { mainActivity.startActivity(this) }

            }

        }

        fun changeToPrevMonth() {
            dayTask.forEach {
                it.clear()
            }
            schedule.forEach {
                it.check = false
                it.startRange = ""
                it.endRange = ""
            }
            baseCalendar.changeToPrevMonth {
                refreshView(it)
            }
        }

        fun changeToNextMonth() {
            dayTask.forEach {
                it.clear()
            }
            schedule.forEach {
                it.check = false
                it.startRange = ""
                it.endRange = ""
            }
            baseCalendar.changeToNextMonth {
                refreshView(it)
            }
        }

        private fun refreshView(calendar: Calendar) {
            notifyDataSetChanged()
            mainActivity.refreshCurrentMonth(calendar)
        }
    }
    ```
    </div>
    </details>
    
  - ### 3.ListActivty
    <figure class="first" align = "center">
    <img src="https://user-images.githubusercontent.com/41900899/181429647-bd93d0f9-fb42-4f4f-ae07-73f3c92ecd68.png" width = "30%"/>
    </figure>
    <br>캘린더에서 날짜를 클릭하면 그 날짜에 존재하는 과제들의 조금 더 상세한 정보를 볼 수 있는 리스트 뷰이다.<br>
    리스트는 마감일자 기준으로 정렬되었으며 현재일 기준 마감일자가 가까울 수록 리스트 아이템의 배경색을 다르게 설정했다.
    <details>
    <summary>리스트 뷰 Adapter 코드 보기</summary>
    <div markdown = "1">

    ```java
            class ListViewAdapter(private val todayList: ArrayList<Taskinfo>, val context:Context): RecyclerView.Adapter<ViewHolderHelper>()  {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHelper {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_task, parent, false)
            return ViewHolderHelper(view)
        }

        override fun onBindViewHolder(holder: ViewHolderHelper, position: Int) {

            val date = Calendar.getInstance()
            date.time = Date()
            if(date.time.month+1==todayList[position].endMonth){
                if(todayList[position].endDay-date.time.date<=3){
                    holder.itemView.list_item_task.setBackgroundColor(Color.argb(50,255,0,0))
                }
                else if(todayList[position].endDay-date.time.date<=5){
                    holder.itemView.list_item_task.setBackgroundColor(Color.argb(50,255,235,59))
                }
            }
            holder.itemView.body.animation = AnimationUtils.loadAnimation(context,R.anim.itemanim)
            holder.itemView.courseName.text = todayList[position].course
            holder.itemView.professor.text = todayList[position].professor
            val str = todayList[position].startMonth.toString() +"-"+todayList[position].startDay + " ~ " +todayList[position].endMonth +"-"+todayList[position].endDay
            holder.itemView.deadLine.text = str
            holder.itemView.taskName.text = todayList[position].taskName
            holder.itemView.taskContent.text = todayList[position].content
            holder.itemView.showMore.setOnClickListener {
                if(holder.itemView.showMore.tag=="0"){
                    holder.itemView.showMore.tag = "1"
                    holder.itemView.taskContent.visibility = View.VISIBLE
                    holder.itemView.showMore.setImageResource(R.drawable.less)
                }else if(holder.itemView.showMore.tag=="1"){
                    holder.itemView.showMore.tag="0"
                    holder.itemView.taskContent.visibility = View.GONE
                    holder.itemView.showMore.setImageResource(R.drawable.more)
                }
            }
        }

        override fun getItemCount(): Int {
            return todayList.size
        }
    }
    ```

    </div>
    </details>

  - ### 4.API 호출
    API 요청에 대한 응답으로는 정상적으로 로그인 하여 과제를 얻어온 경우도 있지만, 로그인 실패, 로그인은 성공했지만 과제가 없는 경우도 있다.<br> 정상적인 경우를 제외하고 나머지 경우에 대해 임의로 livedata에 다른 값을 넣어서 observe()에서 처리하게끔 구현했다.
    
    <details>
    <summary>observe() 코드 보기</summary>
    <div markdown = "1">

    ```java
    api.data.observe(this, {
            Log.d(TAG, "observe : called ")
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            when {
                api.data.value!!.isEmpty() -> {
                }
                it[0].startMonth==66 -> {
                    dialog.dismiss()
                    Toast.makeText(this,"과제가 없습니다.",Toast.LENGTH_SHORT).show()
                    initView(api.data.value!!)
                    api.data.value!!.clear()
                }
                it[0].startMonth==99 -> {
                    dialog.dismiss()
                    Toast.makeText(this, "학번과 패스워드가 잘못되었습니다", Toast.LENGTH_SHORT).show()
                    api.data.value!!.clear()
                    finish()
                }
                it[0].startMonth==11 -> {
                    dialog.dismiss()
                    Toast.makeText(this, "서버가 꺼졌누....", Toast.LENGTH_SHORT).show()
                    api.data.value!!.clear()
                    finish()
                }
                else -> {
                    dialog.dismiss()
                    initView(api.data.value!!)
                }
            }

        })
    ```
    </div>
    </details>
    <br>
  - ### 5.Firebase PUSH 알림
    PUSH 알림은 서버에서 사용자들의 과제 정보를 업데이트 하면서 새로운 과제가 생기면 해당 사용자의 토큰값으로 PUSH 알림을 전송한다. 안드로이드에선 onMessageReceived() 와 sendNotification()을 통해 처리했다.
    
## 개선점 및 느낀점
- 캘린더에 일정을 표시하는 것이 가장 어려웠고 오래걸렸다. 네이버 캘린더나 갤럭시 기본 캘린더 앱처럼 표시하고 싶어서 다른 캘린더 오픈소스를 찾아보다가 찾지 못하고 RecyclerView를 통해 직접 구현했다. onBindViewHolder()가 호출될 때마다 과제 정보들을 순회하기때문에 상당히 비효율적이고 자세히 본다면 텍스트 표시도 어색하다. 기간내에 프로젝트를 완성해야 했기에 어쩔 수 없었다. 나중에 실력이 향상되고 나서 캘린더를 다룰 일이 생긴다면 그땐 이번 프로젝트처럼 구현하고 싶지 않다.
