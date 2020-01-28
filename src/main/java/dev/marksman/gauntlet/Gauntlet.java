package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import dev.marksman.collectionviews.Vector;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;

import static dev.marksman.gauntlet.ExistentialDomainTestBuilder.existentialDomainTestBuilder;
import static dev.marksman.gauntlet.GeneratorTestBuilderImpl.generatorTestBuilder;
import static dev.marksman.gauntlet.UniversalDomainTestBuilder.universalDomainTestBuilder;

public final class Gauntlet {

    public static int DEFAULT_SAMPLE_COUNT = 100;

    private Gauntlet() {

    }

    public static <A> GeneratorTestBuilder<A> all(Generator<A> generator) {
        return generatorTestBuilder(generator, DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(Generator<A> generatorA,
                                                                Generator<B> generatorB) {
        return generatorTestBuilder(Generators.tupled(generatorA, generatorB),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(Generator<A> generatorA,
                                                                      Generator<B> generatorB,
                                                                      Generator<C> generatorC) {
        return generatorTestBuilder(Generators.tupled(generatorA, generatorB, generatorC),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C, D> GeneratorTestBuilder<Tuple4<A, B, C, D>> all(Generator<A> generatorA,
                                                                            Generator<B> generatorB,
                                                                            Generator<C> generatorC,
                                                                            Generator<D> generatorD) {
        return generatorTestBuilder(Generators.tupled(generatorA, generatorB, generatorC, generatorD),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C, D, E> GeneratorTestBuilder<Tuple5<A, B, C, D, E>> all(Generator<A> generatorA,
                                                                                  Generator<B> generatorB,
                                                                                  Generator<C> generatorC,
                                                                                  Generator<D> generatorD,
                                                                                  Generator<E> generatorE) {
        return generatorTestBuilder(Generators.tupled(generatorA, generatorB, generatorC, generatorD, generatorE),
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A> DomainTestBuilder<A> all(Iterable<A> domain) {
        return universalDomainTestBuilder(Vector.copyFrom(domain));
    }

    public static <A> DomainTestBuilder<A> some(Iterable<A> domain) {
        return existentialDomainTestBuilder(domain);
    }

}
