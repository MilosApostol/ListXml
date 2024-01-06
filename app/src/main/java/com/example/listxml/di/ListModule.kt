package com.example.listxml.di

import com.example.listxml.data.room.AppDatabase
import com.example.listxml.data.room.list.ListDao
import com.example.listxml.data.room.list.ListRepository
import com.example.listxml.data.room.list.ListViewModel
import com.example.listxml.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ListModule {

    @Provides
    @Singleton
    fun provideListDao(database: AppDatabase) = database.listDao()

    @Provides
    @Singleton
    fun provideListRepository(dao: ListDao) = ListRepository(dao)

    @Provides
    @Singleton
    fun provideListViewModel(repository: ListRepository, userSessionManager: UserSessionManager) =
        ListViewModel(repository, userSessionManager)
}