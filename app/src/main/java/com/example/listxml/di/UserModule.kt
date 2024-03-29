package com.example.listxml.di

import com.example.listxml.data.firebase.user.UserRepFirebase
import com.example.listxml.data.room.AppDatabase
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserDao
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()


    @Provides
    @Singleton
    fun providesUserRoom(
        dao: UserDao,
        coroutineScope: CoroutineScope
    ) = UserRepository(dao, coroutineScope)

    @Provides
    @Singleton
    fun providesUserViewModel(
        repository: UserRepository,
        userRepFirebase: UserRepFirebase,
        sessionManager: UserSessionManager
    ) =
        UserViewModel(repository, userRepFirebase, sessionManager)
}