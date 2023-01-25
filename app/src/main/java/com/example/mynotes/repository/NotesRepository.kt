package com.example.mynotes.repository

import androidx.lifecycle.LiveData
import com.example.mynotes.dao.NotesDao
import com.example.mynotes.model.Notes

class NotesRepository(private val dao: NotesDao) {

    fun getAllNotes() : LiveData<List<Notes>>{
        return dao.getNotes()
    }

    fun insertNotes(notes: Notes) {
        dao.insertNotes(notes)
    }

    fun deleteNotes(id: Int){
        dao.deleteNotes(id)
    }

    fun updateNotes(notes: Notes){
        dao.updateNotes(notes)
    }
}