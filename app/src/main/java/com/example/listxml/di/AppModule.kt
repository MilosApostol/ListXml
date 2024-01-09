package com.example.listxml.di

import android.content.Context
import androidx.room.Room
import com.example.listxml.data.room.AppDatabase
import com.example.listxml.session.UserSessionManager
import com.example.listxml.utill.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesDataBase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext, AppDatabase::class.java, AppDatabase.DATABASE
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesUserSession(): UserSessionManager = UserSessionManager()
    @Singleton
    @Provides
    fun provideExternalScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun providesContextProvider(@ApplicationContext appContext: Context) =
        ContextProvider(appContext)
}
