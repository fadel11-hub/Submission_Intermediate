package com.dicoding.picodiploma.loginwithanimation.data.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    // Save Session
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            Log.d("Token Debug", "Token disimpan: ${user.token}")
        }
    }

    // Get Session
    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            val token = preferences[TOKEN_KEY] ?: ""
            Log.d("Token Debug", "Token dimuat: $token")
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                token,
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    // Get Token Directly
    suspend fun getToken(): String {
        val token = dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }.first()
        Log.d("Token Debug", "Token diakses langsung: $token")
        return token
    }

    // Logout
    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
            Log.d("Token Debug", "Token dihapus saat logout.")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
