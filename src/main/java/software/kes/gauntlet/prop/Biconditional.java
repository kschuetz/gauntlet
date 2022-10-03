package software.kes.gauntlet.prop;

import software.kes.collectionviews.Vector;
import software.kes.gauntlet.EvalFailure;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static com.jnape.palatable.lambda.functions.builtin.fn1.Id.id;
import static software.kes.gauntlet.Cause.propertyFailed;
import static software.kes.gauntlet.Cause.propertyPassed;
import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.Reasons.reasons;

final class Biconditional<A> implements Prop<A> {
    private final Prop<A> antecedent;
    private final Prop<A> consequent;
    private final String name;

    Biconditional(Prop<A> antecedent, Prop<A> consequent) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.name = antecedent.getName() + " <=> " + consequent.getName();
    }

    @Override
    public EvalResult evaluate(A data) {
        // success + success -> success
        // success + failure -> failure
        // failure + success -> failure
        // failure + failure -> success
        return antecedent.evaluate(data)
                .match(success -> consequent.evaluate(data)
                                .match(id(),
                                        f1 -> createFailure(antecedent, f1)),
                        failure -> consequent.evaluate(data)
                                .match(__ -> createFailure(consequent, failure),
                                        __ -> evalSuccess()));
    }

    @Override
    public String getName() {
        return name;
    }

    private EvalFailure createFailure(Prop<A> passed, EvalFailure failure) {
        return evalFailure(this, reasons("Biconditional failed",
                "outcome of properties did not match"),
                Vector.of(propertyPassed(passed),
                        propertyFailed(failure)));
    }
}
