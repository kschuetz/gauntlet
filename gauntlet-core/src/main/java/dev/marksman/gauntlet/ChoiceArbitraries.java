package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Unit;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.Generator;
import dev.marksman.kraftwerk.Generators;

import static com.jnape.palatable.lambda.adt.Maybe.just;
import static com.jnape.palatable.lambda.adt.Unit.UNIT;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkStrategies.shrinkChoice2;

final class ChoiceArbitraries {

    static <A, B> Arbitrary<Choice2<A, B>> arbitraryChoice2(Weighted<Arbitrary<A>> a,
                                                            Weighted<Arbitrary<B>> b) {
        if (a.getWeight() + b.getWeight() < 1) {
            throw new IllegalStateException("total of weights must be >= 1");
        }
        Generator<Choice2<Unit, Unit>> generateWhich = Generators.choiceBuilderValue(a.getWeight(), UNIT)
                .orValue(b.getWeight(), UNIT)
                .toGenerator();

        Arbitrary<A> arbitraryA = a.getValue();
        Arbitrary<B> arbitraryB = b.getValue();
        ShrinkStrategy<Choice2<A, B>> shrinkStrategy = shrinkChoice2(arbitraryA.getShrinkStrategy().orElse(ShrinkStrategy.none()),
                arbitraryB.getShrinkStrategy().orElse(ShrinkStrategy.none()));

        return ConcreteArbitrary.concreteArbitrary(parameters ->
                        new ChoiceSupply2<>(arbitraryA.createSupply(parameters),
                                arbitraryB.createSupply(parameters),
                                generateWhich.prepare(parameters)),
                just(shrinkStrategy),
                // TODO: prettyPrinter
                Object::toString);
    }
}
