package br.com.ufes.pedrotlf.pad.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import br.com.ufes.pedrotlf.pad.MyPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreference(app: Application): MyPrefs {
        return MyPrefs(app)
    }
}