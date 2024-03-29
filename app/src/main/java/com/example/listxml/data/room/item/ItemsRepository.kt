package com.example.listxml.data.room.item

import javax.inject.Inject

class ItemsRepository @Inject constructor(val dao: ItemsDao){

    fun insertItems(itemsEntity: ItemsEntity){
        dao.insertItem(itemsEntity)
    }

    fun findItemById(id: String): ItemsEntity{
        return dao.getItemsById(id)
    }

    fun updateItems(itemsEntity: ItemsEntity){
        dao.updateItem(itemsEntity)
    }

    fun deleteItems(itemsEntity: ItemsEntity){
        dao.deleteItem(itemsEntity)
    }

}