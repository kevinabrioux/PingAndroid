package com.ping.android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ping.android.R
import com.ping.android.architecture.ViewRenderer
import com.ping.android.databinding.ActivityMainBinding
import com.ping.android.databinding.FragmentPingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.subscribeOn
import javax.inject.Inject

@AndroidEntryPoint
class PingFragment : Fragment(), ViewRenderer<StartingState> {

    private val viewModel: PingFragmentViewModel by viewModels()

    private var binding: FragmentPingBinding? = null

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentPingBinding.inflate(layoutInflater, container, false)
        return this.binding?.root
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { render(it) }
        }
    }

    override fun render(state: StartingState) {
        with(state) {
            this@PingFragment.binding?.ping?.text = this.ping
        }
    }
}