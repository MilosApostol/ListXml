package com.example.listxml.data.room.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.user.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel @Inject constructor(val repository: ItemsRepository) : ViewModel() {


    val itemsList = MutableLiveData<List<ItemsEntity>>(emptyList())

    fun insertItems(items: ItemsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertItems(items)
        }
    }
    fun removeList(items: ItemsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItems(items)
            withContext(Dispatchers.Main) {
                itemsList.value = itemsList.value?.filter { it.id != items.id }
            }
        }
    }
}
