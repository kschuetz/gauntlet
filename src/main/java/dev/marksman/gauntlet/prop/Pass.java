package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

final class Pass<A> implements Prop<A> {
    private static final Name NAME = Name.name("Pass");
    private static final Pass<?> INSTANCE = new Pass<>();

    @Override
    public EvalResult test(Context context, A data) {
        return EvalResult.success();
    }

    @Override
    public Name getName() {
        return NAME;
    }

    @SuppressWarnings("unchecked")
    static <A> Pass<A> pass() {
        return (Pass<A>) INSTANCE;
    }
}
