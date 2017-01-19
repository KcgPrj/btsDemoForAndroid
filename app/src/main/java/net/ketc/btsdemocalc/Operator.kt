package net.ketc.btsdemocalc

/**
 * sealedクラス使ってみたかっただけ…
 */
sealed class Operator {
    abstract fun run(x: Double, y: Double): Double

    class Add : Operator() {
        override fun run(x: Double, y: Double) = x + y
    }

    class Sub : Operator() {
        override fun run(x: Double, y: Double) = x - y
    }

    class Multi : Operator() {
        override fun run(x: Double, y: Double) = x * y
    }

    class Div : Operator() {
        override fun run(x: Double, y: Double) = x / y
    }
}