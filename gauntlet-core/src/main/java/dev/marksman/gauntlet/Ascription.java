package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.coproduct.CoProduct2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.kraftwerk.Generator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PRIVATE;

public abstract class Ascription implements CoProduct2<Ascription.Composite,
        Ascription.GeneratorSource, Ascription> {

    public Ascription composite(int position) {
        return new Composite(position, this);
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class Composite extends Ascription {
        @Getter
        private final int position;
        @Getter
        private final Ascription next;

        @Override
        public <R> R match(Fn1<? super Composite, ? extends R> aFn, Fn1<? super GeneratorSource, ? extends R> bFn) {
            return aFn.apply(this);
        }
    }

    @AllArgsConstructor(access = PRIVATE)
    public static class GeneratorSource extends Ascription {
        @Getter
        private final Generator<?> generator;

        @Override
        public <R> R match(Fn1<? super Composite, ? extends R> aFn, Fn1<? super GeneratorSource, ? extends R> bFn) {
            return bFn.apply(this);
        }
    }

    public static Ascription generatorSource(Generator<?> generator) {
        return new GeneratorSource(generator);
    }

}
