// Vendored from TheAlgorithms/Kotlin (MIT). See repo-root LICENSE.
//   source: src/main/kotlin/dynamicProgramming/isPrime.kt
//   upstream commit: b913c1d85c972fd1e679c5d832d6458b21be8fb0
// Verbatim copy (package namespaced under `vendor.`). Analyzed by bmc4j as kotlinc compiles it.
package vendor.dynamicProgramming

/**
 * Extension function that checks if an integer is a prime number.
 * A prime number is a natural number greater than 1 that has no positive divisors other than 1 and itself.
 * @return true if the number is prime, false otherwise
 */
fun Int.isPrime() = this > 1 && (2..(this / 2)).all { this % it != 0 }
