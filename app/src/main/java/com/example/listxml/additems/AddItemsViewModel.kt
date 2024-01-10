package com.example.listxml.additems

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.LiveEvent
import com.example.listxml.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemsViewModel @Inject constructor(
    val addItemsRep: AddItemsRep
) : ViewModel() {
    val onNetworkRestoredEvent = LiveEvent<Unit>()
    val searchTextLiveData = MutableLiveData<String>()
    val allItemsData = addItemsRep.getAllItems()
     val addItems = MutableLiveData<List<AddItemsEntity>>(emptyList())
    init {
        initiateFetching()
    }
    /*
    fun getItems() {
        viewModelScope.launch {
            _addItem.value = addItemsRep.getItems()
        }
    }

     */

    private fun initiateFetching() {
        viewModelScope.launch {
            fetchItemsList()
        }
    }

    val filteredItemListLiveData: LiveData<List<AddItemsEntity>> =
        MediatorLiveData<List<AddItemsEntity>>().apply {
            addSource(addItems) { addItemList ->
                val searchText = searchTextLiveData.value.orEmpty()

                value = if (searchText.isBlank()) {
                    addItemList
                } else {
                    addItemList.filter { item ->
                        item.title.contains(searchText, ignoreCase = true)
                    }
                }
            }

            addSource(searchTextLiveData) { searchText ->
                val addItemList = addItems.value.orEmpty()

                value = if (searchText.isBlank()) {
                    addItemList
                } else {
                    addItemList.filter { item ->
                        item.title.contains(searchText, ignoreCase = true)
                    }
                }
            }
        }

    private fun fetchItemsList() {
        viewModelScope.launch {
           addItemsRep.getItems()
        }
    }

    fun onSearchChange(text: String) {
        searchTextLiveData.value = text
    }


}