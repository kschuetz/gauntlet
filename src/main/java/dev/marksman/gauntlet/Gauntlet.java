package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.collectionviews.Vector;

import java.util.concurrent.atomic.AtomicReference;

import static dev.marksman.gauntlet.ExistentialDomainTestBuilder.existentialDomainTestBuilder;
import static dev.marksman.gauntlet.GeneratorTestBuilder1.generatorTestBuilder1;
import static dev.marksman.gauntlet.SimpleGeneratorTestRunner.simpleGeneratorTestRunner;
import static dev.marksman.gauntlet.UniversalDomainTestBuilder.universalDomainTestBuilder;

public final class Gauntlet {

    public static int DEFAULT_SAMPLE_COUNT = 100;
    public static int DEFAULT_MAX_DISCARDS = 100;

    private static AtomicReference<GeneratorTestRunner> defaultGeneratorTestRunner = new AtomicReference<>(simpleGeneratorTestRunner());

    private Gauntlet() {

    }

    public static GeneratorTestRunner defaultGeneratorTestRunner() {
        return defaultGeneratorTestRunner.get();
    }

    public static void setDefaultGeneratorTestRunner(GeneratorTestRunner generatorTestRunner) {
        defaultGeneratorTestRunner.set(generatorTestRunner);
    }

    public static <A> GeneratorTestBuilder<A> all(Arbitrary<A> generator) {
        return generatorTestBuilder1(generator, DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(Arbitrary<A> generatorA,
                                                                Arbitrary<B> generatorB) {
        return generatorTestBuilder1(CompositeArbitraries.combine(generatorA, generatorB),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(Arbitrary<A> generatorA,
                                                                      Arbitrary<B> generatorB,
                                                                      Arbitrary<C> generatorC) {
        return generatorTestBuilder1(CompositeArbitraries.combine(generatorA, generatorB, generatorC),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A> DomainTestBuilder<A> all(Iterable<A> domain) {
        return universalDomainTestBuilder(Vector.copyFrom(domain));
    }

    public static <A> DomainTestBuilder<A> some(Iterable<A> domain) {
        return existentialDomainTestBuilder(domain);
    }

}
