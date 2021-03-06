package br.com.ufes.pedrotlf.pad.di

import br.com.ufes.pedrotlf.pad.MyPrefs
import br.com.ufes.pedrotlf.pad.api.SadeApi
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideRetrofit(prefs: MyPrefs): Retrofit {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS })
            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(prefs.serverUrl)
            .addConverterFactory(
                JacksonConverterFactory.create(
                    jacksonObjectMapper()
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                )
            )
            .client(client)
            .build()
    }

    @Provides
    fun provideOrderApi(retrofit: Retrofit): SadeApi =
        retrofit.create(SadeApi::class.java)
}