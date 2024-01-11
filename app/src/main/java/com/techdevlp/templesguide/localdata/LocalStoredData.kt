package com.techdevlp.templesguide.localdata

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.techdevlp.templesguide.localdata.model.LocationDetails
import com.techdevlp.templesguide.localdata.model.UserDetails
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalStoredData(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore("data_store_temple_trips")
        private val USER_DETAILS_KEY = stringPreferencesKey("user_details_key")
        private val LOCATION_DETAILS_KEY = stringPreferencesKey("location_details_key")
    }

    suspend fun getUserDetails(): UserDetails? {
        val jsonString = context.dataStore.data.map { preferences ->
            preferences[USER_DETAILS_KEY]
        }.first()

        return if (jsonString != null) {
            val gson = Gson()
            gson.fromJson(jsonString, UserDetails::class.java)
        } else {
            null
        }
    }

    suspend fun setUserDetails(userDetails: UserDetails?) {
        val gson = Gson()
        val jsonString = gson.toJson(userDetails)

        context.dataStore.edit { preferences ->
            preferences[USER_DETAILS_KEY] = jsonString
        }
    }

    suspend fun clearUserDetails() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_DETAILS_KEY)
        }
    }

    suspend fun getLocationDetails(): LocationDetails? {
        val jsonString = context.dataStore.data.map { preferences ->
            preferences[LOCATION_DETAILS_KEY]
        }.first()

        return if (jsonString != null) {
            val gson = Gson()
            gson.fromJson(jsonString, LocationDetails::class.java)
        } else {
            null
        }
    }

    suspend fun setLocationDetails(userDetails: LocationDetails?) {
        val gson = Gson()
        val jsonString = gson.toJson(userDetails)

        context.dataStore.edit { preferences ->
            preferences[LOCATION_DETAILS_KEY] = jsonString
        }
    }

    suspend fun clearLocationDetails() {
        context.dataStore.edit { preferences ->
            preferences.remove(LOCATION_DETAILS_KEY)
        }
    }
}