package com.ping.android

import android.content.Context
import androidx.lifecycle.*
import com.ping.android.repository.PingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MainActivityViewModel(private val pingRepository: PingRepository): ViewModel() {

    @ExperimentalCoroutinesApi
    val state: LiveData<StartingState>? = this.pingRepository
        .ping("ping -c 1 -w 1 https://www.google.com")
        .map {
            StartingState(it?.cnt.toString())
        }
        .flowOn(Dispatchers.IO)
        .asLiveData(Dispatchers.Main + this.viewModelScope.coroutineContext)
}

data class StartingState(val ping: String)

class MainActivityViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(PingRepository(context)) as T
    }
}