package dev.marksman.gauntlet;

import dev.marksman.collectionviews.ImmutableSet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Classified<A> {
    @Getter
    A value;
    @Getter
    ImmutableSet<String> categories;

    public static <A> Classified<A> classified(ImmutableSet<String> categories, A value) {
        return new Classified<>(value, categories);
    }

}
