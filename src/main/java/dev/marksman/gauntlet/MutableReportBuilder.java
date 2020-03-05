package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.util.Set;

final class MutableReportBuilder<A> {
    Report<A> build() {
        throw new UnsupportedOperationException("TODO");
    }

    void setClassifiers(ImmutableFiniteIterable<Fn1<A, Set<String>>> classifiers) {

    }

    void setInitialSeed(long value) {

    }

    void supplyFailure(SupplyFailure supplyFailure) {

    }

    void failWithError(A input, Throwable error) {

    }

    void markSuccess(A input) {

    }

    boolean shouldContinue() {
        return true;
    }

    static <A> MutableReportBuilder<A> mutableReportBuilder() {
        return new MutableReportBuilder<>();
    }
}
