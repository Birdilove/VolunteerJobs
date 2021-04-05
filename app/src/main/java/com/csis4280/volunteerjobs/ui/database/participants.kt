package com.csis4280.volunteerjobs.ui.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
        tableName = "participants",
        foreignKeys = [ForeignKey(
                entity = job::class,
                parentColumns = arrayOf("jobId", "postedBy"),
                childColumns = arrayOf("jobId", "postedBy"),
                onDelete = ForeignKey.CASCADE
        )],primaryKeys = ["jobId", "postedBy", "userEmail"]
)
data class participants(
        var jobId: Int,
        var postedBy: String,
        var userEmail: String) : Parcelable {
    constructor() : this(0,"",  "")
}
