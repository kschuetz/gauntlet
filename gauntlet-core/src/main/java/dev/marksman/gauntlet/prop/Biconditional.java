package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;

class Biconditional<A> implements Prop<A> {
    final Prop<A> antecedent;
    final Prop<A> consequent;
    private final String name;

    Biconditional(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = antecedent.getName() + " <=> " + consequent.getName();
    }

    @Override
    public EvalResult test(A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + success -> failure
        // failure + failure -> success
        return antecedent.test(data)
                .match(success -> consequent.test(data)
                                .match(id(),
                                        f1 -> createFailure().addCause(f1)),
                        failure -> consequent.test(data)
                                .match(__ -> createFailure().addCause(failure),
                                        __ -> evalSuccess()));
    }

    @Override
    public String getName() {
        return name;
    }

    private EvalFailure createFailure() {
        return EvalFailure.evalFailure(this, failureReasons("Biconditional failed."));
    }
}
