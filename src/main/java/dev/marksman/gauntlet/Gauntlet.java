package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import dev.marksman.collectionviews.Vector;

import static dev.marksman.gauntlet.ExistentialDomainTestBuilder.existentialDomainTestBuilder;
import static dev.marksman.gauntlet.GeneratorTestBuilder1.generatorTestBuilder1;
import static dev.marksman.gauntlet.GeneratorTestBuilder2.generatorTestBuilder2;
import static dev.marksman.gauntlet.GeneratorTestBuilder3.generatorTestBuilder3;
import static dev.marksman.gauntlet.UniversalDomainTestBuilder.universalDomainTestBuilder;

public final class Gauntlet {

    public static int DEFAULT_SAMPLE_COUNT = 100;

    private Gauntlet() {

    }

    public static <A> GeneratorTestBuilder<A> all(GenerationStrategy<A> generator) {
        return generatorTestBuilder1(generator, DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B> GeneratorTestBuilder<Tuple2<A, B>> all(GenerationStrategy<A> generatorA,
                                                                GenerationStrategy<B> generatorB) {
        return generatorTestBuilder2(generatorA, generatorB,
                DEFAULT_SAMPLE_COUNT);
    }

    public static <A, B, C> GeneratorTestBuilder<Tuple3<A, B, C>> all(GenerationStrategy<A> generatorA,
                                                                      GenerationStrategy<B> generatorB,
                                                                      GenerationStrategy<C> generatorC) {
        return generatorTestBuilder3(generatorA, generatorB, generatorC,
                DEFAULT_SAMPLE_COUNT);
    }

//    public static <A, B, C, D> GeneratorTestBuilder<Tuple4<A, B, C, D>> all(GenerationStrategy<A> generatorA,
//                                                                            GenerationStrategy<B> generatorB,
//                                                                            GenerationStrategy<C> generatorC,
//                                                                            GenerationStrategy<D> generatorD) {
//        return generatorTestBuilder1(Generators.tupled(generatorA, generatorB, generatorC, generatorD),
//                DEFAULT_SAMPLE_COUNT);
//    }
//
//    public static <A, B, C, D, E> GeneratorTestBuilder<Tuple5<A, B, C, D, E>> all(GenerationStrategy<A> generatorA,
//                                                                                  GenerationStrategy<B> generatorB,
//                                                                                  GenerationStrategy<C> generatorC,
//                                                                                  GenerationStrategy<D> generatorD,
//                                                                                  GenerationStrategy<E> generatorE) {
//        return generatorTestBuilder1(Generators.tupled(generatorA, generatorB, generatorC, generatorD, generatorE),
//                DEFAULT_SAMPLE_COUNT);
//    }

    public static <A> DomainTestBuilder<A> all(Iterable<A> domain) {
        return universalDomainTestBuilder(Vector.copyFrom(domain));
    }

    public static <A> DomainTestBuilder<A> some(Iterable<A> domain) {
        return existentialDomainTestBuilder(domain);
    }

}
