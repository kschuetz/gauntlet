package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

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
    public EvalResult test(Context context, A data) {
        // success + success -> failure
        // success + failure -> success
        // failure + success -> success
        // failure + failure -> failure
        return p.test(context, data)
                .match(success -> q.test(context, data)
                                .match(__ -> evalResult(failure(this, "xor failed")
                                                .addCause(failure(q, "Expected failure"))),
                                        f1 -> success()),
                        failure -> q.test(context, data)
                                .match(EvalResult::evalResult,
                                        f1 -> evalResult(failure(this, "xor failed")
                                                .addCause(failure)
                                                .addCause(f1))));
    }

    @Override
    public String getName() {
        return name;
    }
}
