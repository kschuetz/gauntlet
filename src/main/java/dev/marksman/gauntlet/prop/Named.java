package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class Named<A> implements Prop<A> {
    private final Name name;
    final Prop<A> underlying;

    @Override
    public EvalResult test(A data) {
        return underlying.test(data);
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Prop<A> named(Name name) {
        return Combinators.named(name, underlying);
    }
}
