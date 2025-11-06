package com.iprism.ecmhealthadvisor.utils

import android.content.Context
import android.content.SharedPreferences

class User(var context: Context) {

    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    init {
        sharedPreferences = context.getSharedPreferences("GrossBytes", PRIVATE_MODE)
        editor = sharedPreferences.edit()
    }

    fun storeMainDataId(mainDataId : String?){
        editor.putString(MAIN_DATA_ID, mainDataId)
        editor.commit()
    }

    fun storeUserDetails(
        id: String?,
        authToken: String?,
        name: String?,
        mobile: String?,
        hospital_name: String?
    ) {
        editor.putString(ID, id)
        editor.putString(AUTH_TOKEN, authToken)
        editor.putString(NAME, name)
        editor.putString(MOBILE, mobile)
        editor.putString(HOSPITAL_NAME, hospital_name)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.commit()
    }

    fun getUserDetails(): HashMap<String, String?> {
        val user = HashMap<String, String?>()
        user[ID] = sharedPreferences.getString(ID, null)
        user[AUTH_TOKEN] = sharedPreferences.getString(AUTH_TOKEN, null)
        user[NAME] = sharedPreferences.getString(NAME, null)
        user[MOBILE] = sharedPreferences.getString(MOBILE, null)
        user[EMAIL] = sharedPreferences.getString(EMAIL, null)
        user[HOSPITAL_NAME] = sharedPreferences.getString(HOSPITAL_NAME, null)
        user[MAIN_DATA_ID] = sharedPreferences.getString(MAIN_DATA_ID, null)
        return user
    }

    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false)
    }

    fun logoutUser() {
        editor.clear()
        editor.apply()
    }

    companion object {

        const val ID = "id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val MOBILE = "mobile"
        const val HOSPITAL_NAME = "hospital_name"
        const val AUTH_TOKEN = "auth_token"
        const val MAIN_DATA_ID = "main_data_id"
        const val IS_USER_LOGIN = "isUserLogin"
    }

}