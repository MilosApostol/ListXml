package com.example.listxml.data.room.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.ListAdapter
import com.example.listxml.data.room.user.UserEntity
import com.example.listxml.session.UserSessionManager
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

    private val _lists = MutableLiveData<List<ListEntity>>(emptyList())
    val lists: LiveData<List<ListEntity>> = _lists

    private val _listUpdate = MutableLiveData<ListEntity>()
    val listUpdate: LiveData<ListEntity> = _listUpdate



    fun getAllLists(): LiveData<List<ListEntity>> {
        return repository.getAllLists()

    }

    fun getListsByUserId() = viewModelScope.launch {
        val userId = userSessionManager.getUser()?.id ?: return@launch
        val lists = withContext(Dispatchers.IO) {
            repository.getUsersListById(userId)
        }
        _lists.value = lists
    }

    //update
    fun getListById(listId: String) = viewModelScope.launch(Dispatchers.IO) {
        val fetchedList = repository.getListById(listId)
        _listUpdate.postValue(fetchedList)

    }

    fun updateList(list: ListEntity) {
        list.listCreatorId = userSessionManager.getUserId()
        viewModelScope.launch {
            repository.updateList(list)
            _listUpdate.postValue(list)
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

        fun updateList(listId: String, newName: String) = viewModelScope.launch {
            val updatedList = lists.value!!.find { it.id == listId }
            if (updatedList != null) {
                val newList = updatedList.copy(name = newName)
                _lists.value = lists.value!!.toMutableList().apply {
                    removeIf { it.id == listId }
                    add(newList)
                }
                repository.updateList(newList)
            }
        }

    fun removeList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.getListById(id)
            repository.deleteList(list)

            withContext(Dispatchers.Main) {
                _lists.value = lists.value?.filter { it.id != id }
            }
        }
    }


        fun removeAll() {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteAll()
            }
        }
    }
