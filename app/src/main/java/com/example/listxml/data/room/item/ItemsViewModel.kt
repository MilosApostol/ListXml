package com.example.listxml.data.room.item

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ItemsViewModel @Inject constructor(val repository: ItemsRepository) : ViewModel() {


}