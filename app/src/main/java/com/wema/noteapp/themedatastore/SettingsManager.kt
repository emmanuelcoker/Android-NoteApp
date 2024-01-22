package com.wema.noteapp.themedatastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "settingsManager_pref")

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
    }

    suspend fun setUIMode(uiModel: UIModel){
        dataStore.edit { preference ->
            preference[IS_DARK_MODE] = when(uiModel){
                UIModel.LIGHT -> false
                UIModel.DARK -> true
            }
        }
    }

    val uiModelFlow : Flow<UIModel> = dataStore.data
        .catch {
            if(it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            }else{
                throw  it
            }
        }.map {  preference ->
            when(preference[IS_DARK_MODE]){
                true -> UIModel.DARK
                false -> UIModel.LIGHT
                else -> UIModel.DARK
            }
        }

}