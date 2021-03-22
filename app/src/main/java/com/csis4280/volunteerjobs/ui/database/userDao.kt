package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface userDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUser(user:user)

    @Query("SELECT * FROM user ")
    fun getAll():LiveData<user>

    @Query("SELECT * FROM user WHERE userEmail = :email")
    fun getUserById(email: String): user?

}