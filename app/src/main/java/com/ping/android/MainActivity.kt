package com.ping.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.ping.android.architecture.ViewRenderer
import com.ping.android.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity : AppCompatActivity(), ViewRenderer<StartingState> {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            MainActivityViewModelFactory(this)
        ).get(MainActivityViewModel::class.java)
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(this.binding.root)
        this.viewModel.state?.observe(this, { state -> this.render(state) })
    }

    override fun render(state: StartingState) {
        with(state) {
            this@MainActivity.binding?.ping?.text = this.ping
        }
    }


}