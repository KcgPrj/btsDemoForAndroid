package net.ketc.btsdemocalc

import android.app.Application
import android.os.AsyncTask
import android.os.Build
import net.orekyuu.bts4j.BugReportBuilder
import net.orekyuu.bts4j.BugReportServiceBuilder

class MyApp : Application() {
    val service = BugReportServiceBuilder("http://192.168.10.5:18080", "06a2736a-31ca-4450-8b25-5A3AD6A724D8").build()!!
    override fun onCreate() {
        super.onCreate()
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val throwableStr = throwable.javaClass.simpleName
            val device = Build.DEVICE
            val apiLevel = Build.VERSION.SDK_INT
            val build = BugReportBuilder()
                    .stacktrace(throwableStr + "\n" + throwable.stackTrace.joinToString(separator = "\n") { "    at $it" })
                    .description("$throwableStr : $thread")
                    .title(throwableStr)
                    .version(BuildConfig.VERSION_NAME)
                    .runtimeInfo("device = $device, api = $apiLevel")
                    .log("it is log")
                    .build()
            AsyncTask.execute {
                try {
                    service.sendReport(build)
                } catch (e: Throwable) {
                    e.printStackTrace()
                } finally {
                    defaultUncaughtExceptionHandler.uncaughtException(thread, throwable)
                }
            }
        }
    }
}