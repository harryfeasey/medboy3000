package com.example.medboy2000

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface MedicationDAO {


    @Query("SELECT * from meds_table ORDER BY datetime(reminder) ASC")
    fun getAllMeds(): LiveData<List<Medication>>

    @Query("Select * FROM meds_table where id = :id")
    fun findByPrimaryKey(id: Int): Medication

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medication: Medication)

    @Update
    suspend fun updateMedication(medication: Medication)

    @Delete
    suspend fun deleteMedication(medication: Medication)

    @Query("DELETE FROM meds_table")
    suspend fun deleteAll()

}