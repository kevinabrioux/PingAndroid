package com.ping.android.fragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ping.android.repository.PingRepositoryInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class PingFragmentViewModel @ViewModelInject constructor(private var pingRepository: PingRepositoryInterface) : ViewModel() {

    @ExperimentalCoroutinesApi
    private val _state = MutableStateFlow(StartingState(""))

    init {
        this.viewModelScope.launch(Dispatchers.Main) {
            this@PingFragmentViewModel.pingRepository
                .ping("ping -c 1 -w 1 https://www.google.com")
                .map {
                    StartingState(it?.cnt.toString())
                }.flowOn(Dispatchers.IO)
                .collect { _state.value = it }
        }
    }

    val state: StateFlow<StartingState> = this._state
}

data class StartingState(val ping: String)