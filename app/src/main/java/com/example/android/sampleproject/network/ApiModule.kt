package com.example.android.sampleproject.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {

    @Provides
    fun provideUsersApi(): RetrofitApi {
        return Retrofit.Builder()
            .baseUrl("https://yz9ovz3ag5.execute-api.us-east-1.amazonaws.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
    }

    @Provides
    fun provideUsersService(): BloggersService {
        return BloggersService()
    }

}