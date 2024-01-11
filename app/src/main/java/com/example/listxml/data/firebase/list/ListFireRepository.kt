package com.example.listxml.data.firebase.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.listxml.Constants
import com.example.listxml.data.room.list.ListDao
import com.example.listxml.data.room.list.ListEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ListFireRepository @Inject constructor(
    val listDao: ListDao, private val externalScope: CoroutineScope
) {
    private val _list = MutableLiveData<List<ListEntity>>(emptyList())
    val list: LiveData<List<ListEntity>> = _list

    private var job: Job? = null

    private val reference = FirebaseDatabase.getInstance().getReference(Constants.Lists)

    fun insertList(
        reference: DatabaseReference,
        list: ListEntity,
        key: String,
        callback: (Boolean) -> Unit
    ) {
        externalScope.launch {
            val updateList = list.copy(id = key)
            listDao.insertList(listEntity = updateList)

            reference.setValue(updateList).await()
            callback(true)
        }
    }

    fun readData(returnedList: (MutableList<ListEntity>) -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listNew = mutableListOf<ListEntity>()
                for (itemSnapshot in snapshot.children) {
                    val list: ListEntity? = itemSnapshot.getValue(ListEntity::class.java)
                    list?.let { listNew.add(it) }
                }
                returnedList(listNew)
                Log.d("listReadtrived", "Number of items retrieved: ${listNew.size}")

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

    fun deleteList(listId: String) {
        reference.child(listId).removeValue()
    }
    fun updateList(list: ListEntity) {
        externalScope.launch {

            reference.child(list.id).setValue(list)
            listDao.updateList(list)

        }
    }

}