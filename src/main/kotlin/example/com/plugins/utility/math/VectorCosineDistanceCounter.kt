package example.com.plugins.utility.math

import kotlin.math.pow
import kotlin.math.sqrt

fun countCosineDistance(vectorA: DoubleArray, vectorB: DoubleArray, scalarA: Double, scalarB: Double): Double {
    var dotProduct = 0.0

    for (i in vectorA.indices) {
        dotProduct += vectorA[i] * vectorB[i]
    }

    return 1 - (dotProduct / scalarA * scalarB)
}


fun DoubleArray.countScalar(): Double = this.reduce{ sum, x -> sum + x.pow(2.0) }.sqrt()


fun Double.sqrt() = sqrt(this)