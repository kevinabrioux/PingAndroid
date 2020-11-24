package com.ping.android.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.ping.android.application.PingApplication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Thread.sleep
import java.net.InetAddress
import java.net.Socket
import java.net.URL
import javax.inject.Inject

interface PingRepositoryInterface {
    suspend fun ping(command: String) : Flow<PingRepository.Ping?>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
interface PingModule {
    @Binds
    fun bindPingRepository(impl: PingRepository) : PingRepositoryInterface
}

class PingRepository @Inject constructor(@ApplicationContext var context: Context): PingRepositoryInterface {

    override suspend fun ping(command: String) = flow {
        while (true) {
            val ping = ping(URL("https://www.google.com:443/"), context)
            emit(ping)
            Log.d("PING", ping.toString())
            sleep(500)
        }
    }

    class Ping {
        var net: String? = "NO_CONNECTION"
        var host = ""
        var ip = ""
        var dns = Int.MAX_VALUE
        var cnt = Int.MAX_VALUE
    }

    private fun ping(url: URL, ctx: Context): Ping? {
        val r = Ping()
        if (isNetworkConnected(ctx)) {
            r.net = getNetworkType(ctx)
            try {
                val start = System.currentTimeMillis()
                val hostAddress: String = InetAddress.getByName(url.host).hostAddress
                val dnsResolved = System.currentTimeMillis()
                val socket = Socket(hostAddress, url.port)
                socket.close()
                val probeFinish = System.currentTimeMillis()
                r.dns = (dnsResolved - start).toInt()
                r.cnt = (probeFinish - dnsResolved).toInt()
                r.host = url.host
                r.ip = hostAddress
            } catch (ex: Exception) {
                Log.e("PING", ex.toString())
            }
        }
        return r
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun getNetworkType(context: Context): String? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.typeName
    }
}

