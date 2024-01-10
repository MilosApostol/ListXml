package com.example.listxml.additems

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listxml.LiveEvent
import com.example.listxml.Preferences
import com.example.listxml.utill.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemsViewModel @Inject constructor(
    val addItemsRep: AddItemsRep,
    val contextProvider: ContextProvider
) : ViewModel() {
    val onNetworkRestoredEvent = LiveEvent<Unit>()
    val searchTextLiveData = MutableLiveData<String>()
    val allItemsData = addItemsRep.getAllItems()
     val addItems = MutableLiveData<List<AddItemsEntity>>(emptyList())
    init {
        initiateFetching()
    }
    private fun initiateFetching() {
        viewModelScope.launch {
            fetchItemsList()
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