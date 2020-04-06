package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.Context;
import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Name;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class Renamed<A> implements Prop<A> {
    private final Name name;
    final Prop<A> underlying;

    @Override
    public EvalResult test(Context context, A data) {
        return underlying.test(context, data);
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Prop<A> rename(Name name) {
        return Props.named(name, underlying);
    }
}
