package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;


@Value
@AllArgsConstructor(access = PRIVATE)
public class Counterexample<A> {
    EvalFailure failure;
    A sample;

    public static <A> Counterexample<A> counterexample(EvalFailure failure, A sample) {
        return new Counterexample<>(failure, sample);
    }
}
