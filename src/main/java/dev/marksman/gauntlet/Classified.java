package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.ImmutableSet;
import dev.marksman.collectionviews.SetBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Classified<A> {
    @Getter
    A value;
    @Getter
    ImmutableSet<String> categories;

    public static <A> Classified<A> classified(ImmutableSet<String> categories, A value) {
        return new Classified<>(value, categories);
    }

    public static <A> Fn1<A, Classified<A>> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers) {
        return value -> applyClassifiers(classifiers, value);
    }

    public static <A> Classified<A> applyClassifiers(Iterable<Fn1<A, Set<String>>> classifiers, A value) {
        SetBuilder<String> builder = SetBuilder.builder();
        for (Fn1<A, Set<String>> classifier : classifiers) {
            builder = builder.addAll(classifier.apply(value));
        }

        return classified(builder.build(), value);
    }
}
