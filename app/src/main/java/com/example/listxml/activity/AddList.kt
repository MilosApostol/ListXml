package com.example.listxml.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.listxml.Constants
import com.example.listxml.data.firebase.list.ListFireViewModel
import com.example.listxml.data.room.list.ListEntity
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.databinding.ActivityAddListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class AddList : AppCompatActivity() {
    private lateinit var binding: ActivityAddListBinding
    private val listViewModel: ListViewModel by viewModels()
    private val listFireViewModel: ListFireViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "AddLists"

        val listId = intent.getStringExtra("listId")
        if (listId != null) {
            //this works
            lifecycleScope.launch(Dispatchers.Main) {
                listViewModel.getListById(listId)
                listViewModel.listUpdate.observe(this@AddList) { list ->
                    when {
                        list != null && list.id == listId -> binding.textViewList.setText(list.name)
                        else -> {
                        }
                    }
                }
            }
        }

        binding.buttonAddList.setOnClickListener {
            val listName = binding.textViewList.text.toString()
            if (listName.isNotEmpty()) {
                if (listId == null) {
                    val reference = FirebaseDatabase.getInstance().getReference(Constants.Lists)
                    val key = reference.key!!
                    val list = ListEntity(
                        id = UUID.randomUUID().toString(),
                        name = listName,
                        listCreatorId = Firebase.auth.currentUser?.uid!!
                    )
                    reference.push()
                        .setValue(list) { _, ref ->
                            val key = ref.key
                            list.id = key!!
                            lifecycleScope.launch {
                                listFireViewModel.insertList(ref, list, key) { _ ->
                                    val intent = Intent(this@AddList, ListActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                        }
                } else {

                    listFireViewModel.updateList(
                        ListEntity(
                            id = listId,
                            name = listName,
                            listCreatorId = Firebase.auth.currentUser?.uid!!
                        )
                    )
                    val intent = Intent(this@AddList, ListActivity::class.java)
                    startActivity(intent)
                }
                //offline
                /*
            } else {
                Toast.makeText(this, "sad insert", Toast.LENGTH_LONG).show()
                listViewModel.insertLists(
                    ListEntity(
                        name = listName,
                        id = UUID.randomUUID().toString(),
                        listCreatorId = userId ?: ""
                    )
                )
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }

                 */
            } else {
                Toast.makeText(this@AddList, "Empty list", Toast.LENGTH_LONG).show()
            }
        }
    }
}