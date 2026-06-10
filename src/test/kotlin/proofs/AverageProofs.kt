package proofs

import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import vendor.math.average

/**
 * Proofs over `vendor.math.average(numbers: Array<Int>): Int` from TheAlgorithms/Kotlin.
 *
 * CONTRACT of an average: for a non-empty input, the result must lie within [min, max] of the
 * inputs. (Integer truncation toward zero is acceptable — truncation never leaves the [min,max]
 * band.) We use a size-2 symbolic array so the proof is a pure scalar question about `(a+b)/2`.
 */
class AverageProofs {

    /**
     * CONTRACT: average(a, b) lies between min(a,b) and max(a,b). True for real arithmetic and for
     * truncating integer division of a correctly-computed sum. This is a plain proof of the real
     * contract: it REFUTES, so this test FAILS — bmc4j produces inputs where the result escapes the
     * [min,max] band, which is only possible because the intermediate Int `sum` overflowed. The
     * witness (e.g. average of two positives coming out negative) is the defect.
     */
    @BmcProof
    fun int_average_lies_between_min_and_max() {
        val a = Bmc.anyInt()
        val b = Bmc.anyInt()
        val avg = average(arrayOf(a, b))
        val lo = if (a <= b) a else b
        val hi = if (a >= b) a else b
        Bmc.check(avg in lo..hi)
    }

    /**
     * Same contract, but inputs bounded so that a+b cannot overflow Int. Here the algorithm IS
     * correct, so this VERIFIES — pinning the defect to the unbounded-sum case (overflow), not the
     * truncation. Together with the proof above this localizes the bug precisely.
     */
    @BmcProof
    fun int_average_is_correct_when_sum_cannot_overflow() {
        val a = Bmc.anyInt(-1_000_000_000, 1_000_000_000)
        val b = Bmc.anyInt(-1_000_000_000, 1_000_000_000)
        val avg = average(arrayOf(a, b))
        val lo = if (a <= b) a else b
        val hi = if (a >= b) a else b
        Bmc.check(avg in lo..hi)
    }
}
