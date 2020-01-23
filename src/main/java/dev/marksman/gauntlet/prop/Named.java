package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.gauntlet.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class Named<A> implements Prop<A> {
    private final Name name;
    final Prop<A> underlying;

    @Override
    public Either<Errors, EvalResult> test(Context context, A data) {
        return underlying.test(context, data);
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
