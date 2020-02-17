package com.example.android.sampleproject.network

import com.example.android.sampleproject.model.BlogModel
import io.reactivex.Single
import javax.inject.Inject

class BloggersService {
    @Inject
    lateinit var api: RetrofitApi
    init {
        DaggerApiComponent.create().inject(this)
    }
    fun getBloggers(): Single<BlogModel> {
        return api.getBlogs()
    }
}