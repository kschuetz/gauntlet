package software.kes.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.kraftwerk.GenerateFn;
import software.kes.kraftwerk.Weighted;

import java.util.TreeMap;

import static software.kes.kraftwerk.Generators.generateLongIndex;

final class FrequencyMapArbitrary {

    private FrequencyMapArbitrary() {
    }

    @SafeVarargs
    static <A> Arbitrary<A> frequencies(Weighted<Arbitrary<A>> first, Weighted<Arbitrary<A>>... rest) {
        if (rest.length == 0) {
            return first.getValue();
        }
        return frequencyMapImpl(NonEmptyVector.of(first, rest));
    }

    @SafeVarargs
    static <A> Arbitrary<A> frequencies(Arbitrary<A> first, Arbitrary<A>... rest) {
        if (rest.length == 0) {
            return first;
        }
        return frequencyMapImpl(NonEmptyVector.of(first, rest).fmap(Arbitrary::weighted));
    }

    private static <A> Arbitrary<A> frequencyMapImpl(ImmutableNonEmptyVector<Weighted<Arbitrary<A>>> arbitraries) {
        Arbitrary<A> first = arbitraries.head().getValue();
        Maybe<ShrinkStrategy<A>> shrinkStrategy = first.getShrinkStrategy();
        PrettyPrinter<A> prettyPrinter = first.getPrettyPrinter();
        return Arbitrary.arbitrary(generatorParameters -> {
            ImmutableNonEmptyVector<Weighted<Supply<A>>> sources = arbitraries.fmap(source -> source.fmap(s -> s.createSupply(generatorParameters)));
            TreeMap<Long, Supply<A>> tree = new TreeMap<>();
            long total = 0;
            for (Weighted<Supply<A>> source : sources) {
                total += source.getWeight();
                tree.put(total, source.getValue());
            }
            GenerateFn<Long> generateWhich = generateLongIndex(total).createGenerateFn(generatorParameters);
            return new FrequencyMapSupply<>(generateWhich, tree);

        }, shrinkStrategy, prettyPrinter);
    }
}
