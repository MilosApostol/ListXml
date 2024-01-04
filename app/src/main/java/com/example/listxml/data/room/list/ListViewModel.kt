package com.example.listxml.data.room.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(val repository: ListRepository): ViewModel() {

    private val _getAllLists = MutableStateFlow<List<ListEntity>>(emptyList())
    val getAllLists: StateFlow<List<ListEntity>> get() = _getAllLists.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllLists().collect{
                _getAllLists.value = it
            }
        }
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