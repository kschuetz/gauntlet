package software.kes.gauntlet.shrink.builtins;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.Maybe;
import com.jnape.palatable.lambda.adt.These;
import com.jnape.palatable.lambda.adt.choice.Choice2;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.adt.choice.Choice4;
import com.jnape.palatable.lambda.adt.choice.Choice5;
import com.jnape.palatable.lambda.adt.choice.Choice6;
import com.jnape.palatable.lambda.adt.choice.Choice7;
import com.jnape.palatable.lambda.adt.choice.Choice8;
import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.adt.hlist.Tuple3;
import com.jnape.palatable.lambda.adt.hlist.Tuple4;
import com.jnape.palatable.lambda.adt.hlist.Tuple5;
import com.jnape.palatable.lambda.adt.hlist.Tuple6;
import com.jnape.palatable.lambda.adt.hlist.Tuple7;
import com.jnape.palatable.lambda.adt.hlist.Tuple8;
import com.jnape.palatable.lambda.adt.product.Product2;
import com.jnape.palatable.lambda.adt.product.Product3;
import com.jnape.palatable.lambda.adt.product.Product4;
import com.jnape.palatable.lambda.adt.product.Product5;
import com.jnape.palatable.lambda.adt.product.Product6;
import com.jnape.palatable.lambda.adt.product.Product7;
import com.jnape.palatable.lambda.adt.product.Product8;
import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.Fn2;
import com.jnape.palatable.lambda.functions.Fn3;
import com.jnape.palatable.lambda.functions.Fn4;
import com.jnape.palatable.lambda.functions.Fn5;
import com.jnape.palatable.lambda.functions.Fn6;
import com.jnape.palatable.lambda.functions.Fn7;
import com.jnape.palatable.lambda.functions.Fn8;
import com.jnape.palatable.lambda.optics.Iso;
import software.kes.collectionviews.ImmutableNonEmptyVector;
import software.kes.collectionviews.ImmutableVector;
import software.kes.collectionviews.NonEmptyVector;
import software.kes.collectionviews.Vector;
import software.kes.gauntlet.shrink.ShrinkResult;
import software.kes.gauntlet.shrink.ShrinkStrategy;
import software.kes.kraftwerk.constraints.BigIntegerRange;
import software.kes.kraftwerk.constraints.ByteRange;
import software.kes.kraftwerk.constraints.DoubleRange;
import software.kes.kraftwerk.constraints.FloatRange;
import software.kes.kraftwerk.constraints.IntRange;
import software.kes.kraftwerk.constraints.LongRange;
import software.kes.kraftwerk.constraints.ShortRange;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.jnape.palatable.lambda.adt.Maybe.nothing;
import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Into.into;
import static com.jnape.palatable.lambda.optics.functions.View.view;
import static software.kes.gauntlet.shrink.builtins.ShrinkCollection.shrinkCollection;
import static software.kes.gauntlet.shrink.builtins.ShrinkProduct2.shrinkProduct2;
import static software.kes.gauntlet.shrink.builtins.ShrinkProduct3.shrinkProduct3;
import static software.kes.gauntlet.shrink.builtins.ShrinkProduct5.shrinkProduct5;
import static software.kes.gauntlet.shrink.builtins.ShrinkProduct6.shrinkProduct6;
import static software.kes.gauntlet.shrink.builtins.ShrinkProduct8.shrinkProduct8;

public final class ShrinkStrategies {
    private ShrinkStrategies() {
    }

    /**
     * Returns a shrinking strategy that shrinks ints.
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
     * Returns a shrinking strategy that shrinks floats.
     */
    public static ShrinkStrategy<Float> shrinkFloat() {
        return ShrinkNumerics.shrinkFloat();
    }

    /**
     * Returns a shrinking strategy that shrinks doubles.
     */
    public static ShrinkStrategy<Double> shrinkDouble() {
        return ShrinkNumerics.shrinkDouble();
    }

    /**
     * Returns a shrinking strategy that shrinks BigIntegers, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<BigInteger> shrinkBigInteger() {
        return ShrinkNumerics.shrinkBigInteger();
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

    /**
     * Returns a shrinking strategy that shrinks floats, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Float> shrinkFloat(FloatRange range) {
        return ShrinkNumerics.shrinkFloat(range);
    }

    /**
     * Returns a shrinking strategy that shrinks doubles, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<Double> shrinkDouble(DoubleRange range) {
        return ShrinkNumerics.shrinkDouble(range);
    }

    /**
     * Returns a shrinking strategy that shrinks BigIntegers, but limits values in the output to a given range.
     */
    public static ShrinkStrategy<BigInteger> shrinkBigInteger(BigIntegerRange range) {
        return ShrinkNumerics.shrinkBigInteger(range);
    }

    public static ShrinkStrategy<Boolean> shrinkBoolean() {
        return input -> input ? ShrinkResult.singleton(false) : ShrinkResult.empty();
    }

    public static ShrinkStrategy<Character> shrinkCharacter() {
        return ShrinkCharacter.shrinkCharacter();
    }

    public static ShrinkStrategy<Character> shrinkAlphaCharacter() {
        return ShrinkCharacter.shrinkCharacter().filter(Character::isLetter);
    }

    public static ShrinkStrategy<Character> shrinkAlphaUpperCharacter() {
        return ShrinkCharacter.shrinkCharacter().filter(c -> Character.isLetter(c) && Character.isUpperCase(c));
    }

    public static ShrinkStrategy<Character> shrinkAlphaLowerCharacter() {
        return ShrinkCharacter.shrinkCharacter().filter(c -> Character.isLetter(c) && Character.isLowerCase(c));
    }

    public static ShrinkStrategy<Character> shrinkAlphanumericCharacter() {
        return ShrinkCharacter.shrinkCharacter().filter(Character::isLetterOrDigit);
    }

    public static ShrinkStrategy<Character> shrinkNumericCharacter() {
        return ShrinkCharacter.shrinkCharacter().filter(Character::isDigit);
    }

    public static ShrinkStrategy<String> shrinkString() {
        return ShrinkString.shrinkString();
    }

    public static ShrinkStrategy<String> shrinkString(ShrinkStrategy<Character> elements) {
        return ShrinkString.shrinkString(elements);
    }

    public static ShrinkStrategy<String> shrinkString(int minLength) {
        return ShrinkString.shrinkString(minLength);
    }

    public static ShrinkStrategy<String> shrinkString(int minLength, ShrinkStrategy<Character> elements) {
        return ShrinkString.shrinkString(minLength, elements);
    }

    public static ShrinkStrategy<String> shrinkAlphaString(int minLength) {
        return shrinkString(minLength, shrinkAlphaCharacter());
    }

    public static ShrinkStrategy<String> shrinkAlphaUpperString(int minLength) {
        return shrinkString(minLength, shrinkAlphaUpperCharacter());
    }

    public static ShrinkStrategy<String> shrinkAlphaLowerString(int minLength) {
        return shrinkString(minLength, shrinkAlphaLowerCharacter());
    }

    public static ShrinkStrategy<String> shrinkAlphanumericString(int minLength) {
        return shrinkString(minLength, shrinkAlphanumericCharacter());
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

    public static <A, B, C, D> ShrinkStrategy<Choice4<A, B, C, D>> shrinkChoice4(ShrinkStrategy<A> sa,
                                                                                 ShrinkStrategy<B> sb,
                                                                                 ShrinkStrategy<C> sc,
                                                                                 ShrinkStrategy<D> sd) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice4::a),
                b -> sb.apply(b).fmap(Choice4::b),
                c -> sc.apply(c).fmap(Choice4::c),
                d -> sd.apply(d).fmap(Choice4::d));
    }

    public static <A, B, C, D, E> ShrinkStrategy<Choice5<A, B, C, D, E>> shrinkChoice5(ShrinkStrategy<A> sa,
                                                                                       ShrinkStrategy<B> sb,
                                                                                       ShrinkStrategy<C> sc,
                                                                                       ShrinkStrategy<D> sd,
                                                                                       ShrinkStrategy<E> se) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice5::a),
                b -> sb.apply(b).fmap(Choice5::b),
                c -> sc.apply(c).fmap(Choice5::c),
                d -> sd.apply(d).fmap(Choice5::d),
                e -> se.apply(e).fmap(Choice5::e));
    }

    public static <A, B, C, D, E, F> ShrinkStrategy<Choice6<A, B, C, D, E, F>> shrinkChoice6(ShrinkStrategy<A> sa,
                                                                                             ShrinkStrategy<B> sb,
                                                                                             ShrinkStrategy<C> sc,
                                                                                             ShrinkStrategy<D> sd,
                                                                                             ShrinkStrategy<E> se,
                                                                                             ShrinkStrategy<F> sf) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice6::a),
                b -> sb.apply(b).fmap(Choice6::b),
                c -> sc.apply(c).fmap(Choice6::c),
                d -> sd.apply(d).fmap(Choice6::d),
                e -> se.apply(e).fmap(Choice6::e),
                f -> sf.apply(f).fmap(Choice6::f));
    }

    public static <A, B, C, D, E, F, G> ShrinkStrategy<Choice7<A, B, C, D, E, F, G>> shrinkChoice7(ShrinkStrategy<A> sa,
                                                                                                   ShrinkStrategy<B> sb,
                                                                                                   ShrinkStrategy<C> sc,
                                                                                                   ShrinkStrategy<D> sd,
                                                                                                   ShrinkStrategy<E> se,
                                                                                                   ShrinkStrategy<F> sf,
                                                                                                   ShrinkStrategy<G> sg) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice7::a),
                b -> sb.apply(b).fmap(Choice7::b),
                c -> sc.apply(c).fmap(Choice7::c),
                d -> sd.apply(d).fmap(Choice7::d),
                e -> se.apply(e).fmap(Choice7::e),
                f -> sf.apply(f).fmap(Choice7::f),
                g -> sg.apply(g).fmap(Choice7::g));
    }

    public static <A, B, C, D, E, F, G, H> ShrinkStrategy<Choice8<A, B, C, D, E, F, G, H>> shrinkChoice8(ShrinkStrategy<A> sa,
                                                                                                         ShrinkStrategy<B> sb,
                                                                                                         ShrinkStrategy<C> sc,
                                                                                                         ShrinkStrategy<D> sd,
                                                                                                         ShrinkStrategy<E> se,
                                                                                                         ShrinkStrategy<F> sf,
                                                                                                         ShrinkStrategy<G> sg,
                                                                                                         ShrinkStrategy<H> sh) {
        return input -> input.match(a -> sa.apply(a).fmap(Choice8::a),
                b -> sb.apply(b).fmap(Choice8::b),
                c -> sc.apply(c).fmap(Choice8::c),
                d -> sd.apply(d).fmap(Choice8::d),
                e -> se.apply(e).fmap(Choice8::e),
                f -> sf.apply(f).fmap(Choice8::f),
                g -> sg.apply(g).fmap(Choice8::g),
                h -> sh.apply(h).fmap(Choice8::h));
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

    public static <A, B, C, D, E, F> ShrinkStrategy<Tuple6<A, B, C, D, E, F>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                                          ShrinkStrategy<B> sb,
                                                                                          ShrinkStrategy<C> sc,
                                                                                          ShrinkStrategy<D> sd,
                                                                                          ShrinkStrategy<E> se,
                                                                                          ShrinkStrategy<F> sf) {
        return ShrinkTuple.shrinkTuple6(sa, sb, sc, sd, se, sf);
    }

    public static <A, B, C, D, E, F, G> ShrinkStrategy<Tuple7<A, B, C, D, E, F, G>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                                                ShrinkStrategy<B> sb,
                                                                                                ShrinkStrategy<C> sc,
                                                                                                ShrinkStrategy<D> sd,
                                                                                                ShrinkStrategy<E> se,
                                                                                                ShrinkStrategy<F> sf,
                                                                                                ShrinkStrategy<G> sg) {
        return ShrinkTuple.shrinkTuple7(sa, sb, sc, sd, se, sf, sg);
    }

    public static <A, B, C, D, E, F, G, H> ShrinkStrategy<Tuple8<A, B, C, D, E, F, G, H>> shrinkTuple(ShrinkStrategy<A> sa,
                                                                                                      ShrinkStrategy<B> sb,
                                                                                                      ShrinkStrategy<C> sc,
                                                                                                      ShrinkStrategy<D> sd,
                                                                                                      ShrinkStrategy<E> se,
                                                                                                      ShrinkStrategy<F> sf,
                                                                                                      ShrinkStrategy<G> sg,
                                                                                                      ShrinkStrategy<H> sh) {
        return ShrinkTuple.shrinkTuple8(sa, sb, sc, sd, se, sf, sg, sh);
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
        return ShrinkProduct4.shrinkProduct4(sa, sb, sc, sd, fromProduct, toProduct);
    }

    public static <A, B, C, D, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                  ShrinkStrategy<B> sb,
                                                                  ShrinkStrategy<C> sc,
                                                                  ShrinkStrategy<D> sd,
                                                                  Iso<Product4<A, B, C, D>, Product4<A, B, C, D>, T, T> iso) {
        return ShrinkProduct4.shrinkProduct4(sa, sb, sc, sd,
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

    public static <A, B, C, D, E, F, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                        ShrinkStrategy<B> sb,
                                                                        ShrinkStrategy<C> sc,
                                                                        ShrinkStrategy<D> sd,
                                                                        ShrinkStrategy<E> se,
                                                                        ShrinkStrategy<F> sf,
                                                                        Fn6<A, B, C, D, E, F, T> fromProduct,
                                                                        Fn1<T, Product6<A, B, C, D, E, F>> toProduct) {
        return shrinkProduct6(sa, sb, sc, sd, se, sf, fromProduct, toProduct);
    }

    public static <A, B, C, D, E, F, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                        ShrinkStrategy<B> sb,
                                                                        ShrinkStrategy<C> sc,
                                                                        ShrinkStrategy<D> sd,
                                                                        ShrinkStrategy<E> se,
                                                                        ShrinkStrategy<F> sf,
                                                                        Iso<Product6<A, B, C, D, E, F>, Product6<A, B, C, D, E, F>, T, T> iso) {
        return shrinkProduct6(sa, sb, sc, sd, se, sf,
                (a, b, c, d, e, f) -> view(iso).apply(tuple(a, b, c, d, e, f)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, E, F, G, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                           ShrinkStrategy<B> sb,
                                                                           ShrinkStrategy<C> sc,
                                                                           ShrinkStrategy<D> sd,
                                                                           ShrinkStrategy<E> se,
                                                                           ShrinkStrategy<F> sf,
                                                                           ShrinkStrategy<G> sg,
                                                                           Fn7<A, B, C, D, E, F, G, T> fromProduct,
                                                                           Fn1<T, Product7<A, B, C, D, E, F, G>> toProduct) {
        return ShrinkProduct7.shrinkProduct7(sa, sb, sc, sd, se, sf, sg, fromProduct, toProduct);
    }

    public static <A, B, C, D, E, F, G, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                           ShrinkStrategy<B> sb,
                                                                           ShrinkStrategy<C> sc,
                                                                           ShrinkStrategy<D> sd,
                                                                           ShrinkStrategy<E> se,
                                                                           ShrinkStrategy<F> sf,
                                                                           ShrinkStrategy<G> sg,
                                                                           Iso<Product7<A, B, C, D, E, F, G>, Product7<A, B, C, D, E, F, G>, T, T> iso) {
        return ShrinkProduct7.shrinkProduct7(sa, sb, sc, sd, se, sf, sg,
                (a, b, c, d, e, f, g) -> view(iso).apply(tuple(a, b, c, d, e, f, g)),
                view(iso.mirror()));
    }

    public static <A, B, C, D, E, F, G, H, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                              ShrinkStrategy<B> sb,
                                                                              ShrinkStrategy<C> sc,
                                                                              ShrinkStrategy<D> sd,
                                                                              ShrinkStrategy<E> se,
                                                                              ShrinkStrategy<F> sf,
                                                                              ShrinkStrategy<G> sg,
                                                                              ShrinkStrategy<H> sh,
                                                                              Fn8<A, B, C, D, E, F, G, H, T> fromProduct,
                                                                              Fn1<T, Product8<A, B, C, D, E, F, G, H>> toProduct) {
        return shrinkProduct8(sa, sb, sc, sd, se, sf, sg, sh, fromProduct, toProduct);
    }

    public static <A, B, C, D, E, F, G, H, T> ShrinkStrategy<T> shrinkProduct(ShrinkStrategy<A> sa,
                                                                              ShrinkStrategy<B> sb,
                                                                              ShrinkStrategy<C> sc,
                                                                              ShrinkStrategy<D> sd,
                                                                              ShrinkStrategy<E> se,
                                                                              ShrinkStrategy<F> sf,
                                                                              ShrinkStrategy<G> sg,
                                                                              ShrinkStrategy<H> sh,
                                                                              Iso<Product8<A, B, C, D, E, F, G, H>, Product8<A, B, C, D, E, F, G, H>, T, T> iso) {
        return shrinkProduct8(sa, sb, sc, sd, se, sf, sg, sh,
                (a, b, c, d, e, f, g, h) -> view(iso).apply(tuple(a, b, c, d, e, f, g, h)),
                view(iso.mirror()));
    }

    public static <A> ShrinkStrategy<Vector<A>> shrinkVector(ShrinkStrategy<A> element) {
        return shrinkCollection(0, element);
    }

    public static <A> ShrinkStrategy<Vector<A>> shrinkVector(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element);
    }

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkImmutableVector(ShrinkStrategy<A> element) {
        return shrinkCustomCollection(Vector::copyFrom, id(), element);
    }

    public static <A> ShrinkStrategy<ImmutableVector<A>> shrinkImmutableVector(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCustomCollection(Vector::copyFrom, id(), minimumSize, element);
    }

    public static <A> ShrinkStrategy<NonEmptyVector<A>> shrinkNonEmptyVector(ShrinkStrategy<A> element) {
        return shrinkCustomCollection(NonEmptyVector::copyFromOrThrow, id(), 1, element);
    }

    public static <A> ShrinkStrategy<NonEmptyVector<A>> shrinkNonEmptyVector(int minimumSize, ShrinkStrategy<A> element) {
        if (minimumSize < 1) {
            throw new IllegalArgumentException("minSize must be >= 1");
        }
        return shrinkCustomCollection(NonEmptyVector::copyFromOrThrow, id(), minimumSize, element);
    }

    public static <A> ShrinkStrategy<ImmutableNonEmptyVector<A>> shrinkImmutableNonEmptyVector(ShrinkStrategy<A> element) {
        return shrinkCustomCollection(NonEmptyVector::copyFromOrThrow, id(), element);
    }

    public static <A> ShrinkStrategy<ImmutableNonEmptyVector<A>> shrinkImmutableNonEmptyVector(int minimumSize, ShrinkStrategy<A> element) {
        return shrinkCustomCollection(NonEmptyVector::copyFromOrThrow, id(), minimumSize, element);
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

    public static <K, V> ShrinkStrategy<HashMap<K, V>> shrinkHashMap(ShrinkStrategy<V> values) {
        return shrinkHashMap(0, values);
    }

    public static <K, V> ShrinkStrategy<HashMap<K, V>> shrinkHashMap(int minimumSize, ShrinkStrategy<V> values) {
        return ShrinkStrategy.none(); // TODO
    }

    public static <A, Collection> ShrinkStrategy<Collection> shrinkCustomCollection(Fn1<? super Vector<A>, ? extends Collection> fromVector,
                                                                                    Fn1<? super Collection, ? extends Vector<A>> toVector,
                                                                                    ShrinkStrategy<A> element) {
        return shrinkCollection(0, element).convert(fromVector, toVector);
    }

    public static <A, Collection> ShrinkStrategy<Collection> shrinkCustomCollection(Fn1<? super Vector<A>, ? extends Collection> fromVector,
                                                                                    Fn1<? super Collection, ? extends Vector<A>> toVector,
                                                                                    int minimumSize,
                                                                                    ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element).convert(fromVector, toVector);
    }

    public static <A, Collection> ShrinkStrategy<Collection> shrinkCustomCollection(Iso<? super Vector<A>, ? extends Vector<A>, ? extends Collection, ? super Collection> iso,
                                                                                    ShrinkStrategy<A> element) {
        return shrinkCollection(0, element).convert(iso);
    }

    public static <A, Collection> ShrinkStrategy<Collection> shrinkCustomCollection(Iso<? super Vector<A>, ? extends Vector<A>, ? extends Collection, ? super Collection> iso,
                                                                                    int minimumSize,
                                                                                    ShrinkStrategy<A> element) {
        return shrinkCollection(minimumSize, element).convert(iso);
    }
}
