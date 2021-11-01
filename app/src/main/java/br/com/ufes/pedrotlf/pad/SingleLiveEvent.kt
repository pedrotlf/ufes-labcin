package br.com.ufes.pedrotlf.pad

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A LiveData that notifies only one observer
 */
class SingleLiveEvent<T>: MediatorLiveData<T>() {

    private val pending = AtomicBoolean()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if(hasActiveObservers()){
            Log.w(TAG, "Multiple observers registered but only one will be notified")
        }
        super.observe(owner, {
            if(pending.compareAndSet(true, false)){
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call(){
        value = null
    }

    companion object{
        const val TAG = "SingleLiveEvent"
    }
}