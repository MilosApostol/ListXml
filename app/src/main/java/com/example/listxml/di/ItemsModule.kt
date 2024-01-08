package com.example.listxml.di

import com.example.listxml.data.room.AppDatabase
import com.example.listxml.data.room.item.ItemsDao
import com.example.listxml.data.room.item.ItemsRepository
import com.example.listxml.data.room.item.ItemsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ItemsModule {

    @Provides
    @Singleton
    fun providesItemsDao(database: AppDatabase) = database.itemsDao()

    @Provides
    @Singleton
    fun providesItemsRepository(dao: ItemsDao) = ItemsRepository(dao)

    @Provides
    @Singleton
    fun providesItemsViewModel(repository: ItemsRepository) = ItemsViewModel(repository)
}