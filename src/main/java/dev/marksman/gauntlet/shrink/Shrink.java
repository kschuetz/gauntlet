package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.enhancediterables.ImmutableFiniteIterable.emptyImmutableFiniteIterable;

@FunctionalInterface
public interface Shrink<A> {
    ImmutableFiniteIterable<A> apply(A input);

    default Shrink<A> filter(Fn1<A, Boolean> predicate) {
        return new FilterShrink<>(this, predicate);
    }

    default <B> Shrink<B> convert(Iso<A, A, B, B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    default <B> Shrink<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        Shrink<A> orig = this;
        return input -> orig.apply(ba.apply(input)).fmap(ab);
    }

    static <A> Shrink<A> none() {
        return input -> emptyImmutableFiniteIterable();
    }
}
