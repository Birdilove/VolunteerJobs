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
                parentColumns = arrayOf("jobId"),
                childColumns = arrayOf("jobId"),
                onDelete = ForeignKey.CASCADE
        ), ForeignKey(
                entity = user::class,
                parentColumns = arrayOf("userId"),
                childColumns = arrayOf("userId"),
                onDelete = ForeignKey.CASCADE
        )],
)
data class participants(
        @PrimaryKey
        var jobId: Int,
        var userId: Int,
        var userEmail: String) : Parcelable {
    constructor() : this(0, 0, "")
}
