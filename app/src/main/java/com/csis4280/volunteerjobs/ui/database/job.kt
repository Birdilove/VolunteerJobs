package com.csis4280.volunteerjobs.ui.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "job")
data class job(
    @PrimaryKey(autoGenerate = true)
    var jobId: Int,
    var jobTitle: String,
    var jobDescription: String,
    var jobType:String,
    var startDate: Date,
    var endDate: Date,
    var postedBy: String
): Parcelable {
    constructor() : this(0, "", "","", Date(), Date(),"")
    constructor(jobTitle: String, jobDescription: String, jobType: String, startDate: Date, endDate: Date, postedBy: String) : this(0, jobTitle,jobDescription,jobType,startDate,endDate, postedBy)
}

