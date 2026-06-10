// Vendored from TheAlgorithms/Kotlin (MIT). See repo-root LICENSE.
//   source: src/main/kotlin/dynamicProgramming/Factorial.kt
//   upstream commit: b913c1d85c972fd1e679c5d832d6458b21be8fb0
// Verbatim copy (only the package was namespaced under `vendor.` to avoid colliding with the
// other vendored Factorial). Analyzed by bmc4j exactly as kotlinc compiles it.
package vendor.dynamicProgramming

/**
 * This is a tail-recursive implementation of factorial calculation.
 * @param n - the number to calculate factorial for
 * @param accumulator - accumulates the factorial value (default is 1)
 * @return factorial of the input number
 */
tailrec fun factorial(n: Int, accumulator: Int = 1): Int {
    if (n == 0) {
        return accumulator
    }

    return factorial(n - 1, accumulator * n)
}
