package com.csis4280.volunteerjobs.ui.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "job", primaryKeys = ["jobId", "postedBy"])
data class job(
    var jobId: Int,
    var jobTitle: String,
    @Json(name = "jobDesc")var jobDescription: String,
    var jobType:String,
    @Json(name = "jobStartDate")var startDate: String,
    @Json(name = "jobEndDate")var endDate: String,
    var postedBy: String
): Parcelable {
    constructor() : this(0, "", "","", "", "","")
    constructor(jobTitle: String, jobDescription: String, jobType: String, startDate: String, endDate: String, postedBy: String) : this(0, jobTitle,jobDescription,jobType,startDate,endDate, postedBy)
}


