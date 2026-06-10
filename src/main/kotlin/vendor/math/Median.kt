// Vendored from TheAlgorithms/Kotlin (MIT). See repo-root LICENSE.
//   source: src/main/kotlin/math/Median.kt
//   upstream commit: b913c1d85c972fd1e679c5d832d6458b21be8fb0
// Verbatim copy (package namespaced under `vendor.`). Analyzed by bmc4j as kotlinc compiles it.
package vendor.math

import java.util.Arrays.sort

/**
 * Calculates the median of an array of Int
 *
 * @param values is an array of Int
 * @return the middle number of the array
 */
fun median(values: IntArray): Double {
    sort(values)
    return when {
        values.size % 2 == 0 -> getHalfwayBetweenMiddleValues(values)
        else -> getMiddleValue(values)
    }
}

/**
 * Calculates the middle number of an array when the size is an even number
 *
 * @param values is an array of Int
 * @return the middle number of the array
 */
private fun getHalfwayBetweenMiddleValues(values: IntArray): Double {
    val arraySize = values.size
    val sumOfMiddleValues = (values[arraySize / 2] + values[(arraySize / 2) - 1 ])
    return sumOfMiddleValues / 2.0
}

/**
 * Calculates the middle number of an array when the size is an odd number
 *
 * @param values is an array of Int
 * @return the middle number of the array
 */
private fun getMiddleValue(values: IntArray): Double {
    return values[values.size / 2].toDouble()
}
