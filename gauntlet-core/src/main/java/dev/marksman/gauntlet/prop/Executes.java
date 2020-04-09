package dev.marksman.gauntlet.prop;

import dev.marksman.gauntlet.EvalResult;
import dev.marksman.gauntlet.Prop;

import java.util.function.Consumer;

import static dev.marksman.gauntlet.EvalSuccess.evalSuccess;

final class Executes<A> implements Prop<A> {
    private final String name;
    private final Consumer<A> executable;

    Executes(String name, Consumer<A> executable) {
        this.name = name;
        this.executable = executable;
    }

    @Override
    public EvalResult evaluate(A data) {
        executable.accept(data);
        return evalSuccess();
    }

    @Override
    public String getName() {
        return name;
    }

    static <A> Executes<A> executes(Consumer<A> consumer) {
        return new Executes<>("executes", consumer);
    }

    static <A> Executes<A> executes(String name, Consumer<A> consumer) {
        return new Executes<>(name, consumer);
    }
}
