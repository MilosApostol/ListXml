package com.example.listxml.di

import com.example.listxml.data.firebase.user.UserFireViewModel
import com.example.listxml.data.firebase.user.UserRepFirebase
import com.example.listxml.data.room.user.UserRepository
import com.example.listxml.session.UserSessionManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Firebase {
    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth


    @Provides
    @Singleton
    fun providesViewModel(
        repository: UserRepFirebase,
        userRepository: UserRepository,
        userSessionManager: UserSessionManager,
        auth: FirebaseAuth
    ) =
        UserFireViewModel(repository, userRepository, userSessionManager, auth)

}