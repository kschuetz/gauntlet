package software.kes.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;
import software.kes.gauntlet.SimpleResult;

import static software.kes.gauntlet.EvalFailure.evalFailure;
import static software.kes.gauntlet.EvalSuccess.evalSuccess;

final class BasicProp<A> implements Prop<A> {
    private final String name;
    private final Fn1<? super A, SimpleResult> evaluator;

    BasicProp(String name, Fn1<? super A, SimpleResult> evaluator) {
        this.name = name;
        this.evaluator = evaluator;
    }

    @Override
    public EvalResult evaluate(A data) {
        return evaluator.apply(data)
                .match(__ -> evalSuccess(),
                        fail -> evalFailure(this, fail.getReasons()));
    }

    @Override
    public String getName() {
        return name;
    }
}
