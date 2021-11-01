package br.com.ufes.pedrotlf.pad

import android.app.Application
import android.content.Context

class MyPrefs(app: Application) {
    private val prefs = app.getSharedPreferences("pad_ufes_prefs", Context.MODE_PRIVATE)

    var serverUrl: String
        get() {
            val finalString: String

            val serverIp = prefs.getString("server_url", null)
            if(serverIp == null){
                finalString = BuildConfig.REPO_DEFAULT_URL
                with (prefs.edit()) {
                    putString("server_url", finalString)
                    commit()
                }
            } else {
                finalString = serverIp
            }

            return finalString
        }
        set(value) {
            with (prefs.edit()) {
                putString("server_url", value)
                commit()
            }
        }
}