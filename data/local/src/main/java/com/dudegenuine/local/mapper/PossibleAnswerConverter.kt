package com.dudegenuine.local.mapper

import androidx.room.TypeConverter
import com.dudegenuine.model.Quiz
import com.dudegenuine.model.common.ImageUtil.strOf
import com.google.gson.Gson

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
object PossibleAnswerConverter {
    val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String?): Quiz.Answer.Possible? {
        val possibility: Quiz.Answer.Exact = Gson().fromJson(data, Quiz.Answer.Exact::class.java)

        //Log.d(TAG, "fromJson: answer ${possibility.type}")

        return when(possibility.type){
            strOf<Quiz.Answer.Possible.SingleChoice>() ->
                Quiz.Answer.Possible.SingleChoice(possibility.answer ?: "")

            strOf<Quiz.Answer.Possible.MultipleChoice>() ->
                Quiz.Answer.Possible.MultipleChoice(possibility.answers ?: emptySet())

            else -> null
        }

        /*val type = object: TypeToken<PossibleAnswer?>(){}.type
        return Gson().fromJson(data, type)*/
    }
    @TypeConverter
    fun toJson(data: Quiz.Answer.Possible?): String? {
        return Gson().toJson(data)
    }
}