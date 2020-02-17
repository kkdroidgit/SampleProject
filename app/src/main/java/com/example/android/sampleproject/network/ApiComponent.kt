package com.example.android.sampleproject.network

import com.example.android.sampleproject.model.ListViewModel
import com.example.android.sampleproject.network.ApiModule
import com.example.android.sampleproject.network.BloggersService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: BloggersService)
    fun inject(viewModel: ListViewModel)
}
