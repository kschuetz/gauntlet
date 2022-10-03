package software.kes.gauntlet.prop;

import software.kes.gauntlet.EvalResult;
import software.kes.gauntlet.Prop;

final class Renamed<A> implements Prop<A> {
    private final String name;
    private final Prop<A> underlying;

    Renamed(String name, Prop<A> underlying) {
        this.name = name;
        this.underlying = underlying;
    }

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
        return Facade.named(name, underlying);
    }

    public Prop<A> getUnderlying() {
        return this.underlying;
    }
}
