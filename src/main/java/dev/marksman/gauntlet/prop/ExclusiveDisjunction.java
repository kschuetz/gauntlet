package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;
import static dev.marksman.gauntlet.Name.name;

class ExclusiveDisjunction<A> implements Prop<A> {
    final Prop<A> p;
    final Prop<A> q;
    private final Name name;

    ExclusiveDisjunction(Prop<A> p, Prop<A> q) {
        this.p = p;
        this.q = q;
        this.name = name(p.getName() + " xor " + q.getName());
    }

    @Override
    public EvalResult test(Context context, A data) {
        // success + success -> failure
        // success + failure -> success
        // success + error -> error
        // failure + success -> success
        // failure + failure -> failure
        // failure + error -> failure
        // error + success -> error
        // error + failure -> error
        // error + error -> error
        return p.safeTest(context, data)
                .match(success ->
                                q.safeTest(context, data)
                                        .match(__ -> evalResult(failure(name, "xor failed")
                                                        .addCause(failure(q.getName(), "Expected failure"))),
                                                f1 -> success(),
                                                EvalResult::evalResult),
                        failure ->
                                q.safeTest(context, data)
                                        .match(EvalResult::evalResult,
                                                f1 -> evalResult(failure(name, "xor failed")
                                                        .addCause(failure)
                                                        .addCause(f1)),
                                                EvalResult::evalResult),
                        e1 ->
                                q.safeTest(context, data)
                                        .match(__ -> evalResult(e1),
                                                __ -> evalResult(e1),
                                                e2 -> evalResult(e1.combine(e2))));
    }

    @Override
    public Name getName() {
        return name;
    }
}
