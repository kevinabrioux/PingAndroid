package com.ping.android.repository

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.ping.android.application.PingApplication
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.flow
import java.lang.Thread.sleep
import java.net.InetAddress
import java.net.Socket
import java.net.URL
import javax.inject.Inject


@Module
@InstallIn(ActivityComponent::class)
class PingRepository @Inject constructor(@ActivityContext var context: Context) {

    fun ping(command: String) = flow {
        /*
        while (true) {
            val process: Process = Runtime.getRuntime().exec(command)
            val stdInput = BufferedReader(InputStreamReader(process.inputStream))
            var s: String? = ""
            var res = ""
            do {
                s = stdInput.readLine()
                res += s + "\n"
            } while (s != null)
            process.destroy()
            Log.d("PING", res)
            emit(res)
            sleep(500)
        }*/
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

