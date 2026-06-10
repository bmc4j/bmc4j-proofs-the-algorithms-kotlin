package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import org.bmc4j.Verdict
import vendor.dynamicProgramming.factorial
import vendor.mathematics.getFactorial

/**
 * Proofs over the two vendored factorial implementations from TheAlgorithms/Kotlin.
 *
 *  - `vendor.dynamicProgramming.factorial(n: Int)`  — tail-recursive, returns **Int**.
 *  - `vendor.mathematics.getFactorial(number: Long)` — recursive, returns **Long**, guards `< 0`.
 *
 * The contract under test is the defining recurrence/monotonicity of factorial over a SMALL bounded
 * domain (so JBMC unwinding terminates): for natural `n` in range, `n! >= n` and `n! >= 1`, and the
 * factorial of a positive number is strictly positive. These hold mathematically for all naturals.
 */
class FactorialProofs {

    // ---- Int version (suspect) --------------------------------------------------------------

    /**
     * CONTRACT: factorial of any natural number is positive (n! >= 1 for n >= 0). This is true in
     * exact arithmetic. We bound n to [0, 20] so the tail recursion unwinds finitely. If bmc4j
     * REFUTES this, it has found an input where the SHIPPED Int implementation returns a
     * non-positive value — i.e. silent 32-bit overflow. (Expected REFUTED — see report.)
     */
    @BmcProof(expect = Verdict.REFUTED, unwind = 22)
    fun int_factorial_is_always_positive_for_naturals() {
        val n = Bmc.anyInt(0, 20)
        Bmc.check(factorial(n) >= 1)
    }

    /**
     * Cross-check the Int implementation against the Long one on a domain where Long does NOT
     * overflow (0..20, since 20! < Long.MAX). They must agree for every n if the Int version is
     * correct. A REFUTED verdict is a concrete n where Int factorial != true factorial.
     */
    @BmcProof(expect = Verdict.REFUTED, unwind = 22)
    fun int_factorial_agrees_with_long_factorial() {
        val n = Bmc.anyInt(0, 20)
        Bmc.check(factorial(n).toLong() == getFactorial(n.toLong()))
    }

    /**
     * Tightly bounded sanity floor: on 0..12 the Int result still fits (12! = 479_001_600 < 2^31),
     * so the implementation IS correct there. This proof should VERIFY — it pins down exactly where
     * the algorithm is sound and proves the overflow is a >12 phenomenon, not an everywhere bug.
     */
    @BmcProof(unwind = 14)
    fun int_factorial_is_correct_up_to_12() {
        val n = Bmc.anyInt(0, 12)
        Bmc.check(factorial(n).toLong() == getFactorial(n.toLong()))
    }

    // ---- Long version (the guarded one) -----------------------------------------------------

    /** CONTRACT: getFactorial is positive for naturals; bounded to a Long-safe domain. */
    @BmcProof(unwind = 22)
    fun long_factorial_is_always_positive_for_naturals() {
        val n = Bmc.anyLong(0, 20)
        Bmc.check(getFactorial(n) >= 1L)
    }

    /** CONTRACT: n! >= n for n >= 1 (monotonic floor), Long version, Long-safe domain. */
    @BmcProof(unwind = 22)
    fun long_factorial_dominates_its_argument() {
        val n = Bmc.anyLong(1, 20)
        Bmc.check(getFactorial(n) >= n)
    }
}
