package com.csis4280.volunteerjobs.ui.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.firebase.auth.EmailAuthProvider

@Dao
interface participantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(participants: participants)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllParticipants(participants: List<participants>)

    @Query("SELECT * FROM participants")
    fun getAll(): List<participants>

    @Query("SELECT * FROM participants Where participants.jobId = :id AND participants.userEmail = :email")
    fun getParticipentById(id: Int, email: String): participants?

    @Query("Select * from job join participants on job.jobId = participants.jobId AND job.postedBy = participants.postedBy where participants.userEmail = :email")
    fun getAllParticipations(email: String): LiveData<List<job>>

    @Query("SELECT * FROM job WHERE jobId = :id")
    fun getJobById(id: Int): job?

    @Delete()
    fun deleteEntry(note: participants)

}