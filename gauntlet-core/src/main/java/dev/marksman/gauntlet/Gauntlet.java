package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.collectionviews.Vector;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static dev.marksman.gauntlet.ConcreteEnvironment.concreteEnvironment;
import static dev.marksman.gauntlet.ExistentialDomainTestBuilder.existentialDomainTestBuilder;
import static dev.marksman.gauntlet.GeneratorParameters.defaultGeneratorParameters;
import static dev.marksman.gauntlet.GeneratorTestBuilder1.generatorTestBuilder1;
import static dev.marksman.gauntlet.UniversalDomainTestBuilder.universalDomainTestBuilder;
import static java.util.concurrent.Executors.newFixedThreadPool;

public final class Gauntlet {

    public static int DEFAULT_SAMPLE_COUNT = 100;
    public static int DEFAULT_MAX_DISCARDS = 100;

    private static Executor executor = newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static AtomicReference<GauntletEnvironment> environment = new AtomicReference<>(
            concreteEnvironment(executor,
                    DefaultGeneratorTestRunner.defaultGeneratorTestRunner(defaultGeneratorParameters()),
                    DefaultReporter.defaultReporter()));

    private Gauntlet() {

    }

    public static <A> GeneratorTestBuilder<A> all(Arbitrary<A> generator) {
        return generatorTestBuilder1(environment.get(), generator, DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(Arbitrary<A> generatorA,
                                                                Arbitrary<B> generatorB) {
        return generatorTestBuilder1(environment.get(), CompositeArbitraries.combine(generatorA, generatorB),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(Arbitrary<A> generatorA,
                                                                      Arbitrary<B> generatorB,
                                                                      Arbitrary<C> generatorC) {
        return generatorTestBuilder1(environment.get(), CompositeArbitraries.combine(generatorA, generatorB, generatorC),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A> DomainTestBuilder<A> all(Iterable<A> domain) {
        return universalDomainTestBuilder(Vector.copyFrom(domain));
    }

    public static <A> DomainTestBuilder<A> some(Iterable<A> domain) {
        return existentialDomainTestBuilder(domain);
    }

}
