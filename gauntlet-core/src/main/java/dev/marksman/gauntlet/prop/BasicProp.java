package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalFailure;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;
import dev.marksman.gauntlet.SimpleResult;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;

final class BasicProp<A> implements Prop<A> {
    private final String name;
    private final Fn1<? super A, SimpleResult> evaluator;

    BasicProp(String name, Fn1<? super A, SimpleResult> evaluator) {
        this.name = name;
        this.evaluator = evaluator;
    }

    @Override
    public EvalResult test(A data) {
        return evaluator.apply(data)
                .match(__ -> evalSuccess(),
                        fail -> EvalFailure.evalFailure(this, fail.getReasons()));
    }

    @Override
    public String getName() {
        return name;
    }

}
