package br.com.ufes.pedrotlf.pad.di

import android.app.Application
import androidx.room.Room
import br.com.ufes.pedrotlf.pad.data.PatientsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabse(
        app: Application,
        callback: PatientsDatabase.Callback
    ) = Room.databaseBuilder(app, PatientsDatabase::class.java, "card_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun providePatientDao(db: PatientsDatabase) = db.patientDAO()
}