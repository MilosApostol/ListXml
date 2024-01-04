package com.example.listxml.di

import com.example.listxml.data.room.AppDatabase
import com.example.listxml.data.room.UserViewModel
import com.example.listxml.data.room.user.UserDao
import com.example.listxml.data.room.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton


@Module
@InstallIn(Singleton::class)
class UserModule {

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()


    @Provides
    @Singleton
    fun providesUserRoom(dao: UserDao) = UserRepository(dao)

    @Provides
    @Singleton
    fun providesUserViewModel(repository: UserRepository) = UserViewModel(repository)
}