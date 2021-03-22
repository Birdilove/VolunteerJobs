package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface jobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJob(job:job)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllJob(jobs: List<job>)

    @Query("SELECT * FROM job ")
    fun getAll(): LiveData<List<job>>

    @Query("SELECT * FROM job WHERE jobId = :id")
    fun getJobById(id: Int): job?

    @Delete
    fun deleteJob(note: job)


}