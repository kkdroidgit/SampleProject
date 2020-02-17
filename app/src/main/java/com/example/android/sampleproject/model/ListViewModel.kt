package com.example.android.sampleproject.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.sampleproject.network.BloggersService
import com.example.android.sampleproject.network.DaggerApiComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel : ViewModel() {

    @Inject
    lateinit var bloggerService: BloggersService
    init {
        DaggerApiComponent.create().inject(this)
    }
    private val disposable = CompositeDisposable()
    val listBloggers = MutableLiveData<List<Bloggers>>()
    val apiError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchUsers()
    }

    private fun fetchUsers() {
        loading.value = true
        disposable.add(
            bloggerService.getBloggers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BlogModel>() {
                    override fun onSuccess(data: BlogModel) {
                        Log.d("error ", "" + data)
                        listBloggers.value = data.body.sortedBy { it.title }
                        apiError.value = false
                        loading.value = false
                    }
                    override fun onError(e: Throwable) {
                        apiError.value = true
                        loading.value = false
                        Log.d("error ", "" + e.printStackTrace())
                    }
                })
        )
    }
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}