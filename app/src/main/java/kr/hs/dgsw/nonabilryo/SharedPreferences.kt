package kr.hs.dgsw.nonabilryo

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val PREFS_FILENAME = "kr.hs.dgsw.nonabilryo.prefs"
    private val PREF_ACCESS_TOKEN = "accessToken"
    private val PREF_REFRESH_TOKEN = "refreshToken"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveTokens(accessToken: String, refreshToken: String) {
        editor.putString(PREF_ACCESS_TOKEN, accessToken)
        editor.putString(PREF_REFRESH_TOKEN, refreshToken)
        editor.commit()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(PREF_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(PREF_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        editor.remove(PREF_ACCESS_TOKEN)
        editor.remove(PREF_REFRESH_TOKEN)
        editor.commit()
    }
}