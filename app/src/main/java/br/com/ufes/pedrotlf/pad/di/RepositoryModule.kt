package br.com.ufes.pedrotlf.pad.di

import br.com.ufes.pedrotlf.pad.api.SadeApi
import br.com.ufes.pedrotlf.pad.data.SadeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideOrderRepository(sadeApi: SadeApi): SadeRepository {
        return SadeRepository(sadeApi)
    }
}