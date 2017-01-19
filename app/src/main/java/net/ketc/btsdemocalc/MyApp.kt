package net.ketc.btsdemocalc

import android.app.Application
import android.os.AsyncTask
import android.os.Build
import net.orekyuu.bts4j.BugReportBuilder
import net.orekyuu.bts4j.BugReportServiceBuilder

class MyApp : Application() {
    val service = BugReportServiceBuilder("url", "token").build()!!
    override fun onCreate() {
        super.onCreate()
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val throwableStr = throwable.javaClass.simpleName
            val device = Build.DEVICE
            val androidCodeName = Build.VERSION.CODENAME
            val apiLevel = Build.VERSION.SDK_INT
            val build = BugReportBuilder()
                    .stacktrace(throwable.stackTrace.joinToString { "\n" })
                    .assignUserId(0)
                    .description("$throwableStr : $thread")
                    .title(throwableStr)
                    .version("version")
                    .runtimeInfo("device = $device os = $androidCodeName api = $apiLevel")
                    .log("log")
                    .build()
            AsyncTask.execute {
                try {
                    service.sendReport(build)
                } catch (e: Throwable) {
                } finally {
                    defaultUncaughtExceptionHandler.uncaughtException(thread, throwable)
                }
            }
        }
    }
}