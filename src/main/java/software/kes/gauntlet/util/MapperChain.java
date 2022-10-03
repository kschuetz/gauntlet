package software.kes.gauntlet.util;

import com.jnape.palatable.lambda.functions.Fn1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static com.jnape.palatable.lambda.functions.builtin.fn1.Reverse.reverse;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Cons.cons;
import static com.jnape.palatable.lambda.functions.builtin.fn2.Snoc.snoc;
import static com.jnape.palatable.lambda.functions.builtin.fn2.ToCollection.toCollection;
import static com.jnape.palatable.lambda.functions.builtin.fn3.FoldLeft.foldLeft;

public interface MapperChain {
    MapperChain append(Fn1<Object, Object> f);

    MapperChain prepend(Fn1<Object, Object> f);

    Fn1<Object, Object> getFn();

    Object apply(Object input);

    boolean isEmpty();

    static MapperChain empty() {
        return MapperChain.EmptyMapperChain.INSTANCE;
    }

    static MapperChain mapperChain(Fn1<Object, Object> f) {
        return new MapperChain.MapperChainImpl(Collections.singletonList(lambdaToJava(f)));
    }

    final class EmptyMapperChain implements MapperChain {
        static final EmptyMapperChain INSTANCE = new EmptyMapperChain();

        private EmptyMapperChain() {

        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public MapperChain append(Fn1<Object, Object> f) {
            return mapperChain(f);
        }

        @Override
        public MapperChain prepend(Fn1<Object, Object> f) {
            return mapperChain(f);
        }

        @Override
        public Fn1<Object, Object> getFn() {
            return id();
        }

        @Override
        public Object apply(Object input) {
            return input;
        }

    }

    final class MapperChainImpl implements MapperChain {
        private final Iterable<Function<Object, Object>> mappers;
        private volatile Fn1<Object, Object> fnComposedOnTheHeap;

        private MapperChainImpl(Iterable<Function<Object, Object>> mappers) {
            this.mappers = mappers;
            fnComposedOnTheHeap = null;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        public MapperChainImpl append(Fn1<Object, Object> f) {
            return new MapperChainImpl(cons(lambdaToJava(f), mappers));
        }

        @Override
        public MapperChain prepend(Fn1<Object, Object> f) {
            return new MapperChainImpl(snoc(lambdaToJava(f), mappers));
        }

        public Object apply(Object input) {
            return getFn().apply(input);
        }

        public Fn1<Object, Object> getFn() {
            if (fnComposedOnTheHeap == null) {
                synchronized (this) {
                    if (fnComposedOnTheHeap == null) {
                        fnComposedOnTheHeap = build();
                    }

                }
            }
            return fnComposedOnTheHeap;
        }

        private Fn1<Object, Object> build() {
            ArrayList<Function<Object, Object>> fnChain = toCollection(ArrayList::new, reverse(mappers));
            return o -> foldLeft((x, fn) -> fn.apply(x), o, fnChain);
        }
    }

    static Function<Object, Object> lambdaToJava(Fn1<Object, Object> f) {
        return f::apply;
    }
}
