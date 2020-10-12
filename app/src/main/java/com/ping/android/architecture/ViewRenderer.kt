package com.ping.android.architecture

interface ViewRenderer<State> {
    fun render(state: State)
}