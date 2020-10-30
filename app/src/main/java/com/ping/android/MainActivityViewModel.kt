package com.ping.android

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ping.android.repository.PingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MainActivityViewModel @ViewModelInject constructor(private var pingRepository: PingRepository) : ViewModel() {

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