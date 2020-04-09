package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.FailureReasons.failureReasons;

class ExclusiveDisjunction<A> implements Prop<A> {
    final Prop<A> p;
    final Prop<A> q;
    private final String name;

    ExclusiveDisjunction(Prop<A> p, Prop<A> q) {
        this.p = p;
        this.q = q;
        this.name = p.getName() + " xor " + q.getName();
    }

    @Override
    public EvalResult test(A data) {
        // success + success -> failure
        // success + failure -> success
        // failure + success -> success
        // failure + failure -> failure
        return p.test(data)
                .match(success -> q.test(data)
                                .match(__ -> EvalFailure.evalFailure(this, failureReasons("xor failed"))
                                                .addCause(EvalFailure.evalFailure(q, failureReasons("Expected failure"))),
                                        f1 -> evalSuccess()),
                        failure -> q.test(data)
                                .match(id(),
                                        f1 -> EvalFailure.evalFailure(this, failureReasons("xor failed"))
                                                .addCause(failure)
                                                .addCause(f1)));
    }

    @Override
    public String getName() {
        return name;
    }
}
