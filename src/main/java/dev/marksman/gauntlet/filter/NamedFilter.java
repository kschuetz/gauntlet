package dev.marksman.gauntlet.filter;


import com.jnape.palatable.lambda.functions.Fn1;
import com.jnape.palatable.lambda.functions.specialized.Predicate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class NamedFilter<A> implements Predicate<A> {
    private final String name;
    private final Fn1<A, Boolean> predicate;

    @Override
    public Boolean checkedApply(A a) {
        return predicate.apply(a);
    }

    static <A> NamedFilter<A> namedFilter(String name, Fn1<A, Boolean> predicate) {
        return new NamedFilter<>(name, predicate);
    }
}
