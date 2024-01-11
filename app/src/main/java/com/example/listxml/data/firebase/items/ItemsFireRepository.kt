package com.example.listxml.data.firebase.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listxml.Constants
import com.example.listxml.additems.AddItemsDao
import com.example.listxml.data.room.item.ItemsDao
import com.example.listxml.data.room.item.ItemsEntity
import com.example.listxml.data.room.list.ListDao
import com.example.listxml.data.room.list.ListEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ItemsFireRepository @Inject constructor(
    val itemsDao: ItemsDao, val externalScope: CoroutineScope
) {
    private val items = MutableLiveData<List<ListEntity>>(emptyList())

    private var job: Job? = null

    private val reference = FirebaseDatabase.getInstance().getReference(Constants.Items)

    fun insertItems(
        reference: DatabaseReference,
        item: ItemsEntity,
        key: String,
        callback: (Boolean) -> Unit
    ) {
        externalScope.launch {
            val updateItem = item.copy(id = key)
            itemsDao.insertItem(itemsEntity = updateItem)

            reference.child(key).setValue(updateItem).await()
            callback(true)
        }
    }

    fun readData(returnedItems: (MutableList<ItemsEntity>) -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemsNew = mutableListOf<ItemsEntity>()
                for (itemSnapshot in snapshot.children) {
                    val itemList: ItemsEntity? = itemSnapshot.getValue(ItemsEntity::class.java)
                    itemList?.let { itemsNew.add(it) }
                }
                returnedItems(itemsNew)
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        job = Job()

        job?.let { current ->
            reference.addValueEventListener(valueEventListener)

            current.invokeOnCompletion {
                if (it is CancellationException) {
                }
                reference.removeEventListener(valueEventListener)
            }
        }
    }

    fun deleteList(itemsId: String) {
        reference.child(itemsId).removeValue()
    }
}