package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import org.bmc4j.Verdict
import vendor.math.median

/**
 * Proofs over `vendor.math.median(values: IntArray): Double` from TheAlgorithms/Kotlin.
 *
 * CONTRACT: the median of a set lies within [min, max] of its elements. For a 2-element array the
 * even-size branch computes `(values[1] + values[0]) / 2.0` — but the two Ints are added **before**
 * the widening to Double, so the sum can overflow Int. We use a 2-element symbolic array (the
 * smallest input that hits the even/halfway branch) to ask that scalar question directly.
 */
class MedianProofs {

    /**
     * CONTRACT: median(a, b) lies between min(a,b) and max(a,b). A REFUTED verdict witnesses inputs
     * whose Int sum overflows before the `/ 2.0`, yielding a median outside the data range.
     * (Expected REFUTED — same overflow class as average.)
     */
    @BmcProof(expect = Verdict.REFUTED, unwind = 4)
    fun median_of_two_lies_between_min_and_max() {
        val a = Bmc.anyInt()
        val b = Bmc.anyInt()
        val m = median(intArrayOf(a, b))
        val lo = if (a <= b) a else b
        val hi = if (a >= b) a else b
        Bmc.check(m >= lo.toDouble() && m <= hi.toDouble())
    }

    /**
     * Same contract, inputs bounded so a+b cannot overflow Int. Here the algorithm is correct, so
     * this VERIFIES — localizing the defect to the unbounded-sum (overflow) case.
     */
    @BmcProof(unwind = 4)
    fun median_of_two_is_correct_when_sum_cannot_overflow() {
        val a = Bmc.anyInt(-1_000_000_000, 1_000_000_000)
        val b = Bmc.anyInt(-1_000_000_000, 1_000_000_000)
        val m = median(intArrayOf(a, b))
        val lo = if (a <= b) a else b
        val hi = if (a >= b) a else b
        Bmc.check(m >= lo.toDouble() && m <= hi.toDouble())
    }
}
