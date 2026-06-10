package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import vendor.dynamicProgramming.factorial
import vendor.mathematics.getFactorial

/**
 * Proofs over the two vendored factorial implementations from TheAlgorithms/Kotlin.
 *
 *  - `vendor.dynamicProgramming.factorial(n: Int)`  — tail-recursive, returns **Int**.
 *  - `vendor.mathematics.getFactorial(number: Long)` — recursive, returns **Long**, guards `< 0`.
 *
 * The contract under test is the defining positivity/agreement of factorial over a SMALL bounded
 * domain (so JBMC unwinding terminates): for natural `n` in range, `n! >= 1`, and the Int result
 * must equal the true (Long) factorial. These hold mathematically for all naturals.
 */
class FactorialProofs {

    // ---- Int version (the defect) -----------------------------------------------------------

    /**
     * CONTRACT: factorial of any natural number is positive (n! >= 1 for n >= 0). True in exact
     * arithmetic. We bound n to [0, 20] so the tail recursion unwinds finitely. This is a plain
     * proof of the real contract: it REFUTES, so this test FAILS — bmc4j finds an n where the
     * shipped Int implementation returns a non-positive value, i.e. silent 32-bit overflow (it
     * first breaks at n >= 13, where 13! exceeds Int.MAX).
     */
    @BmcProof(unwind = 22)
    fun int_factorial_is_always_positive_for_naturals() {
        val n = Bmc.anyInt(0, 20)
        Bmc.check(factorial(n) >= 1)
    }

    /**
     * Cross-check the Int implementation against the Long one on a domain where Long does NOT
     * overflow (0..20, since 20! < Long.MAX). They must agree for every n if the Int version is
     * correct. Plain proof of the real contract: it REFUTES, so this test FAILS, with a concrete n
     * where the Int factorial disagrees with the true factorial.
     */
    @BmcProof(unwind = 22)
    fun int_factorial_agrees_with_long_factorial() {
        val n = Bmc.anyInt(0, 20)
        Bmc.check(factorial(n).toLong() == getFactorial(n.toLong()))
    }

    /**
     * Tightly bounded sanity floor: on 0..12 the Int result still fits (12! = 479_001_600 < 2^31),
     * so the implementation IS correct there. This proof VERIFIES — it pins down exactly where the
     * algorithm is sound and shows the overflow is a >12 phenomenon, not an everywhere bug.
     */
    @BmcProof(unwind = 14)
    fun int_factorial_is_correct_up_to_12() {
        val n = Bmc.anyInt(0, 12)
        Bmc.check(factorial(n).toLong() == getFactorial(n.toLong()))
    }

    // ---- Long version (the guarded one) -----------------------------------------------------

    /** CONTRACT: getFactorial is positive for naturals; bounded to a Long-safe domain. VERIFIES. */
    @BmcProof(unwind = 22)
    fun long_factorial_is_always_positive_for_naturals() {
        val n = Bmc.anyLong(0, 20)
        Bmc.check(getFactorial(n) >= 1L)
    }

    /** CONTRACT: n! >= n for n >= 1 (monotonic floor), Long version, Long-safe domain. VERIFIES. */
    @BmcProof(unwind = 22)
    fun long_factorial_dominates_its_argument() {
        val n = Bmc.anyLong(1, 20)
        Bmc.check(getFactorial(n) >= n)
    }
}
