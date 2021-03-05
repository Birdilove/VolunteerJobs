package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface jobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJob(job:job)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllJob(jobs: List<job>)

    @Query("SELECT * FROM job ")
    fun getAll(): LiveData<List<job>>

}