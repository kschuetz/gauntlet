package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.BasicPropResult;
import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import static dev.marksman.gauntlet.EvalResult.evalResult;
import static dev.marksman.gauntlet.EvalResult.success;
import static dev.marksman.gauntlet.Failure.failure;

final class BasicProp<A> implements Prop<A> {
    private final String name;
    private final Fn1<? super A, BasicPropResult> evaluator;

    BasicProp(String name, Fn1<? super A, BasicPropResult> evaluator) {
        this.name = name;
        this.evaluator = evaluator;
    }

    @Override
    public EvalResult test(Context context, A data) {
        return evaluator.apply(data)
                .match(__ -> success(),
                        fr -> evalResult(failure(this, fr.getReason())));
    }

    @Override
    public String getName() {
        return name;
    }

}
