package com.example.android.sampleproject.network

import com.example.android.sampleproject.model.BlogModel
import io.reactivex.Single
import retrofit2.http.GET

interface RetrofitApi {
    @GET("blogs")
    fun getBlogs(): Single<BlogModel>
}
