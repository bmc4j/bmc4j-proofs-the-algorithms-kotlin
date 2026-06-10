package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Baseline smoke proof kept on `main`.
 *
 * The real findings live in the open pull requests (see README) — each PR adds its proof group and
 * the CI report comment shows that PR's verdicts. This single trivially-true proof keeps `main`'s
 * proof run non-empty and green, and demonstrates the @BmcProof pipeline end to end.
 *
 * CONTRACT: for any symbolic Int n, `n + 1 > n` UNLESS n is `Int.MAX_VALUE` (where +1 overflows).
 * Excluding that one point makes this hold for every input in the bound, so it VERIFIES.
 */
class BaselineProof {

    @BmcProof
    fun increment_grows_below_int_max() {
        val n = Bmc.anyInt(Int.MIN_VALUE, Int.MAX_VALUE - 1)
        Bmc.check(n + 1 > n)
    }
}
