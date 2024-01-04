package com.example.listxml.data.room.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(val repository: ListRepository): ViewModel() {


    fun getAllLists(): Flow<List<ListEntity>>{
        return repository.getAllLists()
            .flowOn(Dispatchers.IO)

    }

    fun insertLists(listEntity: ListEntity) {
        viewModelScope.launch {
            repository.insertLists(listEntity)
        }
    }

    fun updateLists(listEntity: ListEntity){
        viewModelScope.launch {
            repository.updateList(listEntity)
        }
    }

    fun removeList(listEntity: ListEntity){
        viewModelScope.launch {
            repository.deleteList(listEntity)
        }
    }

    fun removeAll(){
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}