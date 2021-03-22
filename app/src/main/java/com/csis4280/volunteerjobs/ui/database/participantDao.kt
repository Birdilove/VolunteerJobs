package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface participantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(participants: participants)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllParticipants(participants: List<participants>)

    @Query("SELECT * FROM participants")
    fun getAll(): List<participants>

    @Query("SELECT * FROM participants Where participants.jobId = :id")
    fun getParticipentById(id: Int): participants?

    @Query("SELECT * FROM job JOIN participants ON participants.jobId = job.jobId")
    fun getAllParticipations(): LiveData<List<job>>

    @Query("SELECT * FROM job WHERE jobId = :id")
    fun getJobById(id: Int): job?

    @Delete()
    fun deleteEntry(note: participants)

}