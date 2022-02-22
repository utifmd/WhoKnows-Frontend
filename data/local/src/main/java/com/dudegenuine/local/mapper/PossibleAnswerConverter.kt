package com.dudegenuine.local.mapper

import android.util.Log
import androidx.room.TypeConverter
import com.dudegenuine.model.Answer
import com.dudegenuine.model.PossibleAnswer
import com.dudegenuine.model.common.ImageUtil.strOf
import com.google.gson.Gson

/**
 * Mon, 21 Feb 2022
 * WhoKnows by utifmd
 **/
object PossibleAnswerConverter {
    val TAG = javaClass.simpleName

    @TypeConverter
    fun fromJson(data: String?): PossibleAnswer? {
        val possibility: Answer = Gson().fromJson(data, Answer::class.java)

        Log.d(TAG, "fromJson: answer ${possibility.type}")

        return when(possibility.type){
            strOf<PossibleAnswer.SingleChoice>() ->
                PossibleAnswer.SingleChoice(possibility.answer ?: "")

            strOf<PossibleAnswer.MultipleChoice>() ->
                PossibleAnswer.MultipleChoice(possibility.answers ?: emptySet())

            else -> null
        }

        /*val type = object: TypeToken<PossibleAnswer?>(){}.type
        return Gson().fromJson(data, type)*/
    }
    @TypeConverter
    fun toJson(data: PossibleAnswer?): String? {
        return Gson().toJson(data)
    }
}