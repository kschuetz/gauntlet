package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class RefinedCounterexample<A> {
    Counterexample<A> counterexample;
    int shrinkCount;

    public static <A> RefinedCounterexample<A> refinedCounterexample(Counterexample<A> counterexample,
                                                                     int shrinkCount) {
        return new RefinedCounterexample<>(counterexample, shrinkCount);
    }
}
