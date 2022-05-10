/*
 * *
 *  * Created by Ali Ashkeyev on 08.05.2022, 2:27
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 08.05.2022, 2:27
 *
 */

package com.example.budka.data.localDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromArray(strings: List<String> ): String {
        var string = "";
        for(s in strings) string += ("$s,");

        return Gson().toJson(strings)
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String): List<String> {
        val myStrings = arrayListOf<String>()

        for(s in concatenatedStrings.split(','))myStrings.add(s)
        return Gson().fromJson(concatenatedStrings, object : TypeToken<List<String?>?>() {}.type)

    }
}