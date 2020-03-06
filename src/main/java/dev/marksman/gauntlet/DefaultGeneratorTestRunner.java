package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import dev.marksman.kraftwerk.Seed;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static dev.marksman.gauntlet.GeneratedDataSet.generatedDataSet;

public class DefaultGeneratorTestRunner implements GeneratorTestRunner {
    private final Executor executor;

    private DefaultGeneratorTestRunner(Executor executor) {
        this.executor = executor;
    }

    @Override
    public <A> Report<A> run(GeneratorTest<A> testData) {
        // generate all inputs
        // if generator fails early:
        //    - test all anyway.  if falsified, fail as normal.
        //    - if cannot falsify, fail with SupplyFailure
        // if all inputs can be generated, submit test tasks to executor along with sample index
        // if failure is found, run tests on shrinks

        return null;
    }

    private <A> GeneratedDataSet<A> generateDataSet(Seed initialSeed,
                                                    ValueSupplier<A> valueSupplier,
                                                    int sampleCount) {
        Maybe<SupplyFailure> supplyFailure = nothing();
        ArrayList<A> values = new ArrayList<>(sampleCount);
        Seed state = initialSeed;
        for (int i = 0; i < sampleCount; i++) {
            GeneratorOutput<A> next = valueSupplier.getNext(state);
            supplyFailure = next.getValue()
                    .match(Maybe::just,
                            value -> {
                                values.add(value);
                                return nothing();
                            });
            state = next.getNextState();
            if (!supplyFailure.equals(nothing())) {
                break;
            }
        }
        return generatedDataSet(values, supplyFailure, state);
    }
}
