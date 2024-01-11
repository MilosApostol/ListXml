package com.example.listxml.data.firebase.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.item.ItemsRepository
import com.example.listxml.data.room.list.ListEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ItemsFireViewModel @Inject constructor(
    val repository: ItemsFireRepository,
    val repositoryRoom: ItemsRepository
) : ViewModel() {

     val items = MutableLiveData<List<ItemsEntity>>()

    fun insertItems(
        reference: DatabaseReference,
        list: ItemsEntity,
        callback: (Boolean) -> Unit){
        repository.insertItems(reference, list, callback)
    }

    fun fetchItems() {
        viewModelScope.launch {
            repository.readData { itemsList ->
                items.value = itemsList
            }
        }
    }


    fun removeList(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = repositoryRoom.findItemById(id)
            repositoryRoom.deleteItems(item)
            repository.deleteList(id)
            withContext(Dispatchers.Main) {
                items.value = items.value?.filter { it.id != id }
            }
        }
    }
}