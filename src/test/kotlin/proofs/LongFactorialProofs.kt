package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import vendor.mathematics.getFactorial

/**
 * Proofs over `vendor.mathematics.getFactorial(number: Long): Long` from TheAlgorithms/Kotlin —
 * the recursive, Long-returning factorial that guards `number < 0`.
 *
 * Unlike the Int `dynamicProgramming/factorial` (which overflows above 12 — see the factorial PR),
 * the Long version is correct over a Long-safe domain. These proofs VERIFY, so this PR's CI is
 * green: the tool proves correctness, not only finds bugs.
 */
class LongFactorialProofs {

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
