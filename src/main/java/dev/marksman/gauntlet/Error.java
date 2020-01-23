package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Maybe;
import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
public class Error {
    private final Maybe<Name> propertyName;
    private final Throwable error;

    public static Error error(Maybe<Name> propertyName, Throwable error) {
        return new Error(propertyName, error);
    }
}
