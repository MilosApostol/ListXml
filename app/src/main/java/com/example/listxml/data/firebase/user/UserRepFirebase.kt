package com.example.listxml.data.firebase.user

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class UserRepFirebase @Inject constructor(private val auth: FirebaseAuth) {


    suspend fun logIn(email: String, password: String): Boolean {
        val resultDeferred = CompletableDeferred<Boolean>()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                resultDeferred.complete(true)
            } else {
                resultDeferred.complete(false)
            }

        }
        return resultDeferred.await()

    }

    suspend fun signUp(email: String, password: String): Boolean {
        val resultDeferred = CompletableDeferred<Boolean>()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                resultDeferred.complete(true)
            } else {
                resultDeferred.complete(false)
            }
        }
        return resultDeferred.await()
    }
}
