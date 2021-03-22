package com.csis4280.volunteerjobs.ui.database

import androidx.room.TypeConverter
import java.util.*

class DateConverter {


        @TypeConverter
        fun  fromTimestamp(value: Long): Date {
            return Date(value)
        }
        @TypeConverter
        fun dateToTimestamp(date: Date): Long {
            return date.time
        }




}