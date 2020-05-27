package dev.marksman.gauntlet.prop;

import com.jnape.palatable.lambda.functions.Fn1;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

final class Dynamic<A> implements Prop<A> {
    private final Fn1<A, Prop<A>> selector;

    public Dynamic(Fn1<A, Prop<A>> selector) {
        this.selector = selector;
    }

    @Override
    public EvalResult evaluate(A data) {
        return selector.apply(data).evaluate(data);
    }

    @Override
    public String getName() {
        return "Dynamic - TODO";
    }
}
