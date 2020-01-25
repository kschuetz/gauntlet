package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;

final class AlwaysPass<A> implements Prop<A> {
    private final Name name;

    AlwaysPass(Name name) {
        this.name = name;
    }

    @Override
    public EvalResult test(Context context, A data) {
        return EvalResult.success();
    }

    @Override
    public Name getName() {
        return name;
    }
}
