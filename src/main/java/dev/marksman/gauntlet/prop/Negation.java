package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.adt.Either;
import dev.marksman.gauntlet.*;

class Negation<A> implements Prop<A> {
    final Prop<A> operand;
    private final Name name;

    Negation(Prop<A> operand) {
        this.operand = operand;
        this.name = Name.name("~" + operand.getName());
    }

    @Override
    public Prop<A> not() {
        return operand;
    }

    @Override
    public Either<Errors, EvalResult> test(Context context, A data) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Name getName() {
        return name;
    }
}
