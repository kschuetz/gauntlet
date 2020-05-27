package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static dev.marksman.gauntlet.Cause.propertyFailed;
import static dev.marksman.gauntlet.Cause.propertyPassed;
import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;
import static dev.marksman.gauntlet.Reasons.reasons;

final class ExclusiveDisjunction<A> implements Prop<A> {
    private final Prop<A> p;
    private final Prop<A> q;
    private final String name;

    ExclusiveDisjunction(Prop<A> p, Prop<A> q) {
        this.p = p;
        this.q = q;
        this.name = p.getName() + " xor " + q.getName();
    }

    @Override
    public EvalResult evaluate(A data) {
        // success + success -> failure
        // success + failure -> success
        // failure + success -> success
        // failure + failure -> failure
        return p.evaluate(data)
                .match(pass1 -> q.evaluate(data)
                                .match(__ -> EvalFailure.evalFailure(this, reasons("xor failed",
                                        "both properties passed"))
                                                .addCause(propertyPassed(p))
                                                .addCause(propertyPassed(q)),
                                        f1 -> evalSuccess()),
                        f1 -> q.evaluate(data)
                                .match(id(),
                                        f2 -> EvalFailure.evalFailure(this, reasons("xor failed",
                                                "both properties failed"))
                                                .addCause(propertyFailed(f1))
                                                .addCause(propertyFailed(f2))));
    }

    @Override
    public String getName() {
        return name;
    }
}
