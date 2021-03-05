package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao

interface participantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(participants: participants)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllParticipants(participants: List<participants>)

    @Query("SELECT * FROM participants ")
    fun getAll(): LiveData<List<participants>>

}