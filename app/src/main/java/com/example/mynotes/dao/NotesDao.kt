package com.example.mynotes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynotes.model.Notes

@Dao
interface NotesDao {
    @Query("SELECT * FROM Notes")
    fun getNotes(): LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes: Notes)

    @Query("DELETE FROM Notes WHERE id =:id")
    fun deleteNotes(id: Int)

    @Update
    fun updateNotes(notes: Notes )

    @Query("SELECT * FROM Notes ORDER BY id DESC")
    fun sortByDESC(): LiveData<List<Notes>>

    @Query("SELECT * FROM Notes ORDER BY id")
    fun sortByASC(): LiveData<List<Notes>>
}