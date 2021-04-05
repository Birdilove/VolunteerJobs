package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface jobDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJob(job:job)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllJob(jobs: List<job>)

    @Query("SELECT * FROM job ")
    fun getAll(): LiveData<List<job>>

    @Query("SELECT * FROM job WHERE jobId = :id AND postedBy = :postedBy")
    fun getJobById(id: Int, postedBy: String): job?

    @Query("SELECT MAX(jobId) FROM job WHERE postedBy = :email")
    fun getMAXJobId(email: String): Int?

    @Delete
    fun deleteJob(job: job)

    @Delete
    fun deleteJobList(job: List<job>)

    @Query("DELETE FROM job")
    fun deleteAll()
}