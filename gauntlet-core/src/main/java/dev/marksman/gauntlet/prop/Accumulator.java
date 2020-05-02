package dev.marksman.gauntlet.prop;

import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;

final class Accumulator {
    private final VectorBuilder<EvalFailure> failures;
    private final int successCount;

    private Accumulator(VectorBuilder<EvalFailure> failures, int successCount) {
        this.failures = failures;
        this.successCount = successCount;
    }

    static Accumulator accumulator() {
        return new Accumulator(VectorBuilder.builder(), 0);
    }

    Accumulator addSuccess() {
        return new Accumulator(failures, successCount + 1);
    }

    Accumulator addFailure(EvalFailure failure) {
        return new Accumulator(failures.add(failure), successCount);
    }

    Accumulator add(EvalResult result) {
        return result.match(__ -> addSuccess(), this::addFailure);
    }

    public VectorBuilder<EvalFailure> getFailures() {
        return this.failures;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

}
