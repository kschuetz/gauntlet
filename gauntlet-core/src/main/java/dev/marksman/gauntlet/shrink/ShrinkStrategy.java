package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;

import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.shrink.ShrinkStrategyNone.shrinkNone;

@FunctionalInterface
public interface ShrinkStrategy<A> {

    static <A> ShrinkStrategy<A> none() {
        return shrinkNone();
    }

    ImmutableFiniteIterable<A> apply(A input);

    default ShrinkStrategy<A> filter(Fn1<? super A, Boolean> predicate) {
        return new FilterShrinkStrategy<>(this, Filter.filter(predicate));
    }

    default <B> ShrinkStrategy<B> convert(Iso<A, A, B, B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    default <B> ShrinkStrategy<B> convert(Fn1<A, B> ab, Fn1<B, A> ba) {
        ShrinkStrategy<A> orig = this;
        return input -> orig.apply(ba.apply(input)).fmap(ab);
    }
}
