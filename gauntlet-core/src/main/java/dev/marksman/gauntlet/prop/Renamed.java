package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class Renamed<A> implements Prop<A> {
    private final String name;
    @Getter
    private final Prop<A> underlying;

    @Override
    public EvalResult evaluate(A data) {
        return underlying.evaluate(data);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Prop<A> rename(String name) {
        return Props.named(name, underlying);
    }
}
