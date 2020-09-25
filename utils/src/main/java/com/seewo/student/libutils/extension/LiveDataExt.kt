package com.seewo.student.libutils.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {

    observe(lifecycleOwner, object : Observer<T> {

        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {

    observeForever(object : Observer<T> {

        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun <T> LiveData<T>.observeOnce(filter: ((T?) -> Boolean), observer: Observer<T>) {

    observeForever(object : Observer<T> {

        override fun onChanged(t: T?) {
            observer.onChanged(t)
            if (filter.invoke(t)) {
                removeObserver(this)
            }
        }
    })
}

fun <T> LiveData<T>.noDirtyObserve(owner: LifecycleOwner, observer: Observer<in T>) {
    this.observe(owner, NoDirtyObserver(observer, this))
}

private class NoDirtyObserver<T>(
    private val observer: Observer<in T>,
    private val liveData: LiveData<T>
) : Observer<T> {

    private var lastVersion = getVersion()

    override fun onChanged(t: T?) {
        val version = getVersion()
        if (lastVersion >= version) {
            return
        }
        lastVersion = version
        observer.onChanged(t)
    }

    private fun getVersion(): Int {
        val liveDataClass = LiveData::class.java
        try {
            val versionField = liveDataClass.getDeclaredField("mVersion")
            versionField.isAccessible = true
            return versionField[liveData] as Int
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }
}

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this, object : Observer<T> {

        private var isInitialized = false
        private var previousValue: T? = null

        override fun onChanged(newValue: T?) {
            val wasInitialized = isInitialized
            if (!isInitialized) {
                isInitialized = true
            }
            if (!wasInitialized || newValue != previousValue) {
                previousValue = newValue
                mediator.postValue(newValue)
            }
        }
    })
}
