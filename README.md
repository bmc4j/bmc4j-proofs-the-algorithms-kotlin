# bmc4j proofs — TheAlgorithms/Kotlin

[bmc4j](https://github.com/bmc4j/bmc4j) bounded-model-checking proofs over a **vendored, attributed**
copy of selected scalar/arithmetic algorithms from
[TheAlgorithms/Kotlin](https://github.com/TheAlgorithms/Kotlin) (MIT-licensed; see `LICENSE`).

Each algorithm is analyzed **as it ships** — bmc4j checks the bytecode kotlinc actually produces.
Every `@BmcProof` is a JUnit 5 test asserting a real contract over *symbolic* inputs (`Bmc.anyInt()`
/ `anyLong()`), so a passing proof holds for **every** input in the bound, not a sampled few.

## Vendoring

Files under `src/main/kotlin/vendor/` are verbatim copies of TheAlgorithms/Kotlin sources (only the
package was namespaced under `vendor.` to let two same-named `Factorial.kt` files coexist). Every
file carries a provenance header: upstream source path + commit
`b913c1d85c972fd1e679c5d832d6458b21be8fb0`.

## Findings

The proofs surface two defects, both **32-bit `Int` overflow reachable only near `Int.MAX`** — not
everyday inputs. They are real (the contract is violated and a concrete counterexample is produced),
but the framing here is deliberately plain: each is an Int-overflow edge in a function whose
contract promises otherwise.

1. **`dynamicProgramming/factorial(n: Int)`** returns `Int`, so it overflows for `n >= 13`
   (`factorial(13)` returns `1932053504`; the correct value is `6227020800`). The contract under
   test — `n! > 0` for natural `n`, and agreement with the Long factorial on a Long-safe range —
   genuinely refutes. The Long-returning `math/getFactorial` does not have this problem on the same
   range.
2. **`math/average(numbers: Array<Int>)`** sums into an `Int`, so the sum overflows for large
   inputs, violating the basic `min <= average <= max` contract — e.g. two equal large negatives
   whose Int sum wraps positive yield an "average" above the maximum. (The exact witness is in the
   PR's report comment.) `math/median` has the same Int-sum overflow in its even-size branch.

The bug-demonstrating proofs are **plain `@BmcProof`s asserting the real contract** — they REFUTE,
so the test FAILS and that PR's CI goes **red**, with the counterexample in the posted report. That
red check plus the counterexample *is* the "this code is broken" demonstration. Companion proofs
over the overflow-free sub-domain VERIFY, localizing each defect to the overflow. `Int.isPrime()`
was checked against an independent reference over `[0,30]` and **verified** — no defect found there.

## Live proof reports (open PRs)

`main` is the scaffold (vendored sources + workflow) plus one trivial baseline proof. The proofs
themselves land via **pull requests** — each PR's CI posts a per-proof Expected/Actual/Counterexample
report as a PR comment, which is the shareable artifact. The PRs are intentionally left **open** as
the showcase. The two bug PRs have a **red** CI check (the asserted contract genuinely refutes); the
isPrime PR is green:

- **[Prove math/average and median](https://github.com/bmc4j/bmc4j-proofs-the-algorithms-kotlin/pull/1)**
  — plain proofs of `min <= average <= max` and the median range contract; both REFUTE (the Int sum
  overflows, putting the result outside `[min,max]`). Each ships with a bounded-sum companion that
  VERIFIES. CI check: **red**.
- **[Prove factorial](https://github.com/bmc4j/bmc4j-proofs-the-algorithms-kotlin/pull/2)** — plain
  proof that `factorial(n) > 0` / agrees with the Long factorial; REFUTES for `n >= 13`. Ships with a
  "correct up to 12" companion that VERIFIES. CI check: **red**.
- **[Prove isPrime and the Long factorial](https://github.com/bmc4j/bmc4j-proofs-the-algorithms-kotlin/pull/3)**
  — all VERIFIED (the tool proves things correct, not only finds bugs). CI check: **green**.


## Run it

```
# CI / published snapshot:
./gradlew test

# Local fast loop against a publishToMavenLocal build (PowerShell needs --% for -P):
./gradlew --% test -PbmcVersion=0.0.1-local
```

CI runs every proof and posts a per-proof Expected/Actual/Counterexample report as a PR comment.
