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

Two **genuine defects** were found and machine-witnessed (both silent 32-bit `Int` overflow in
functions whose contracts promise otherwise):

1. **`dynamicProgramming/factorial(n: Int)`** returns `Int`, so it overflows silently for `n >= 13`
   (e.g. `factorial(13)` returns `1932053504`; the correct value is `6227020800`). The docstring
   promises "factorial of the input number" with no caveat. The Long-returning `math/getFactorial`
   in the same repo does not have this problem on the same range.
2. **`math/average(numbers: Array<Int>)`** sums into an `Int`, so the sum overflows for large
   inputs. bmc4j's witness `average([1119760, 2146757126])` returns **`-1073545205`** — a *negative*
   "average" of two *positive* numbers (correct: `1073938443`), violating the basic
   `min <= average <= max` contract.

The proofs that pin these down expect `Verdict.REFUTED`; companion proofs over the overflow-free
sub-domain `VERIFY`, localizing each defect precisely to the overflow (the truncation/logic is
otherwise correct). `Int.isPrime()` was checked against an independent reference over `[0,30]` and
**verified** — no defect found there.

## Live proof reports (open PRs)

`main` is the scaffold (vendored sources + workflow) plus one trivial baseline proof. The proofs
themselves land via **pull requests** — each PR's CI posts a per-proof Expected/Actual/Counterexample
report as a PR comment, which is the shareable artifact. The PRs are intentionally left **open** as
the showcase:

- **Prove math/average and median** — the headline refutation:
  `average([1119760, 2146757126]) → -1073545205` (a negative average of two positives), plus the
  even-size median Int-overflow. — _PR link below_
- **Prove factorial** — the `Int` `factorial` silent-overflow refutation (`factorial(13)` etc.).
  — _PR link below_
- **Prove isPrime and the Long factorial** — the VERIFIED proofs (the tool proves things correct,
  not only finds bugs). — _PR link below_

<!-- PR-LINKS -->


## Run it

```
# CI / published snapshot:
./gradlew test

# Local fast loop against a publishToMavenLocal build (PowerShell needs --% for -P):
./gradlew --% test -PbmcVersion=0.0.1-local
```

CI runs every proof and posts a per-proof Expected/Actual/Counterexample report as a PR comment.
