package org.techtown.finalproject.APIViewModel

import com.google.gson.annotations.SerializedName

data class APIdata(
    val task: List<tasks>
)
data class tasks(
    val course : String,
    val title: String,
    val d_day_start : String,
    val d_day_end : String,
    val clear: String,
    val content : String
)