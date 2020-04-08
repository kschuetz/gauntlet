package dev.marksman.gauntlet;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;


@Value
@AllArgsConstructor(access = PRIVATE)
public class FailedSample<A> {
    Failure failure;
    A sample;

    public static <A> FailedSample<A> failedSample(Failure failure, A sample) {
        return new FailedSample<>(failure, sample);
    }
}
