package com.example.mynotes.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mynotes.database.NotesDatabase
import com.example.mynotes.model.Notes
import com.example.mynotes.repository.NotesRepository

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NotesRepository

    var flagForSort = false



    init {
        val dao = NotesDatabase.getDatabaseInstance(application).myNotesDao()
        repository = NotesRepository(dao)
    }
    fun addNotes(notes: Notes){
        repository.insertNotes(notes)
    }

    fun getNotes(): LiveData<List<Notes>> {
        return repository.getAllNotes()
    }

    fun deleteNotes(id: Int){
        repository.deleteNotes(id)
    }

    fun updateNotes(notes: Notes){
        repository.updateNotes(notes)
    }

    fun sortedDesc(): LiveData<List<Notes>> = repository.sortByIdDesc()

    fun sortedAsc(): LiveData<List<Notes>> = repository.sortByIdAsc()

    fun setFlag(flag: Boolean){
        flagForSort = flag
    }

}