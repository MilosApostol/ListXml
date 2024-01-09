package com.example.listxml.additems

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddItemsViewModel @Inject constructor(val addItemsRep: AddItemsRep): ViewModel() {

     val searchText = MutableStateFlow("")

     val addItem = MutableLiveData(listOf<AddItemsEntity>())

    fun getItems() {
        viewModelScope.launch {
            addItem.value = addItemsRep.getItems()
        }
    }

    fun onSearchChange(text: String) {
        searchText.value = text
    }


}