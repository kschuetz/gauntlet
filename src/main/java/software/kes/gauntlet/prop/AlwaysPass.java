package software.kes.gauntlet.prop;

import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

import static software.kes.gauntlet.EvalSuccess.evalSuccess;

final class AlwaysPass<A> implements Prop<A> {
    private static final String NAME = "Pass";
    private static final AlwaysPass<?> INSTANCE = new AlwaysPass<>();

    @SuppressWarnings("unchecked")
    static <A> AlwaysPass<A> alwaysPass() {
        return (AlwaysPass<A>) INSTANCE;
    }

    @Override
    public EvalResult evaluate(A data) {
        return evalSuccess();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
