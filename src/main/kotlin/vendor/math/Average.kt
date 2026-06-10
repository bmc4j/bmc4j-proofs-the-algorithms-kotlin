// Vendored from TheAlgorithms/Kotlin (MIT). See repo-root LICENSE.
//   source: src/main/kotlin/math/Average.kt
//   upstream commit: b913c1d85c972fd1e679c5d832d6458b21be8fb0
// Verbatim copy (package namespaced under `vendor.`). Analyzed by bmc4j as kotlinc compiles it.
package vendor.math


/**
 * Calculate the average of a list of Double
 *
 * @param numbers array to store numbers
 * @return average of given numbers
 */
fun average(numbers: Array<Double>): Double {
    var sum = 0.0
    for (number in numbers) {
        sum += number
    }
    return sum / numbers.size
}

/**
 * Calculate the average of a list of Int
 *
 * @param numbers array to store numbers
 * @return average of given numbers
 */
fun average(numbers: Array<Int>) : Int {
    var sum = 0
    for (number in numbers) {
        sum += number
    }
    return sum / numbers.size
}
