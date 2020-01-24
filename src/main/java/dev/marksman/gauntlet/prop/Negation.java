package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

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
    public EvalResult test(Context context, A data) {
        throw new UnsupportedOperationException("todo");
    }

    @Override
    public Name getName() {
        return name;
    }
}
