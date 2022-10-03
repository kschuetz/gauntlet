package software.kes.gauntlet.prop;

import software.kes.collectionviews.ImmutableVector;
import software.kes.enhancediterables.ImmutableNonEmptyFiniteIterable;
import software.kes.gauntlet.Cause;
import software.kes.gauntlet.EvalFailure;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;
import static software.kes.gauntlet.Reasons.reasons;

final class Conjunction<A> implements Prop<A> {
    private final ImmutableNonEmptyFiniteIterable<Prop<A>> operands;
    private final String name;

    Conjunction(ImmutableNonEmptyFiniteIterable<Prop<A>> operands) {
        this.operands = operands;
        this.name = String.join(" ∧ ",
                operands.fmap(Prop::getName));
    }

    @Override
    public Prop<A> and(Prop<A> other) {
        return new Conjunction<>((other instanceof Conjunction<?>)
                ? operands.concat(((Conjunction<A>) other).operands)
                : operands.append(other));
    }

    // success + success -> success
    // success + failure -> failure
    // failure + success -> failure
    // failure + failure -> failure

    @Override
    public EvalResult evaluate(A data) {
        ImmutableNonEmptyFiniteIterable<EvalResult> results = operands.fmap(p -> p.evaluate(data));

        Accumulator combined = results.foldLeft((acc, result) -> result.match(__ -> acc.addSuccess(), acc::addFailure),
                Accumulator.accumulator());
        ImmutableVector<EvalFailure> causes = combined.getFailures().build();

        if (causes.isEmpty()) {
            return evalSuccess();
        } else {
            int failureCount = causes.size();
            int totalCount = combined.getSuccessCount() + failureCount;
            String message = "Some properties in conjunction failed (" + failureCount + " of " + totalCount + ")";
            return evalFailure(this, reasons(message), causes.fmap(Cause::propertyFailed));
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
