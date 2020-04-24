package dev.marksman.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.optics.Iso;
import dev.marksman.collectionviews.ImmutableNonEmptyVector;
import dev.marksman.collectionviews.ImmutableVector;
import dev.marksman.collectionviews.Vector;
import dev.marksman.gauntlet.shrink.ShrinkResult;
import dev.marksman.gauntlet.shrink.ShrinkStrategy;
import dev.marksman.kraftwerk.constraints.ByteRange;
import dev.marksman.kraftwerk.constraints.IntRange;
import dev.marksman.kraftwerk.constraints.LongRange;
import dev.marksman.kraftwerk.constraints.ShortRange;

import java.util.ArrayList;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkCollection.shrinkCollection;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct2.shrinkProduct2;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct3.shrinkProduct3;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct4.shrinkProduct4;
import static dev.marksman.gauntlet.shrink.builtins.ShrinkProduct5.shrinkProduct5;

public final class ShrinkStrategies {

    private ShrinkStrategies() {
    }

    /**
     * Returns a shrinking strategy that shrinks longs.
     */
    public static ShrinkStrategy<Integer> shrinkInt() {
        return ShrinkNumerics.shrinkInt();
    }

    /**
     * Returns a shrinking strategy that shrinks longs.
     */
    public static ShrinkStrategy<Long> shrinkLong() {
        return ShrinkNumerics.shrinkLong();
    }

    /**
     * Returns a shrinking strategy that shrinks shorts.
     */
    public static ShrinkStrategy<Short> shrinkShort() {
        return ShrinkNumerics.shrinkShort();
    }

    /**
     * Returns a shrinking strategy that shrinks bytes.
     */
    public static ShrinkStrategy<Byte> shrinkByte() {
        return ShrinkNumerics.shrinkByte();
    }

    /**
     * Returns a shrinking strategy that shrinks integers, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Integer> shrinkInt(IntRange range) {
        return ShrinkNumerics.shrinkInt(range);
    }

    /**
     * Returns a shrinking strategy that shrinks longs, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Long> shrinkLong(LongRange range) {
        return ShrinkNumerics.shrinkLong(range);
    }

    /**
     * Returns a shrinking strategy that shrinks shorts, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Short> shrinkShort(ShortRange range) {
        return ShrinkNumerics.shrinkShort(range);
    }

    /**
     * Returns a shrinking strategy that shrinks bytes, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Byte> shrinkByte(ByteRange range) {
        return ShrinkNumerics.shrinkByte(range);
    }

    public static ShrinkStrategy<Boolean> shrinkBoolean() {
        return input -> input ? ShrinkResult.singleton(false) : ShrinkResult.empty();
    }

    public static <A> ShrinkStrategy<Maybe<A>> shrinkMaybe(ShrinkStrategy<A> sa) {
        return input -> input.match(__ -> ShrinkResult.empty(),
                a -> ShrinkResult.cons(nothing(), () -> sa.apply(a).fmap(Maybe::just)));
    }

    public static <L, R> ShrinkStrategy<Either<L, R>> shrinkEither(ShrinkStrategy<L> leftStrategy,
                                                                   ShrinkStrategy<R> rightStrategy) {
        return input -> input.match(l -> leftStrategy.apply(l).fmap(Either::left),
                r -> rightStrategy.apply(r).fmap(Either::right));
    }

    public static <A, B> ShrinkStrategy<Choice2<A, B>> shrinkChoice2(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice2::a),
                b -> sb.apply(b).fmap(Choice2::b));
    }

    public static <A, B, C> ShrinkStrategy<Choice3<A, B, C>> shrinkChoice3(ShrinkStrategy<A> sa,
                                                                           ShrinkStrategy<B> sb,
                                                                           ShrinkStrategy<C> sc) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice3::a),
                b -> sb.apply(b).fmap(Choice3::b),
                c -> sc.apply(c).fmap(Choice3::c));
    }

    public static <A, B> ShrinkStrategy<These<A, B>> shrinkThese(ShrinkStrategy<A> sa,
                                                                 ShrinkStrategy<B> sb) {
        ShrinkStrategy<Tuple2<A, B>> shrinkBoth = shrinkTuple(sa, sb);
        return input -> input.match(a -> sa.apply(a).fmap(These::a),
                b -> sb.apply(b).fmap(These::b),
                both -> shrinkBoth.apply(both).fmap(into(These::both)));
    }

    public static <A, B> ShrinkStrategy<Tuple2<A, B>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb) {
        return ShrinkTuple.shrinkTuple2(sa, sb);
    }

    public static <A, B, C> ShrinkStrategy<Tuple3<A, B, C>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                        ShrinkStrategy<B> sb,
                                                                        ShrinkStrategy<C> sc) {
        return ShrinkTuple.shrinkTuple3(sa, sb, sc);
    }

    public static <A, B, C, D> ShrinkStrategy<Tuple4<A, B, C, D>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                              ShrinkStrategy<B> sb,
                                                                              ShrinkStrategy<C> sc,
                                                                              ShrinkStrategy<D> sd) {
        return ShrinkTuple.shrinkTuple4(sa, sb, sc, sd);
    }

    public static <A, B, C, D, E> ShrinkStrategy<Tuple5<A, B, C, D, E>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                                    ShrinkStrategy<B> sb,
                                                                                    ShrinkStrategy<C> sc,
                                                                                    ShrinkStrategy<D> sd,
                                                                                    ShrinkStrategy<E> se) {
        return ShrinkTuple.shrinkTuple5(sa, sb, sc, sd, se);
    }

    public static <A, B, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                            ShrinkStrategy<B> sb,
                                                            Fn2<A, B, T> fromProduct,
                                                            Fn1<T, Product2<A, B>> toProduct) {
        return shrinkProduct2(sa, sb, fromProduct, toProduct);
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               Iso<Product2<A, B>, Product2<A, B>, T, T> iso) {
        return shrinkProduct2(sa, sb,
                (a, b) -> view(iso).apply(tuple(a, b)),
                view(iso.mirror()));
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               ShrinkStrategy<C> sc,
                                                               Fn3<A, B, C, T> fromProduct,
                                                               Fn1<T, Product3<A, B, C>> toProduct) {
        return shrinkProduct3(sa, sb, sc, fromProduct, toProduct);
    }

    public static <A, B, C, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                               ShrinkStrategy<B> sb,
                                                               ShrinkStrategy<C> sc,
                                                               Iso<Product3<A, B, C>, Product3<A, B, C>, T, T> iso) {
        return shrinkProduct3(sa, sb, sc,
                (a, b, c) -> view(iso).apply(tuple(a, b, c)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  Fn4<A, B, C, D, T> fromProduct,
                                                                  Fn1<T, Product4<A, B, C, D>> toProduct) {
        return shrinkProduct4(sa, sb, sc, sd, fromProduct, toProduct);
    }

    public static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  Iso<Product4<A, B, C, D>, Product4<A, B, C, D>, T, T> iso) {
        return shrinkProduct4(sa, sb, sc, sd,
                (a, b, c, d) -> view(iso).apply(tuple(a, b, c, d)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, E, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb,
                                                                     ShrinkStrategy<C> sc,
                                                                     ShrinkStrategy<D> sd,
                                                                     ShrinkStrategy<E> se,
                                                                     Fn5<A, B, C, D, E, T> fromProduct,
                                                                     Fn1<T, Product5<A, B, C, D, E>> toProduct) {
        return shrinkProduct5(sa, sb, sc, sd, se, fromProduct, toProduct);
    }

    public static <A, B, C, D, E, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                     ShrinkStrategy<B> sb,
                                                                     ShrinkStrategy<C> sc,
                                                                     ShrinkStrategy<D> sd,
                                                                     ShrinkStrategy<E> se,
                                                                     Iso<Product5<A, B, C, D, E>, Product5<A, B, C, D, E>, T, T> iso) {
        return shrinkProduct5(sa, sb, sc, sd, se,
                (a, b, c, d, e) -> view(iso).apply(tuple(a, b, c, d, e)),
                view(iso.mirror()));
    }

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkVector(ShrinkStrategy<A> element) {
        return shrinkCollection(0, element);
    }

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkVector(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element);
    }

    public static <A> ShrinkStrategy<ImmutableNonEmptyVector<A>> shrinkNonEmptyVector(ShrinkStrategy<A> element) {
        return shrinkCollection(1, element)
                .convert(ImmutableVector::toNonEmptyOrThrow,
                        vector -> vector);
    }

    public static <A> ShrinkStrategy<ImmutableNonEmptyVector<A>> shrinkNonEmptyVector(int minimumSize, ShrinkStrategy<A> element) {
        if (minimumSize < 1) {
            throw new IllegalArgumentException("minSize must be >= 1");
        }
        return shrinkCollection(minimumSize, element)
                .convert(ImmutableVector::toNonEmptyOrThrow,
                        vector -> vector);
    }

    public static <A> ShrinkStrategy<ArrayList<A>> shrinkArrayList(ShrinkStrategy<A> element) {
        return shrinkArrayList(0, element);
    }

    public static <A> ShrinkStrategy<ArrayList<A>> shrinkArrayList(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element)
                .convert(vector -> vector.toCollection(ArrayList::new),
                        Vector::copyFrom);
    }

    public static <A> ShrinkStrategy<HashSet<A>> shrinkHashSet(ShrinkStrategy<A> element) {
        return shrinkHashSet(0, element);
    }

    public static <A> ShrinkStrategy<HashSet<A>> shrinkHashSet(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element)
                .convert(vector -> vector.toCollection(HashSet::new),
                        hashSet -> Vector.copyFrom(hashSet).toNonEmptyOrThrow());
    }
}
