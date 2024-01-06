package com.example.listxml.data.room.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ListRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {


    fun getAllLists(): Flow<List<ListEntity>> {
        return repository.getAllLists()
            .flowOn(Dispatchers.IO)

    }

    fun getListsByUserId(): Flow<List<ListEntity>> {
        return userSessionManager.getUser()?.id?.let { repository.getUsersListById(it) }
            ?: emptyFlow()
    }

    fun getListById(listId: String): Flow<ListEntity> {
        return repository.getListById(listId)
    }

    fun getUser(): UserEntity? {
        return userSessionManager.getUser()
    }

    fun insertLists(listEntity: ListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLists(listEntity)
        }
    }

    fun updateLists(listEntity: ListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateList(listEntity)
        }
    }

    fun removeList(listEntity: ListEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteList(listEntity)
        }
    }

    fun removeAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}