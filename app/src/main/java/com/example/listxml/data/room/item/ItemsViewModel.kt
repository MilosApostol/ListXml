package com.example.listxml.data.room.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.list.ListEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel @Inject constructor(val repository: ItemsRepository) : ViewModel() {

    fun insertItems(items: ItemsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItems(items)
        }
    }
}