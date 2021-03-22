package com.csis4280.volunteerjobs.ui.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "user")
data class user(
    @PrimaryKey(autoGenerate = true)
    var userId: Int,
    var userName: String ,
    var userEmail: String): Parcelable {
    constructor() : this(0, "", "")
    constructor(userName: String, userEmail: String) : this(0, userName, userEmail)
}