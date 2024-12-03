package com.sametdundar.guaranteeapp.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormData(formData: FormData)

    @Query("SELECT * FROM form_data")
    fun getAllFormData(): LiveData<List<FormData>>

    @Update
    suspend fun updateFormData(formData: FormData)

    @Delete
    suspend fun delete(formData: FormData)
}
