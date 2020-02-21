package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

public abstract class Ascription<A> implements CoProduct2<Ascription.Composite<A>,
        Ascription.GeneratorSource<A>, Ascription<A>> {

    public Ascription<A> composite(int position) {
        return new Composite<>(position, this);
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class Composite<A> extends Ascription<A> {
        @Getter
        private final int position;
        @Getter
        private final Ascription<A> next;

        @Override
        public <R> R match(Fn1<? super Composite<A>, ? extends R> aFn, Fn1<? super GeneratorSource<A>, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class GeneratorSource<A> extends Ascription<A> {
        private final Generator<A> generator;

        @Override
        public <R> R match(Fn1<? super Composite<A>, ? extends R> aFn, Fn1<? super GeneratorSource<A>, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static <A> Ascription<A> generatorSource(Generator<A> generator) {
        return new GeneratorSource<>(generator);
    }

}
