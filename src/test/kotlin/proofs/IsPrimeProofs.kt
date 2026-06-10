package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import org.bmc4j.Verdict
import vendor.dynamicProgramming.isPrime

/**
 * Proofs over `Int.isPrime()` from TheAlgorithms/Kotlin:
 *   `this > 1 && (2..(this / 2)).all { this % it != 0 }`
 *
 * We check it against a trusted predicate over a bounded range. `n / 2` makes the loop length scale
 * with n, so a tight unwind bound forces a small range — primality over [0, 30] exercises the
 * interesting structure (2 and 3 take the empty-range branch; composites must be rejected).
 */
class IsPrimeProofs {

    /** Trusted reference: trial division up to n/2, written independently of the SUT. */
    private fun referenceIsPrime(n: Int): Boolean {
        if (n <= 1) return false
        var d = 2
        while (d <= n / 2) {
            if (n % d == 0) return false
            d++
        }
        return true
    }

    /**
     * CONTRACT: isPrime matches a trusted reference for every n in a bounded range. If this VERIFIES
     * the shipped predicate is correct on that whole range (not sampled). Range kept small so the
     * `2..n/2` loop unwinds within the bound.
     */
    @BmcProof(unwind = 18)
    fun isPrime_matches_reference_on_small_range() {
        val n = Bmc.anyInt(0, 30)
        Bmc.check(n.isPrime() == referenceIsPrime(n))
    }

    /** CONTRACT: nothing <= 1 is prime. */
    @BmcProof
    fun nothing_le_one_is_prime() {
        val n = Bmc.anyInt(Int.MIN_VALUE, 1)
        Bmc.check(!n.isPrime())
    }

    /**
     * Fail-on-purpose guard: there EXISTS a prime in [2,30] (so "no n in range is prime" is
     * refutable). Keeps the report honest — proves the predicate isn't trivially always-false.
     */
    @BmcProof(expect = Verdict.REFUTED, unwind = 18)
    fun some_number_in_range_is_prime() {
        val n = Bmc.anyInt(2, 30)
        Bmc.check(!n.isPrime())
    }
}
