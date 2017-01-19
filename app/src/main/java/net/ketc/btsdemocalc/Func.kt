package net.ketc.btsdemocalc

sealed class Func {
    abstract fun run(x: Double): Double

    class Sqrt : Func() {
        override fun run(x: Double): Double = Math.sqrt(x)
    }

    class Tax : Func() {
        override fun run(x: Double): Double = x * 1.08
    }
}
