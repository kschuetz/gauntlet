package dev.marksman.gauntlet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class AscribedFailure<A> {
    int sampleIndex;
    A input;
    Failure failure;

    public static <A> AscribedFailure<A> ascribedFailure(int sampleIndex, A input, Failure failure) {
        return new AscribedFailure<>(sampleIndex, input, failure);
    }
}
