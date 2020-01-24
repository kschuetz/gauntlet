package dev.marksman.gauntlet;

import com.jnape.palatable.lambda.adt.Either;
import com.jnape.palatable.lambda.adt.choice.Choice3;
import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.enhancediterables.ImmutableNonEmptyFiniteIterable;

public class Evaluator {

    public static <A> Either<Errors, EvalResult> evaluate(EvalState initialState,
                                                          Context context,
                                                          A data,
                                                          Fn1<EvalInfo, Choice3<Errors, EvalResult, EvalState>> fn,
                                                          ImmutableNonEmptyFiniteIterable<Prop<A>> properties) {
        return null;
    }

}
