package com.example.listxml.data.room.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.Constants
import com.example.listxml.ListAdapter
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.session.UserSessionManager
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ListRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

     val lists = MutableLiveData<List<ListEntity>>(emptyList())

     val listUpdate = MutableLiveData<ListEntity>()

//offline
    fun getListsByUserId() = viewModelScope.launch {
        val userId = userSessionManager.getUser().id
        val userLists = withContext(Dispatchers.IO) {
            repository.getUsersListById(userId)
        }
        lists.value = userLists
    }

    //update
    fun getListById(listId: String) = viewModelScope.launch(Dispatchers.IO) {
        val fetchedList = repository.getListById(listId)
        listUpdate.postValue(fetchedList)

    }

    fun updateList(list: ListEntity) {
        viewModelScope.launch {
            repository.updateList(list)
            listUpdate.postValue(list)
        }
    }

    fun getUser(): UserEntity {
        return userSessionManager.getUser()
    }

    fun insertLists(listEntity: ListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLists(listEntity)
        }
    }

    fun removeList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getListById(id)
            repository.deleteList(list)
            withContext(Dispatchers.Main) {
                lists.value = lists.value?.filter { it.id != id }
            }
        }
    }


    fun removeAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}
