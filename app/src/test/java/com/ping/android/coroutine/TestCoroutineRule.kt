package com.ping.android.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {
    var dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    var scope = TestCoroutineScope(dispatcher)
    companion object {
        fun rule() = TestCoroutineRule()
    }
    override fun apply(base: Statement, description: org.junit.runner.Description?) =
        object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                this@TestCoroutineRule.dispatcher = TestCoroutineDispatcher()
                this@TestCoroutineRule.scope = TestCoroutineScope(dispatcher)
                Dispatchers.setMain(dispatcher)
                base.evaluate()
                Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
                scope.cleanupTestCoroutines()
            }
        }
    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        this.scope.runBlockingTest {
            block()
        }
}