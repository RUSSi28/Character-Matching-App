package com.example.charactermatchingapp.data.auth.dataSource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class DataStoreManager(private val context: Context) {
    private val mail = stringPreferencesKey("mail")
    private val password = stringPreferencesKey("password")

    fun getMail(): Flow<String> {
        return context.dataStore.data.map {
            it[mail] ?: ""
        }
    }

    fun getPassword(): Flow<String> {
        return context.dataStore.data.map {
            it[password] ?: ""
        }
    }

    suspend fun saveMail(text: String) {
        context.dataStore.edit {
            it[mail] = text
        }
    }

    suspend fun savePassword(text: String) {
        context.dataStore.edit {
            it[password] = text
        }
    }
}