// Vendored from TheAlgorithms/Kotlin (MIT). See repo-root LICENSE.
//   source: src/main/kotlin/math/Factorial.kt
//   upstream commit: b913c1d85c972fd1e679c5d832d6458b21be8fb0
// Verbatim copy (package namespaced under `vendor.`). Analyzed by bmc4j as kotlinc compiles it.
package vendor.mathematics

import java.security.InvalidParameterException

/**
 * Calculates the factorial of a natural number greater than or equal to 0 recursively.
 * @param number The number of which to calculate the factorial.
 * @return The factorial of the number passed as parameter.
 */
fun getFactorial(number: Long): Long {
    if (number < 0L) {
        throw InvalidParameterException("The number of which to calculate the factorial must be greater or equal to zero.")
    } else return when (number) {
        0L -> 1
        1L -> number
        else -> number * getFactorial(number - 1)
    }
}
