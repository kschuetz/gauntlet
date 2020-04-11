package dev.marksman.gauntlet.prop;

import dev.marksman.collectionviews.VectorBuilder;
import dev.marksman.gauntlet.EvalFailure;
import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
class Accumulator {
    VectorBuilder<EvalFailure> failures;
    int successCount;

    Accumulator addSuccess() {
        return new Accumulator(failures, successCount + 1);
    }

    Accumulator addFailure(EvalFailure failure) {
        return new Accumulator(failures.add(failure), successCount);
    }

    static Accumulator accumulator() {
        return new Accumulator(VectorBuilder.builder(), 0);
    }
}
