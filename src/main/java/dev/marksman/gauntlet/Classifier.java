package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.hlist.Tuple2;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.collectionviews.Vector;
import dev.marksman.enhancediterables.ImmutableFiniteIterable;

import java.util.HashSet;
import java.util.Set;

import static com.jnape.palatable.lambda.adt.hlist.HList.tuple;

public final class Classifier<A> implements Fn1<A, Set<String>> {

    private final ImmutableFiniteIterable<Tuple2<String, Fn1<A, Boolean>>> items;

    private Classifier(ImmutableFiniteIterable<Tuple2<String, Fn1<A, Boolean>>> items) {
        this.items = items;
    }

    @Override
    public Set<String> checkedApply(A a) {
        Set<String> result = new HashSet<>();
        for (Tuple2<String, Fn1<A, Boolean>> item : items) {
            if (item._2().apply(a)) {
                result.add(item._1());
            }
        }
        return result;
    }

    public Classifier<A> combine(Classifier<A> other) {
        return new Classifier<>(items.concat(other.items));
    }

    public Classifier<A> and(String category, Fn1<A, Boolean> predicate) {
        return new Classifier<>(items.prepend(tuple(category, predicate)));
    }

    public static <A> Classifier<A> classify(String category, Fn1<A, Boolean> predicate) {
        return new Classifier<>(Vector.of(tuple(category, predicate)));
    }
}
