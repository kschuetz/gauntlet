package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.pass;

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
    public EvalResult test(A data) {
        return operand.test(data)
                .match(__ -> EvalResult.fail("Failure expected"),
                        fail -> pass(),
                        error -> error);
    }

    @Override
    public Name getName() {
        return name;
    }
}
