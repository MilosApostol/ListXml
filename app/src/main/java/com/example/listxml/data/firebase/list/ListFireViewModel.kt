package com.example.listxml.data.firebase.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListRepository
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListFireViewModel @Inject constructor(
    val repository: ListFireRepository,
    val userSessionManager: UserSessionManager,
    val userRepository: UserRepository,
    val listRepository: ListRepository
) : ViewModel() {
     val lists = MutableLiveData<List<ListEntity>>(emptyList())
    private val listUpdate = MutableLiveData<ListEntity>()


    fun insertList(
        reference: DatabaseReference,
        list: ListEntity,
        key: String,
        callback: (Boolean) -> Unit
    ) {
        repository.insertList(reference, list, key, callback)
    }

    fun readData(filterId: String) {
        repository.readData {
            val filteredLists = it.filter { list -> list.listCreatorId == filterId }

            lists.value = filteredLists
        }
    }
    fun removeList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = listRepository.getListById(id)
            listRepository.deleteList(list)
            repository.deleteList(id)
            withContext(Dispatchers.Main) {
                lists.value = lists.value?.filter { it.id != id }
            }
        }
    }
    fun updateList(update: ListEntity) {
        repository.updateList(update)
        listUpdate.postValue(update)
    }


}