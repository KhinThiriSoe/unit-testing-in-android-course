package com.techyourchance.unittesting.networking.questions

import com.google.gson.annotations.SerializedName

data class QuestionSchema(
    @field:SerializedName("title") val title: String,
    @field:SerializedName("question_id") val id: String,
    @field:SerializedName("body") val body: String
)