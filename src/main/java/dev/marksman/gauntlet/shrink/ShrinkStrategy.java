package dev.marksman.gauntlet.shrink;

import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.builtin.fn1.CatMaybes;
import com.jnape.palatable.lambda.optics.Iso;
import com.jnape.palatable.lambda.optics.Prism;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;
import dev.marksman.gauntlet.filter.Filter;

import static com.jnape.palatable.lambda.optics.functions.Pre.pre;
import static com.jnape.palatable.lambda.optics.functions.Re.re;
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

    default <B> ShrinkStrategy<B> convert(Iso<? super A, ? extends A, ? extends B, ? super B> iso) {
        return convert(view(iso), view(iso.mirror()));
    }

    default <B> ShrinkStrategy<B> convert(Fn1<? super A, ? extends B> ab, Fn1<? super B, ? extends A> ba) {
        ShrinkStrategy<A> orig = this;
        return input -> orig.apply(ba.apply(input)).fmap(ab);
    }

    @SuppressWarnings("unchecked")
    default <B> ShrinkStrategy<B> convertWithPrism(Fn1<? super A, ? extends Maybe<? extends B>> ab, Fn1<? super B, ? extends A> ba) {
        ShrinkStrategy<A> orig = this;
        return input -> {
            // Need two following lines to keep Java 8 compiler happy
            ImmutableFiniteIterable<? extends Maybe<? extends B>> step1 = orig.apply(ba.apply(input)).fmap(ab);
            ImmutableFiniteIterable<Maybe<B>> maybeBs = (ImmutableFiniteIterable<Maybe<B>>) step1;
            return () -> CatMaybes.catMaybes(maybeBs).iterator();
        };
    }

    default <B> ShrinkStrategy<B> convertWithPrism(Prism<? super A, ? extends A, ? extends B, ? super B> prism) {
        return convertWithPrism(view(pre(prism)), view(re(prism)));
    }
}
