package br.com.ufes.pedrotlf.pad.di

import br.com.ufes.pedrotlf.pad.api.DermatologyApi
import br.com.ufes.pedrotlf.pad.data.DermatologyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideOrderRepository(dermatologyApi: DermatologyApi): DermatologyRepository {
        return DermatologyRepository(dermatologyApi)
    }
}