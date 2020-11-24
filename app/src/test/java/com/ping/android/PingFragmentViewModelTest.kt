package com.ping.android

import com.ping.android.coroutine.TestCoroutineRule
import com.ping.android.fragment.PingFragmentViewModel
import com.ping.android.repository.PingRepository
import com.ping.android.repository.PingRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*

@ExperimentalCoroutinesApi
class PingFragmentViewModelTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() = Dispatchers.setMain(dispatcher)
    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun test() = runBlockingTest {
        val vm = PingFragmentViewModel(PingRepoMock())
        Assert.assertEquals("", "", vm.state.single().ping)
    }

    class PingRepoMock : PingRepositoryInterface {
        override suspend fun ping(command: String): Flow<PingRepository.Ping?> = flow { PingRepository.Ping() }
    }
}